package com.hust.khanhkelvin.service;

import com.hust.khanhkelvin.dto.User;

public interface MailService {
    void sendMailActivate(User user);
}
