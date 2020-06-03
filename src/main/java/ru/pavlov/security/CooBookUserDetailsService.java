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
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUserLoginName(username);
		if (user == null) {
			throw new UsernameNotFoundException("No user present with login: " + username);
		}
		else {
			return new CookBookUserDetails(user);
		}
	}
	
	public boolean activateUser(String activationCode) {
		User user = userRepo.findByActivationCode(activationCode);
		if(user == null) {
			return false;
		}
		user.setActivationCode(null);
		userRepo.save(user);
		return true;
	}

}
