package ru.pavlov.domain;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "recipelist")
public class Recipe {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String type;
	private String tagline;
	private String text;
	private String youtubeLink;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User cooker;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "recipes_ingredients", joinColumns = @JoinColumn(name = "recipeId"), inverseJoinColumns = @JoinColumn(name = "ingredientId"))
	private List<Ingredient> ingredients;
	
	@OneToMany(mappedBy="recipe", fetch = FetchType.LAZY)
	private List<RecipePhoto> photos;
	
	public Recipe() {}

	public Recipe(User cooker, String name, String type, String tagline, String youtubeLink, String text, List<Ingredient> ingredients) {
		this.cooker = cooker;
		this.type = type;
		this.name = name;
		this.tagline = tagline;
		this.youtubeLink = youtubeLink;
		this.text = text;
		this.ingredients = ingredients;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public User getUserId() {
		return this.cooker;
	}

	public void setUserId(User cooker) {
		this.cooker = cooker;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public String getYoutubeLink() {
		return youtubeLink;
	}

	public void setYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
	}

	public List<RecipePhoto> getPhotos() {
		return photos;
	}

	public void setPhotos(List<RecipePhoto> photos) {
		this.photos = photos;
	}
		
}