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
import javax.persistence.Table;

@Entity
@Table(name = "ingredients")
public class Ingredient {
	public Ingredient() {}
	public Ingredient(String name, int protein, int fat, int carbohydrate) {
		super();
		this.name = name;
		this.protein = protein;
		this.fat = fat;
		this.carbohydrate = carbohydrate;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String name;
	private String description;
	private int protein;
	private int fat;
	private int carbohydrate;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "recipes_ingredients", joinColumns = @JoinColumn(name = "ingredient_id"), inverseJoinColumns=@JoinColumn(name = "recipe_id"))
	private List<Recipe> recipes;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProtein() {
		return protein;
	}
	public void setProtein(int protein) {
		this.protein = protein;
	}
	public int getFat() {
		return fat;
	}
	public void setFat(int fat) {
		this.fat = fat;
	}
	public int getCarbohydrate() {
		return carbohydrate;
	}
	public void setCarbohydrate(int carbohydrate) {
		this.carbohydrate = carbohydrate;
	}
	
}
