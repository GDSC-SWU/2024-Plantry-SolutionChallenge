package com.gdscplantry.plantry.global.common.filter;

import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.error.ErrorCode;
import com.gdscplantry.plantry.global.error.GlobalErrorCode;
import com.gdscplantry.plantry.global.error.exception.FilterException;
import com.gdscplantry.plantry.global.util.JwtUtil;
import com.gdscplantry.plantry.global.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter implements Filter {
    private final JwtUtil jwtUtil;

    final String LOGIN_PATH = "/api/v1/user/google";
    final String TOKEN_PATH = "/api/v1/user/token";
    final String TERMS_PATH = "/api/v1/mypage/terms";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Filter initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();

        // Pass filter when login requested.
        if (requestURI.equals(LOGIN_PATH) && req.getMethod().equals("GET")) {
            chain.doFilter(request, response);
            return;
        } else if (requestURI.equals(TOKEN_PATH) && req.getMethod().equals("GET")) {
            chain.doFilter(request, response);
            return;
        } else if (requestURI.equals(TERMS_PATH) && req.getMethod().equals("GET")) {
            chain.doFilter(request, response);
            return;
        } else if (requestURI.contains("test")) {
            chain.doFilter(request, response);
            return;
        }

        // Validate JWT
        try {
            String header = req.getHeader("Authorization");
            if (header == null)
                throw new FilterException(GlobalErrorCode.ACCESS_TOKEN_REQUIRED);

            User user = jwtUtil.validateToken(true, header);
            req.setAttribute("user", user);

            chain.doFilter(request, response);
        } catch (FilterException e) {
            ResponseUtil.setResponse(res, e.getErrorCode().getHttpStatus().value(), e.getMessage());
        } catch (JwtException e) {
            ErrorCode code = GlobalErrorCode.INVALID_TOKEN;
            if (e instanceof ExpiredJwtException)
                code = GlobalErrorCode.EXPIRED_JWT;

            ResponseUtil.setResponse(res, code.getHttpStatus().value(), code.getMessage());
        }
    }

    @Override
    public void destroy() {
        log.info("Filter destroyed.");
    }
}
