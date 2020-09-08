package ru.pavlov.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.pavlov.domain.Ingredient;
import ru.pavlov.domain.Recipe;
import ru.pavlov.domain.Review;
import ru.pavlov.domain.User;
import ru.pavlov.domain.UserRole;
import ru.pavlov.repos.IngredientRepository;
import ru.pavlov.repos.RecipeRepository;
import ru.pavlov.repos.ReviewRepository;
import ru.pavlov.repos.UserRepository;
import ru.pavlov.wrappers.LinkWrapper;
import ru.pavlov.mail.MailSender;

@Controller
@RequestMapping("/admin/**")
public class AdminController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RecipeRepository recipeRepo;
	
	@Autowired
	private IngredientRepository ingredientRepo;
	
	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired
	private MailSender mailSender;
	
	@GetMapping("users")	
	public String adminPageUsers(Model model) {
		Iterable<User> users = userRepo.findAll();
		model.addAttribute("users", users);
		return "adminpage_users";
	}
	
	@GetMapping("loadUser")
	@ResponseBody
	public String loadUser(@RequestParam long id){
		User user = userRepo.findById(id);
		ObjectMapper jsonCreator = new ObjectMapper();
		String jsonResponse = null;
		try {
			jsonResponse = jsonCreator.writeValueAsString(user);
		}
		catch(JsonProcessingException jpExp) {
			jsonResponse = "{\"error\":\"" + jpExp.getMessage() + "\"}";
		}
		return jsonResponse;
	}
	
	@PostMapping("deleteUser")
	@ResponseBody
	public String deleteUser(@RequestParam long id) {
		User user = userRepo.findById(id);
		String jsonResponse = null;
		try {
			this.userRepo.delete(user);
			jsonResponse = "\"success\":\"user_deleted\"";
		}
		catch(Exception exp) {
			jsonResponse = "\"error\":\"" + exp.getMessage() + "\"";
		}
		return jsonResponse;
	}
	
	@PostMapping("addNewUser")
	@ResponseBody
	public String addNewUser(@RequestParam String login, @RequestParam String password, @RequestParam String email, @RequestParam String role) {
		UserRole userRole = new UserRole(role);
		List<UserRole> userRoles = new ArrayList<UserRole>();
		userRoles.add(userRole);
		User user = new User(login, password, email, userRoles);
		
		ObjectMapper jsonCreator = new ObjectMapper();
		String jsonResponse = null;
		try {
			this.userRepo.save(user);
			jsonResponse = jsonCreator.writeValueAsString(user);
		}
		catch(Exception exp) {
			jsonResponse = "{\"error\":\"" + exp.getMessage() + "\"}";
		}
		return jsonResponse;
	}	
	
	@GetMapping("recipes")
	public String adminPageRecipes(Model model) {
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "adminpage_recipes";
	}
	
	
	@GetMapping("ingredients")
	public String adminPageIngredinets(Model model, 
										@RequestParam(required = false) Integer pageIndex,
										@RequestParam(required = false) Integer common) {
		if(pageIndex == null) {
			pageIndex = 0;
		}		
		Pageable findSortedByType = PageRequest.of(pageIndex, 10, Sort.by("type"));
		Page<Ingredient> ingredients = null;
		if(common == null || common == -1) {
			ingredients = ingredientRepo.findAll(findSortedByType);
		}
		else {
			boolean isCommon = false;
			if(common == 1) isCommon = true;
			ingredients = ingredientRepo.findByCommon(findSortedByType, isCommon);
		}		
		model.addAttribute("ingredients", ingredients);
		int pagesCount = ingredients.getTotalPages();
		List<LinkWrapper> links = new ArrayList<>();
		for (int i=0; i<pagesCount; i++) {
			LinkWrapper lw = new LinkWrapper();
			String link = "/admin/ingredients?pageIndex=" + i;
			if(common != null && common > -1)
				link = link + ("&common=" + common);
			lw.setLink(link);
			lw.setIndex(i+1);
			links.add(lw);
		}
		model.addAttribute("linkWrappers", links);
		return "adminpage_ingredients";
	}
	
	@PostMapping("deleteIngredient")
	@ResponseBody
	public String deleteIngredient(@RequestParam long id) {
		String response = null;
		Ingredient ingredient = this.ingredientRepo.findById(id);
		try {
			this.ingredientRepo.delete(ingredient);
			response = "\"success\":\"user_deleted\"";
		}
		catch(Exception exp) {
			response = "\"error\":\"" + exp.getMessage() + "\"";
		}
		return response;
	}
	
	@GetMapping("reviews")
	public String adminPageMessages(Model model) {
		Iterable<Review> reviews = reviewRepo.findAll();
		model.addAttribute("reviews", reviews);
		return "adminpage_reviews";
	}
	
	@PostMapping("sendAnswer")
	@ResponseBody
	public String answerReview(@RequestParam long reviewId, @RequestParam String answer) {		
		Review review = this.reviewRepo.findById(reviewId);
		review.setAnswer(answer);
		this.reviewRepo.save(review);
		return "{\"done\":\"true\"}";
	}
	
	@PostMapping("deleteReview")
	@ResponseBody
	public String deleteReview(@RequestParam long reviewId) {
		Review review = this.reviewRepo.findById(reviewId);
		this.reviewRepo.delete(review);
		return "{\"done\": \"true\"}";
	}
	
	@GetMapping("sendmailwindow")
	public String sendmailwindow() {
		return "sendmailwindow";
	}
	
	@PostMapping("sendemail")
	@ResponseBody
	public String sendemail(@RequestParam String emailTo, @RequestParam String message) {
		mailSender.send(emailTo, "Тестирование отправки сообщений", message);
		return "{}";
	}
	


}