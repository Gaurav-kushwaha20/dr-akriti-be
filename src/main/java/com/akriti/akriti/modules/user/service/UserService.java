package com.akriti.akriti.modules.user.service;

import com.akriti.akriti.modules.user.dto.request.CreateAdminReq;
import com.akriti.akriti.modules.user.dto.response.CreateAdminRes;
import com.akriti.akriti.modules.user.dto.response.Token;
import com.akriti.akriti.modules.user.entity.UserEntity;
import com.akriti.akriti.modules.user.enums.UserGender;
import com.akriti.akriti.modules.user.enums.UserRole;
import com.akriti.akriti.modules.user.enums.UserType;
import com.akriti.akriti.modules.user.repo.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.security.jwt.secret}")
    private String jwtSecret;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private UserGender getGender(String gender) {
        if (gender == null) return UserGender.OTHERS;
        return switch (gender.toLowerCase()) {
            case "male" -> UserGender.MALE;
            case "female" -> UserGender.FEMALE;
            default -> UserGender.OTHERS;
        };
    }

    // Does the User Exist by email, username, phone_no
    public Object doesUserExist(String email, String username, String phone_no) {
        if (userRepo.existsByEmail(email)) return "user exist with email";
        if (userRepo.existsByUsername(username)) return "user exist with username";
        if (userRepo.existsByPhone(phone_no)) return "user exist with phone";
        return false;
    }

    // Does Admin Exist
    public boolean doesAdminExist() {
        return userRepo.existsByRole(UserRole.ADMIN);
    }

    // Save the user
    public UserEntity saveUser(UserEntity user) {
        return userRepo.save(user);
    }

    // Create New Admin
    public CreateAdminRes createAdmin(CreateAdminReq request) {
        UserEntity newUser = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .phone(request.getPhone())
                .userType(UserType.ADMIN)
                .role(UserRole.ADMIN)
                .dob(LocalDate.parse(request.getDob()))
                .password(this.encryptPassword(request.getPassword()))
                .gender(this.getGender(request.getGender()))
                .build();
        UserEntity savedUser = this.saveUser(newUser);
        return this.mapToCreateAdminRes(savedUser);
    }

    // Generate new token
    public Token getNewToken(String id, String role, String userType, String email, String firstName, String lastName) {
        String refresh = this.generateRefreshToken(id, role, userType, email, firstName, lastName);
        String access = this.generateAccessToken(id, role, userType, email, firstName, lastName);
        return Token.builder().access(access).refresh(refresh).build();
    }

    //    Check the password
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    //    Encrypt the password
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    //    Map to CreateAdminResponse
    public CreateAdminRes mapToCreateAdminRes(UserEntity user) {
        return CreateAdminRes.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .phone_no(user.getPhone())
                .gender(user.getGender())
                .dob(user.getDob().toString())
                .userType(user.getUserType())
                .role(user.getRole())
                .build();
    }

    //  Generate a Refresh token with your own user info
    public String generateRefreshToken(String id, String role, String userType, String email, String firstName, String lastName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("role", role);
        claims.put("userType", userType);
        claims.put("email", email);
        claims.put("firstName", firstName);
        claims.put("lastname", lastName);
        claims.put("tokenType", "refresh");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(String id, String role, String userType, String email, String firstName, String lastName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("role", role);
        claims.put("userType", userType);
        claims.put("email", email);
        claims.put("firstName", firstName);
        claims.put("lastname", lastName);
        claims.put("tokenType", "access");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //  Extract info back from token (optional)
    public Map<String, Object> extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
