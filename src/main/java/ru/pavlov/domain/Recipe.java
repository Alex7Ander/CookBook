package ru.pavlov.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "recipelist")
public class Recipe {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;	
	
	private String type;
	private String name;
	private String tagline;
	private String text;
	
	public Recipe() {		
	}

	public Recipe(String name, String type, String tagline, String text) {
		this.type = type;
		this.name = name;
		this.tagline = tagline;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
		
}