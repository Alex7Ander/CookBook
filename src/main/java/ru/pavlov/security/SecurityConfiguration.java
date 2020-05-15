package ru.pavlov.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired 
	private UserDetailsService userDetailsService;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
			.antMatchers("/resources/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			//.anyRequest().authenticated()		
			.antMatchers("/regPage").permitAll()
			.antMatchers("/user/**", "/recipe/**", "/ingredient/**", "/cookbook/**").hasAnyRole("ADMIN", "USER")
			.antMatchers("/admin/**").hasRole("ADMIN")
			.antMatchers("/img/**", "/uploadimg/**").permitAll()
			.and().formLogin().loginPage("/login").permitAll().loginProcessingUrl("/login").successHandler(cookbookSuccessHandler())
			.and().logout().permitAll();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth. userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	//return new BCryptPasswordEncoder(10); 
	}
	
	@Bean 
	public AuthenticationSuccessHandler cookbookSuccessHandler() {
		return new CookBookAuthSuccessHandler();
		
	}

}
