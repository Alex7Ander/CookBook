package ru.pavlov.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recipe_photos")
public class RecipePhoto {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;	
	private String photoPath;
	@ManyToOne
	@JoinColumn(name="recipeId")
	private Recipe recipe;
	private boolean isPreview;
	
	
	public RecipePhoto() {}
	
	public RecipePhoto(String photoPath, Recipe recipe) {
		this.photoPath = photoPath;
		this.recipe = recipe;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public boolean isPreview() {
		return isPreview;
	}

	public void setPreview(boolean isPreview) {
		this.isPreview = isPreview;
	}	
}
