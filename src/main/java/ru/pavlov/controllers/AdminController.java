package ru.pavlov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.pavlov.domain.User;
import ru.pavlov.repos.UserRepository;

@Controller
@RequestMapping("/admin/**")
public class AdminController {

	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("users")
	public String adminPage(Model model) {
		Iterable<User> users = userRepo.findAll();
		model.addAttribute("users", users);
		return "adminpage";
	}
}