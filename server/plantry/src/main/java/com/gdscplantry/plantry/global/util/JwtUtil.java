package com.gdscplantry.plantry.global.util;

import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.model.JwtVo;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.builder;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final UserRepository userRepository;

    @Value("${JWT_ISSUER}")
    private String ISSUER;
    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET_KEY;

    private String PAYLOAD_KEY_ID = "id";
    private String PAYLOAD_KEY_EMAIL = "email";
    private String ACCESS_SUBJECT = "access";
    private String REFRESH_SUBJECT = "refresh";

    public JwtVo generateTokens(User user) {
        // Payloads
        Map<String, Object> payloads = new LinkedHashMap<>();
        payloads.put(PAYLOAD_KEY_ID, user.getId());
        payloads.put(PAYLOAD_KEY_EMAIL, user.getEmail());

        // Expiration time (access-1h / refresh-7d)
        Date now = new Date();
        Date accessExp = new Date(now.getTime() + Duration.ofHours(1).toMillis());
        Date refreshExp = new Date(now.getTime() + Duration.ofDays(7).toMillis());

        // Build
        String accessToken = builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(payloads)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(accessExp)
                .setSubject(ACCESS_SUBJECT)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes()))
                .compact();
        String refreshToken = builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(payloads)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(refreshExp)
                .setSubject(REFRESH_SUBJECT)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes()))
                .compact();

        return new JwtVo(accessToken, refreshToken);
    }
}
