package com.proposito365.app.middleware.auth.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.proposito365.app.middleware.auth.AuthController;
import com.proposito365.app.middleware.auth.AuthService;
import com.proposito365.app.middleware.auth.utils.CookieProperties;
import com.proposito365.app.middleware.security.SecurityConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final Logger logger = Logger.getLogger(AuthController.class);
	private AuthService authService;
	private UserDetailsService userDetailsService;
	private final CookieProperties cookieProperties;

	public JwtAuthenticationFilter(AuthService authService, UserDetailsService userDetailsService,
									CookieProperties cookieProperties) {
		this.authService = authService;
		this.userDetailsService = userDetailsService;
		this.cookieProperties = cookieProperties;
	}

	@Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        final String requestURI = request.getRequestURI();
        return requestURI.startsWith(SecurityConfig.PREFIX_URL_MATCHER);
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
		boolean isRefresh = request.getRequestURI().equals("/refresh");
		final Optional<String> token = getJwtFromCookie(request, isRefresh);

        if (token.isEmpty() || !authService.validateToken(token.get())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new BadCredentialsException("Invalid token");
        }
		
        String userName = authService.getUserFromToken(token.get());
        UserDetails userDetails;
		logger.info("HOLLALALALALALALALALALALA   " + userName);
		if (isRefresh)
			userDetails = authService.loadUserById(Long.valueOf(userName));
		else
			userDetails = userDetailsService.loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwtFromCookie(HttpServletRequest request, boolean isRefresh) {
        final Cookie[] cookies = request.getCookies();
		final String cookieName = isRefresh == true ? cookieProperties.getNameRefresh() : cookieProperties.getName();
		if (cookies == null || cookies.length == 0)
            return Optional.empty();

        return (Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(cookieName))
            .map(Cookie::getValue)
            .findFirst());
    }

}