package ru.pavlov.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pavlov.domain.User;
import ru.pavlov.domain.UserRole;
import ru.pavlov.mail.MailSender;
import ru.pavlov.repos.UserRepository;
import ru.pavlov.repos.UserRoleRepository;
import ru.pavlov.security.CooBookUserDetailsService;

@Controller
public class MainController {
	
	@Autowired
	private CooBookUserDetailsService userDetailService;

	@Autowired 
	private UserRepository userRepo;
	
	@Autowired
	private UserRoleRepository userRoleRepo;

	@Autowired
	private MailSender mailSender;
	
	@GetMapping("registration")
	public String regPage() {
		return "registration";
	}
	
	@PostMapping("regUser")
	public String regUser(@RequestParam String login, 
						@RequestParam String password, 
						@RequestParam String password2, 
						@RequestParam String email, 
						@RequestParam String code,
						Model model) {
		
		System.out.println("Начата регистрация нового пользователя");
		if(!code.equals("CookBookGuest2043")) {
			return "notinvited";
		}
		
		System.out.println("Пользователь был приглашен и ввел правильный код");
		if(!password.equals(password2)) {
			model.addAttribute("errorMsg", "Пароли не совпадают");
			return "registration";
		}
		
		System.out.println("Пользователь правильно ввел пароли");
		User userByLogin = userRepo.findByUserLoginName(login);
		if (userByLogin != null) {
			model.addAttribute("errorMsg", "Введенный логин занят");
			System.err.println("Введенный пользователем логин занят");
			return "registration";
		}
		
		User userByEmail = userRepo.findByEmail(email);
		if (userByEmail != null) {
			model.addAttribute("errorMsg", "Введенный email занят");
			System.err.println("Введенный пользователем адресс электронной почты занят");
			return "registration";
		}	
		
		System.out.println("Пользователь ввел ранее не используемые логин и пароль");
		List<UserRole> roles = userRoleRepo.findByRole("USER");
		String userActivationCode = UUID.randomUUID().toString();
		User user = new User(login, password, email, roles);
		user.setActivationCode(userActivationCode);		
		String message = "Добро пожаловать! Вы в одном шаге от регистрации в книге рецептов CookBook.\n"
				+ "Пройдите по ссылке, что бы активировать свой аккаунт:\n\n"
				+ "localhost:5000/activate?code="+userActivationCode;
		try {
			mailSender.send(email, "Активация регистрации на сайте CookBook", message);
		}
		catch(Exception exp) {
			System.err.println("-------------------------ERROR---------------------------");
			exp.printStackTrace();
			System.err.println("---------------------------------------------------------");
			model.addAttribute("errorMsg", "Ошибка отправки письма с активацией аккаунта.");
			userRepo.delete(user);
			return "registration";
		}	
		
		System.out.println("Письмо отправлено успешно");
		userRepo.save(user);
		System.out.println("Пользователь сохранен в БД");
		return "redirect:/login";
	}
	
	@GetMapping("activate")
	public String activate(@RequestParam String code) {
		boolean isActivated = userDetailService.activateUser(code);
		if(isActivated) {
			return "redirect:/login";
		}
		else {
			return "notinvited";
		}		
	}
	
	@GetMapping("resurectionPage")
	public String resurectionPage() {
		return "resurection";
	}
	
	@GetMapping("resurection")
	public String resurection(@RequestParam String email, Model model) {
		User user = userRepo.findByEmail(email);
		if (user == null) {
			return "regUser";
		}
		String pass = user.getPassword();
		String message = "Пароль от вашего аккаунта в CookBook: " + pass;
		try {
			mailSender.send(email, "Запрос на восстановление пароля", message);
			model.addAttribute("errorMsg", "Письмо с паролем выслано на ваш адрес");
		}
		catch(Exception exp) {
			model.addAttribute("errorMsg", "Ошибка при отправке письма с паролем");
		}
		return "redirect:/login";
	}
	
	
}