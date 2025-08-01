package com.example.project_java_web_service_nguyenvandoan.service;

import com.example.project_java_web_service_nguyenvandoan.entity.RefreshToken;
import com.example.project_java_web_service_nguyenvandoan.entity.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user, String ip);
    boolean isValid(RefreshToken token, String ip);
    void deleteByUser(User user);
    Optional<RefreshToken> findByToken(String token);

    void manageRefreshTokenLimit(User user, String ip);
}
