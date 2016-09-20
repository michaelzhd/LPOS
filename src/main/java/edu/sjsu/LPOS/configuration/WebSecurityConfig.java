package edu.sjsu.LPOS.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import edu.sjsu.LPOS.auth.Audience;
import edu.sjsu.LPOS.auth.HttpBasicFilter;
import edu.sjsu.LPOS.auth.JwtAuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement

public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserDetailsService userDetailsService;
	
//	@Autowired
//	private AuthenticationEntryPoint authenticationEntryPoint;
	
	
	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
			.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
    @Bean
    public static Audience getAudience() throws Exception{
    	Audience audience = new Audience();
    	audience.setClientId("098f6bcd4621d373cade4e832627b4f6");
    	audience.setClientName("restapiuser");
    	audience.setClientSecret("MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY");
    	audience.setExpiresInSeconds(172800);
    	return audience;
    }
    
    @Bean
    public HttpBasicFilter httpBasicFilter() throws Exception {
    	HttpBasicFilter filter = new HttpBasicFilter();
    	filter.setAuthenticationManager(super.authenticationManagerBean());
    	return filter;
    }
    
    @Bean
    public JwtAuthorizationFilter jwtFilterRegistrationBean() throws Exception {
    	JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
    	filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }

    
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/**")
                	.authorizeRequests()
//                	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/auth/**").permitAll()
//                .antMatchers("/user/**").permitAll()
                .anyRequest().authenticated();

        //JWT based authentication
        httpSecurity
        		.addFilterBefore(jwtFilterRegistrationBean(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilterRegistrationBean(), UsernamePasswordAuthenticationFilter.class);
    }

}
