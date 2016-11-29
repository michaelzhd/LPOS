package edu.sjsu.LPOS.auth.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.LPOS.exception.NotVerifiedException;
import edu.sjsu.LPOS.util.EncryptionUtil;
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired private UserDetailsService userDetailsService;
	

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user == null) {
        	throw new BadCredentialsException("Username not found");
        }
        String md5password = EncryptionUtil.digestWithMD5(password);
        if (!md5password.equals(user.getPassword())) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }
        if (user.getAuthorities() == null) {
        	throw new InsufficientAuthenticationException("User has no roles assigned");
        }
        if (user.getAuthorities().contains("waitforverify")) {
        	throw new NotVerifiedException("Your account has not been verified yet. Please check your email.");
        }
        return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
