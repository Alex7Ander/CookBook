package ru.pavlov.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ingredient_volumes")
public class IngredientVolume {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="ingredient_id")
	private Ingredient ingredient;
	
	@ManyToOne
	@JoinColumn(name="recipe_id")
	private Recipe recipe;
	
	private String volume;
	
	public IngredientVolume() {}
	
	public IngredientVolume(Ingredient ingredient, String volume, Recipe recipe) {
		this.ingredient = ingredient;
		this.volume = volume;
		this.recipe = recipe;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Ingredient getIngredient() {
		return ingredient;
	}
	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getName() {
		return this.ingredient.getName();
	}
	public Recipe getRecipe() {
		return recipe;
	}
	@Override
	public boolean equals(Object o) {
		try {
			IngredientVolume otherIngredientVolume = (IngredientVolume)o;
			if (otherIngredientVolume.id == this.id) {
				return true;
			}
			else{
				return false;
			}
		}
		catch(ClassCastException ccExp) {
			return false;
		}
	}


}
