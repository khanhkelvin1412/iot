package com.hust.khanhkelvin.service.impl;

import com.hust.khanhkelvin.config.AuthenticationProperties;
import com.hust.khanhkelvin.domain.AuthorityEntity;
import com.hust.khanhkelvin.domain.UserEntity;
import com.hust.khanhkelvin.dto.Authority;
import com.hust.khanhkelvin.dto.User;
import com.hust.khanhkelvin.dto.request.UserLoginRequest;
import com.hust.khanhkelvin.dto.request.UserRegisterRequest;
import com.hust.khanhkelvin.dto.response.AuthToken;
import com.hust.khanhkelvin.dto.response.UserInfo;
import com.hust.khanhkelvin.repository.AuthorityRepository;
import com.hust.khanhkelvin.repository.UserRepository;
import com.hust.khanhkelvin.security.AuthoritiesConstants;
import com.hust.khanhkelvin.security.SecurityUtils;
import com.hust.khanhkelvin.security.jwt.TokenProvider;
import com.hust.khanhkelvin.service.MailService;
import com.hust.khanhkelvin.service.UserService;
import com.hust.khanhkelvin.service.mapper.UserInfoMapper;
import com.hust.khanhkelvin.service.mapper.UserMapper;
import com.hust.khanhkelvin.utils.Constants;
import com.hust.khanhkelvin.utils.RandomUtil;
import com.hust.khanhkelvin.web.error.BadRequestAlertException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final AuthenticationProperties authenticationProperties;

    private final UserInfoMapper userInfoMapper;

    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, MailService mailService, AuthenticationManager authenticationManager, TokenProvider tokenProvider, AuthenticationProperties authenticationProperties, UserInfoMapper userInfoMapper, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.authenticationProperties = authenticationProperties;
        this.userInfoMapper = userInfoMapper;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public User ensureExisted(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestAlertException("User not found", "error.userNotFound"));
        return userMapper.toDto(userEntity);
    }

    @Override
    public User register(UserRegisterRequest request) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(request.getUsername());
        if (userEntity.isPresent()) {
            throw new BadRequestAlertException("Username was used", "usernameWasUsed");
        }

        // đăng ký với thông tin vừa nhập
        User userRegister = this.registerAccount(request.getUsername(), request.getPassword(), request.getEmail());

        // gửi mail xác thực
        mailService.sendMailActivate(userRegister);
        return userRegister;
    }

    @Override
    public AuthToken authenticate(UserLoginRequest request) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername().toLowerCase(),
                request.getPassword(), new ArrayList<>());
        authentication = authenticationManager.authenticate(authentication);

        // create access token
        String accessToken = tokenProvider.createToken(authentication, false);
        return AuthToken.builder()
                .accessToken(accessToken)
                .tokenType(AuthToken.TOKEN_TYPE_BEARER)
                .expiresIn(authenticationProperties.getTokenValidityInSeconds())
                .build();
    }

    @Override
    public UserInfo getCurrentUser() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (!currentUserLogin.isPresent()) {
            return null;
        }
        String currentUser = currentUserLogin.get();
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(currentUser);
        if (userEntityOptional.isPresent()) {
            return userInfoMapper.toDto(userEntityOptional.get());
        }
        return null;
    }

    /**
     * Thực hiện việc gửi mail bất đồng bộ, không sử dụng chung ở trong hàm có truy cập xuống db
     *
     * @param user
     */
    @Async
    public void sendEmailActivate(User user) {
        mailService.sendMailActivate(user);
    }

    /**
     * dang ky tai khoan
     *
     * @param username
     * @param password
     * @param email
     * @return
     */
    public User registerAccount(String username, String password, String email) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setEmail(email);
        userEntity.setActivated(true);
        userEntity.setStatus(Constants.ENTITY_STATUS.ACTIVE);
        Set<AuthorityEntity> authorities = new HashSet<>();
        authorityRepository.findByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
        userEntity.setAuthorities(authorities);
        userEntity.setActivationKey(RandomUtil.generateActivationKey());

        // set password with salt
        this.getSaltWithPassword(password, userEntity);
        UserEntity userEntityAfterRegister = userRepository.save(userEntity);
        return userMapper.toDto(userEntityAfterRegister);
    }

    /**
     * generate password with random salt + password by password encoder
     *
     * @param password
     * @param userEntity
     */
    public void getSaltWithPassword(String password, UserEntity userEntity) {
        String salt = RandomUtil.generateSalt();
        userEntity.setSalt(salt);
        String passwordHash = password.toLowerCase().concat(".").concat(salt);
        userEntity.setPassword(passwordEncoder.encode(passwordHash));
    }
}
