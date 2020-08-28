package ru.pavlov.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "ingredients")
public class Ingredient {
	public Ingredient() {}
	public Ingredient(String name, String type, String description, double protein, double fat, double carbohydrate) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.protein = protein;
		this.fat = fat;
		this.carbohydrate = carbohydrate;
		this.calorie = this.fat * 9 + this.protein * 4 + this.carbohydrate * 4;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="userId")
	@JsonIgnore
	private User user;
	
	private String name;
	private String type;
	@JsonProperty("descr")
	private String description;
	@JsonProperty("prot")
	private double protein;
	private double fat;
	@JsonProperty("carbo")
	private double carbohydrate;

	private double calorie;
	private boolean common;
	@Column(name = "image", columnDefinition="longblob", length=2*1024*1024*1024)
    @Lob()
	private byte[] image;
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getProtein() {
		return protein;
	}
	public void setProtein(double protein) {
		this.protein = protein;
	}
	public double getFat() {
		return fat;
	}
	public void setFat(double fat) {
		this.fat = fat;
	}
	public double getCarbohydrate() {
		return carbohydrate;
	}
	public void setCarbohydrate(double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getCalorie() {
		return calorie;
	}
	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}
	public boolean isCommon() {
		return common;
	}
	public void setCommon(boolean common) {
		this.common = common;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}

}
