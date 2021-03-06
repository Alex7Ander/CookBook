package ru.pavlov.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.pavlov.domain.User;
import ru.pavlov.domain.UserRole;

public class CookBookUserDetails implements UserDetails{
	
	private List<GrantedAuthority> userRoles;
	private User user;
	
	public CookBookUserDetails(User user) {
		this.user = user;		
		userRoles = new ArrayList<GrantedAuthority>();
		for (UserRole role : this.user.getRoles()) {
			GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_" + role.getRole());
			userRoles.add(ga);
		}
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		return userRoles;
	}

	@Override
	public String getPassword() {
		String pass = this.user.getPassword();
		return pass;
	}

	@Override
	public String getUsername() {
		String login = this.user.getUserLoginName();
		return login;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
