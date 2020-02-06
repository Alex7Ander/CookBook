package ru.pavlov.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ru.pavlov.domain.User;

public class CookBookUserDetails implements UserDetails{
	private List<GrantedAuthority> userRoles;
	private User user;
	
	public CookBookUserDetails(User user) {
		this.user = user;
		userRoles = new ArrayList<GrantedAuthority>();
		userRoles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		return userRoles;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUserLoginName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
