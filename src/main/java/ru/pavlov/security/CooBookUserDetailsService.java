package ru.pavlov.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.pavlov.domain.User;
import ru.pavlov.repos.UserRepository;

@Service
public class CooBookUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserLoginName(username);
		if (user == null) {
			throw new UsernameNotFoundException("No user present with login: " + username);
		}
		else {
			return new CookBookUserDetails(user);
		}
	}

}
