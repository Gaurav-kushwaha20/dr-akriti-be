package com.akriti.akriti.modules.user.service;

import com.akriti.akriti.dto.PaginatedResponse;
import com.akriti.akriti.modules.user.dto.request.AdminLogin;
import com.akriti.akriti.modules.user.dto.request.AdminReq;
import com.akriti.akriti.modules.user.dto.response.AdminDetails;
import com.akriti.akriti.modules.user.dto.response.AdminRes;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
            case "others" -> UserGender.OTHERS;
            default -> throw new RuntimeException(gender+" is not a valid gender choice, use male, female or others");
        };
    }

    // Does the User Exist by email, username, phone_no
    public void doesUserExist(String email, String username, String phone_no) {
        if (userRepo.existsByEmail(email)) throw new RuntimeException("User with email "+ email+ " already exist");
        if (userRepo.existsByUsername(username)) throw new RuntimeException("User with username " + username + " already exist");
        if (userRepo.existsByPhone(phone_no)) throw new RuntimeException("User with phone no. " + phone_no + " already exist");
    }

    // Does the user Exist by email, username, phone_no and id not
    public void doesUserExistAndIdNot(String email, String username, String phone_no, String userId){
        if (userRepo.existsByEmailAndIdNot(email, userId)) {
            throw new RuntimeException("User with email " + email + " already exists");
        }
        if (userRepo.existsByUsernameAndIdNot(username, userId)) {
            throw new RuntimeException("User with username " + username + " already exists");
        }
        if (userRepo.existsByPhoneAndIdNot(phone_no, userId)) {

            System.out.println("test on phone no.");
            throw new RuntimeException("User with phone " + phone_no + " already exists");
        }
    }

    // Get the user details
    public UserEntity getUserById(String id){
        return userRepo.findById(id).orElseThrow(()-> new RuntimeException("No user found with id " + id));
    }

    // Does Admin Exist
    public boolean doesAnyAdminExist() {
        return userRepo.existsByRole(UserRole.ADMIN);
    }

    // Save the user
    public UserEntity saveUser(UserEntity user) {
        return userRepo.save(user);
    }

    // Create New Admin
    public CreateAdminRes createAdmin(AdminReq request) {
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

    // Admin Login
    public Map<String, Object> adminLogin(AdminLogin request) {
        UserEntity user = userRepo.findByUsername(request.getUsername());
        if (user == null) throw new RuntimeException("User not found");
        if (!this.checkPassword(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Password doesn't match");
        AdminRes admin = this.mapToAdminResponse(user);
        Token token = this.getNewToken(user.getId(), user.getRole().toString(), user.getUserType().toString(), user.getEmail(), user.getFirstName(), user.getLastName());
        return Map.of("user", admin, "token", token);
    }

    // Get All Admin - Paginated Data
    public Page<AdminRes> getAllAdmin(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<UserEntity> userEntityPage = userRepo.findByRole(UserRole.ADMIN, pageable);
        return userEntityPage.map(this::mapToAdminResponse);
    }

    // Update Admin
    public UserEntity updateAdmin(AdminReq request, UserEntity user){
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());
        user.setDob(LocalDate.parse(request.getDob()));
        user.setGender(this.getGender(request.getGender()));
        return userRepo.save(user);
    }

    // Get Admin Details
    public AdminDetails getAdminDetails(String id) {
        UserEntity user = this.getUserById(id);
        return this.mapToAdminDetails(user);
    }


    // convert page<UserEntity> to List<AdminList>
    public PaginatedResponse<AdminRes> getPaginatedData(Page<AdminRes> userPage) {
        return PaginatedResponse.<AdminRes>builder()
                .page(userPage.getNumber() + 1)
                .pageSize(userPage.getSize())
                .totalPages(userPage.getTotalPages())
                .totalItems(userPage.getTotalElements())
                .counter(userPage.getNumber() * userPage.getSize())
                .data(userPage.toList())
                .build();
    }

    // Generate new token
    public Token getNewToken(String id, String role, String userType, String email, String firstName, String lastName) {
        String refresh = this.generateRefreshToken(id, role, userType, email, firstName, lastName);
        String access = this.generateAccessToken(id, role, userType, email, firstName, lastName);
        return Token.builder().access(access).refresh(refresh).build();
    }

    // Check the password
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    // Encrypt the password
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Map to Admin Details
    public AdminDetails mapToAdminDetails(UserEntity user) {
        return AdminDetails.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .dob(user.getDob())
                .role(user.getRole())
                .userType(user.getUserType())
                .build();
    }

    // Map to Admin Response
    public AdminRes mapToAdminResponse(UserEntity user) {
        return AdminRes.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }


    // Map to CreateAdminResponse
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
