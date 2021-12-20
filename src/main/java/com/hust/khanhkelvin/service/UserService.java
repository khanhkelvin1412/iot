package com.hust.khanhkelvin.service;

import com.hust.khanhkelvin.dto.User;
import com.hust.khanhkelvin.dto.request.UserLoginRequest;
import com.hust.khanhkelvin.dto.request.UserRegisterRequest;
import com.hust.khanhkelvin.dto.response.AuthToken;

public interface UserService {

    /**
     * kiem tra chac chan da ton tai User theo username
     *
     * @param username
     * @return
     */
    User ensureExisted(String username);

    /**
     * Dang ky tai khoan
     *
     * @param request
     * @return
     */
    User register(UserRegisterRequest request);

    /**
     * authenticate user
     *
     * @param request
     * @return
     */
    AuthToken authenticate(UserLoginRequest request);
}
