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
	
	private Double volume;
	private Double resultCalorie;
	
	public IngredientVolume() {}
	
	public IngredientVolume(Ingredient ingredient, Double volume, Recipe recipe) {
		this.ingredient = ingredient;
		this.volume = volume;
		this.recipe = recipe;
		this.resultCalorie = this.volume * this.ingredient.getCalorie() / 100;
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
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public String getName() {
		return this.ingredient.getName();
	}

	public Double getResultCalorie() {
		return resultCalorie;
	}

	public void setResultCalorie(Double resultCalorie) {
		this.resultCalorie = resultCalorie;
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
