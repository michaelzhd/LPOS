package edu.sjsu.LPOS.auth;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	
	@Autowired private LocalAuthServerSetting setting;
	
	public  Claims parseJwt(String jwt) {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(setting.getClientSecret().getBytes())
					.parseClaimsJws(jwt)
					.getBody();
			return claims;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String createJwt(UserAuthorities userAuthorities, long TTLMilliSeconds) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		long currentTimeMillis = System.currentTimeMillis();
		Date now = new Date(currentTimeMillis);
		byte[] keySecretBytes = setting.getClientSecret().getBytes();
		Key keySecret = new SecretKeySpec(keySecretBytes, signatureAlgorithm.getJcaName());
		List<String> authorities = new ArrayList<>();
		for (GrantedAuthority authority: userAuthorities.getAuthorities()) {
			authorities.add(authority.toString());
		}
		
		JwtBuilder jwtBuilder = Jwts.builder()
								.setHeaderParam("typ", "JWT")
								.claim("username", userAuthorities.getUsername())
								.claim("authorities", authorities)
								.setIssuer(setting.getClientName())
								.signWith(signatureAlgorithm, keySecret);
		if (TTLMilliSeconds >= 0) {
			long expireTimeMillis = currentTimeMillis + TTLMilliSeconds;
			Date expireDate = new Date(expireTimeMillis);
			jwtBuilder.setExpiration(expireDate).setNotBefore(now);
		}
		return jwtBuilder.compact();
	}
}
