package com.proposito365.app.infrastructure.middleware.auth.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.proposito365.app.infrastructure.middleware.auth.UserSecurity;

@Service
public class TokenServiceImpl implements TokenService {
	private final static Logger logger = Logger.getLogger(TokenServiceImpl.class);

	@Value("${application.security.jwt.secret-key}")
	private String jwtKey;
	@Value("${application.security.jwt.expiration}")
	private int jwtKeyExpiration;

	private JwtEncoder jwtEncoder;
	private JwtDecoder jwtDecoder;

	public TokenServiceImpl(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
		this.jwtEncoder = jwtEncoder;
		this.jwtDecoder = jwtDecoder;
	}

	@Override
	public String generateToken(Authentication authentication, boolean isRefresh) {
		Instant now = Instant.now();
		logger.info("[TOKEN INFO] " + authentication.getDetails());
		logger.info("[TOKEN INFO] " + authentication.getPrincipal());
		UserSecurity currentUser = (UserSecurity)authentication.getPrincipal();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(isRefresh == true ?  currentUser.getId().toString() : currentUser.getUsername())
            .issuedAt(now)
            .expiresAt(now.plus(jwtKeyExpiration, ChronoUnit.MINUTES))
            .build();
		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
	}

	@Override
	public String getUserFromToken(String token) {
		Jwt jwtToken = jwtDecoder.decode(token);
		return jwtToken.getSubject();
	}

	@Override
	public boolean validateToken(String token) {
		try {
			jwtDecoder.decode(token);
			return true;
		} catch (Exception exception) {
			logger.error("[USER] : Error while trying to validate token", exception);
			throw new BadJwtException("Error validating token");
		}
	}
	
}
