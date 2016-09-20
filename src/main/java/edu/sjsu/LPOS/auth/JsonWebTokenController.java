package edu.sjsu.LPOS.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.repository.UserRepository;
import edu.sjsu.LPOS.service.RedisTokenStoreService;
import edu.sjsu.LPOS.util.EncryptionUtil;

@RestController
public class JsonWebTokenController {
	@Autowired
	private Audience audience;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RedisTokenStoreService tokenService;
	
	@RequestMapping(value = "auth/token", method = RequestMethod.POST)
	public ResponseEntity<AuthResponse> getJsonWebToken(@RequestBody AuthRequest authRequest) {
		
		String username = authRequest.getUsername();
		String password = authRequest.getPassword();
		if (username == null || password == null) {
			return new ResponseEntity<AuthResponse>(HttpStatus.BAD_REQUEST);
		}
		User user = userRepository.findByUsername(username);
		
		if (user != null) {
			String digestedPassword = EncryptionUtil.digestWithMD5(password);
			if (! digestedPassword.equals(user.getPassword())) {
				return new ResponseEntity<AuthResponse>(HttpStatus.UNAUTHORIZED);
			}
			String token = JwtUtil.createJwt(username, user.getAuthorities(), audience.getClientId(),
					audience.getClientName(), audience.getExpiresInSeconds() * 1000, audience.getClientSecret());
			tokenService.set(user.getUsername(), token);
			String result = tokenService.get(user.getUsername());
			if (result != null) {
				System.out.println("got result from redis: " + result);
			}
			
			AccessToken accessToken = new AccessToken();
			accessToken.setAccessToken(token);
			accessToken.setExpireInMillis(audience.getExpiresInSeconds() * 1000);
			accessToken.setTokenType("bearer");
			
			AuthResponse authResponse = new AuthResponse();
			authResponse.setAccessToken(accessToken);
			authResponse.setRoles(user.getAuthorities());
			authResponse.setUsername(username);
			return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
		} else {
			return new ResponseEntity<AuthResponse>(HttpStatus.UNAUTHORIZED);
		}
	}
}
