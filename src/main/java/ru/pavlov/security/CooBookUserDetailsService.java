package ru.pavlov.security;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ru.pavlov.domain.security.User;
import ru.pavlov.domain.security.UserRepository;
import ru.pavlov.domain.security.UserRoleRepository;

public class CooBookUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	private UserRoleRepository userRoleRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username);
		if (user != null) {
			throw new UsernameNotFoundException("No user present with username: "+username);
		}
		else {
			List<String> userRoles = userRoleRepository.findRoleByUserName(username);
			return new CookBookUserDetails(user, userRoles);
		}
	}

}
