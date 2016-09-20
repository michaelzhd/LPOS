package edu.sjsu.LPOS.auth;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import edu.sjsu.LPOS.util.EncryptionUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	
	public static Claims parseJwt(String jwt, String b64Security) {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(EncryptionUtil.encodeBase64(b64Security).getBytes())
					.parseClaimsJws(jwt)
					.getBody();
			return claims;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String createJwt(String username, String role, 
			String audience, String issuer, long TTLMilliSeconds, String b64Security) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		long currentTimeMillis = System.currentTimeMillis();
		Date now = new Date(currentTimeMillis);
		byte[] keySecretBytes = EncryptionUtil.encodeBase64(b64Security).getBytes();
		Key keySecret = new SecretKeySpec(keySecretBytes, signatureAlgorithm.getJcaName());
		
		JwtBuilder jwtBuilder = Jwts.builder()
								.setHeaderParam("typ", "JWT")
								.claim("role", role)
								.claim("username", username)
								.setIssuer(issuer)
								.setAudience(audience)
								.signWith(signatureAlgorithm, keySecret);
		if (TTLMilliSeconds >= 0) {
			long expireTimeMillis = currentTimeMillis + TTLMilliSeconds;
			Date expireDate = new Date(expireTimeMillis);
			jwtBuilder.setExpiration(expireDate).setNotBefore(now);
		}
		return jwtBuilder.compact();
	}
}
