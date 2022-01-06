package com.hust.khanhkelvin.dto.response;

import com.hust.khanhkelvin.domain.AuthorityEntity;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserInfo {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private boolean activated = false;

    private String imageUrl;

    private String description;

    private Set<AuthorityEntity> authorities = new HashSet<>();

    private Integer status;
}
