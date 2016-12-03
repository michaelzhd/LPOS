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
//        if(user.getAuthorities().indexOf("ROLE_waitforverify") != -1){
//            throw new DisabledException("Please check your email to confirm your registration");
//        }
//        List<String> authorities = new ArrayList<String>();
//        for (Authority authority: user.getAuthorities()) {
//        	authorities.add(authority.getName().toString());
//        }

        return new SecurityUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities())
//                AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",",authorities))
        );
	}

}
