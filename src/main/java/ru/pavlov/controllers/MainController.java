package ru.pavlov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pavlov.domain.User;
import ru.pavlov.domain.UserRole;
import ru.pavlov.repos.UserRepository;
import ru.pavlov.repos.UserRoleRepository;

@Controller
public class MainController {

	@Autowired 
	private UserRepository userRepo;
	@Autowired
	private UserRoleRepository userRoleRepo;

	@GetMapping("registration")
	public String regPage() {
		return "registration";
	}
	
	@PostMapping("regUser")
	public String regUser(@RequestParam String login, @RequestParam String password, @RequestParam String password2, @RequestParam String email, Model model) {
		if(!password.equals(password2)) {
			model.addAttribute("errorMsg", "Пароли не совпадают");
			return "registration";
		}
		User userByLogin = userRepo.findByUserLoginName(login);
		if (userByLogin != null) {
			model.addAttribute("errorMsg", "Введенный логин занят");
			return "registration";
		}
		User userByEmail = userRepo.findByEmail(email);
		if (userByEmail != null) {
			model.addAttribute("errorMsg", "Введенный email занят");
			return "registration";
		}		
		List<UserRole> roles = userRoleRepo.findByRole("USER");
		User user = new User(login, password, email, roles);
		userRepo.save(user);		
		return "redirect:/login";
	}

}