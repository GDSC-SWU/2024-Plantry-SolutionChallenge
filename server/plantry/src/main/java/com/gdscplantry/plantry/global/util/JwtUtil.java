package com.gdscplantry.plantry.global.util;

import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.model.JwtVo;
import com.gdscplantry.plantry.global.error.GlobalErrorCode;
import com.gdscplantry.plantry.global.error.exception.AppException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    @Value("${JWT_ISSUER}")
    private String ISSUER;
    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET_KEY;

    final String PAYLOAD_KEY_ID = "id";

    public JwtVo generateTokens(User user) {
        // Payloads
        final String PAYLOAD_KEY_EMAIL = "email";

        Map<String, Object> payloads = new LinkedHashMap<>();
        payloads.put(PAYLOAD_KEY_ID, user.getId());
        payloads.put(PAYLOAD_KEY_EMAIL, user.getEmail());

        // Expiration time (access-1h / refresh-7d)
        Date now = new Date();
        Date accessExp = new Date(now.getTime() + Duration.ofHours(1).toMillis());
        Date refreshExp = new Date(now.getTime() + Duration.ofDays(7).toMillis());

        // Build
        final String ACCESS_SUBJECT = "access";
        final String REFRESH_SUBJECT = "refresh";

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

    // Decode header
    public String decodeHeader(String header) {
        final String BEARER = "Bearer ";

        try {
            return Arrays.stream(header.split(BEARER)).toList().get(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AppException(GlobalErrorCode.INVALID_TOKEN);
        }
    }

    // Get payloads
    public Map<String, Object> getPayloads(String jwt) {
        return parser()
                .setSigningKey(JWT_SECRET_KEY.getBytes())
                .parseClaimsJws(jwt)
                .getBody();
    }

    // Validate token
    @Transactional(readOnly = true)
    public User validateToken(boolean isAccessToken, String header) throws AppException {
        // Decode header
        String token = decodeHeader(header);

        // Validate token
        // Get payload
        Map<String, Object> payloads = getPayloads(token);

        // Find user info
        User user = userRepository.findById(((Number) payloads.get(PAYLOAD_KEY_ID)).longValue())
                .orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

        // Find login info
        String refresh = redisUtil.opsForValueGet(user.getId() + "_refresh");

        if (refresh == null)
            throw new AppException(GlobalErrorCode.LOGIN_REQUIRED);
        else if (!isAccessToken && !refresh.equals(token))
            throw new AppException(GlobalErrorCode.AUTHORIZATION_FAILED);

        return user;
    }
}
