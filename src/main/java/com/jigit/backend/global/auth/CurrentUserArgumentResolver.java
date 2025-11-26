package com.jigit.backend.global.auth;

import com.jigit.backend.global.exception.ApplicationException;
import com.jigit.backend.global.util.JwtUtil;
import com.jigit.backend.user.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * ArgumentResolver that extracts the current user's ID from JWT token.
 * Resolves parameters annotated with @CurrentUser in controller methods.
 */
@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    /**
     * Check if this resolver supports the given parameter.
     * Supports parameters with @CurrentUser annotation and Long type.
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(Long.class);
    }

    /**
     * Resolve the argument by extracting user ID from JWT token.
     * Throws ApplicationException if token is missing or invalid.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                   ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest,
                                   WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = request.getHeader("Authorization");

        // Extract token from Authorization header
        String token = jwtUtil.extractTokenFromHeader(authHeader);

        // Validate token exists and is valid
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new ApplicationException(UserException.UNAUTHORIZED);
        }

        // Extract and return user ID from token
        return jwtUtil.getUserIdFromToken(token);
    }
}