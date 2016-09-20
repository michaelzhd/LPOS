package edu.sjsu.LPOS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.SecurityUserDetails;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.repository.UserRepository;

@Service(value = "userDetailsService")
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("not found");
        }
//        if(user.getAuthorities().equals("ROLE_waitforverify")){
//            throw new DisabledException("Please check your email to confirm your registration");
//        }
        return new SecurityUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities())
        );
	}

}
