package edu.sjsu.LPOS.configuration;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import edu.sjsu.LPOS.auth.login.InterceptPathRequestMatcher;
import edu.sjsu.LPOS.auth.login.LoginAuthenticationProvider;
import edu.sjsu.LPOS.auth.login.LoginBasicFilter;
import edu.sjsu.LPOS.auth.token.JwtAuthorizationFilter;
import edu.sjsu.LPOS.auth.token.SkipPathRequestMatcher;
import edu.sjsu.LPOS.auth.token.TokenAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	public static final String LOGIN_ENTRY_POINT = "/auth/login";
	public static final String REFRESH_ENTRY_POINT = "/auth/refresh";
	public static final String REGISTER_ENTRY_POINT = "/user/register";
	
	@Autowired LoginAuthenticationProvider loginAuthenticationProvider;
	@Autowired TokenAuthenticationProvider tokenAuthenticationProvider;
	@Autowired UserDetailsService userDetailsService;
	@Autowired AuthenticationManager authenticationManager;
	@Autowired AuthenticationEntryPoint authenticationEntryPoint;
	
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
    public LoginBasicFilter loginBasicFilter() throws Exception {
    	List<String> pathToListen = Arrays.asList(LOGIN_ENTRY_POINT);
    	InterceptPathRequestMatcher matcher = new InterceptPathRequestMatcher(pathToListen);
    	LoginBasicFilter filter = new LoginBasicFilter(matcher);
    	filter.setAuthenticationManager(this.authenticationManager);
    	return filter;
    }
    
    @Bean
    public JwtAuthorizationFilter jwtFilterRegistrationBean() throws Exception {
    	List<String> pathToSkip = Arrays.asList(LOGIN_ENTRY_POINT,REFRESH_ENTRY_POINT,REGISTER_ENTRY_POINT);
    	SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathToSkip);
    	JwtAuthorizationFilter filter = new JwtAuthorizationFilter(matcher);
    	filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    protected void configure(AuthenticationManagerBuilder auth) {
    	auth.authenticationProvider(loginAuthenticationProvider);
    	auth.authenticationProvider(tokenAuthenticationProvider);
    }
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/**")
                	.authorizeRequests()
                		.antMatchers(LOGIN_ENTRY_POINT).permitAll()
                		.antMatchers(REFRESH_ENTRY_POINT).permitAll()
                		.antMatchers(REGISTER_ENTRY_POINT).permitAll()
                .anyRequest().authenticated();

        //JWT based authentication
        httpSecurity
        		.addFilterBefore(loginBasicFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilterRegistrationBean(), UsernamePasswordAuthenticationFilter.class);
    }

}
