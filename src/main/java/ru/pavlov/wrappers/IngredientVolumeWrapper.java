package ru.pavlov.wrappers;

import ru.pavlov.domain.IngredientVolume;

public class IngredientVolumeWrapper {

	private IngredientVolume ingredientVolume;
	private double resultCalorie;
	
	public IngredientVolumeWrapper(IngredientVolume ingredientVolume) {
		this.ingredientVolume = ingredientVolume;
		this.resultCalorie = this.ingredientVolume.getIngredient().getCalorie() * this.ingredientVolume.getVolume() / 100;
	}

	public double getVolume() {
		return this.ingredientVolume.getVolume();
	}
	public Long getId() {
		return this.ingredientVolume.getId();
	}
	public String getName() {
		return this.ingredientVolume.getName();
	}
	public double getIngredientCalorie() {
		return this.ingredientVolume.getIngredient().getCalorie();
	}
	
	public IngredientVolume getIngredientVolume() {
		return ingredientVolume;
	}

	public void setIngredientVolume(IngredientVolume ingredientVolume) {
		this.ingredientVolume = ingredientVolume;
	}

	public double getResultCalorie() {
		return resultCalorie;
	}
	
}