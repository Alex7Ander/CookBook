package ru.pavlov.domain;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
	@Lob
	private String text;
	private String youtubeLink;
	private String photoFolder;
	
	@Column(name = "preview_image", columnDefinition="longblob", length=2*1024*1024*1024)
    @Lob()
	private byte[] previewImage;	
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User recipeAuther;
	
	@OneToMany(mappedBy="recipe", fetch = FetchType.LAZY, cascade=CascadeType.ALL) //
	private List<IngredientVolume> ingredients;
	
	@OneToMany(mappedBy="recipe", fetch = FetchType.LAZY)
	private List<RecipePhoto> photos;
		
	public Recipe() {}

	public Recipe(User recipeAuther, String name, String type, String tagline, String youtubeLink, String text, List<IngredientVolume> ingredientsVolume) {
		this.recipeAuther = recipeAuther;
		this.type = type;
		this.name = name;
		this.tagline = tagline;
		this.youtubeLink = youtubeLink;
		this.text = text;
		this.ingredients = ingredientsVolume;
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

	public User getRecipeAuther() {
		return this.recipeAuther;
	}

	public void setRecipeAuther(User recipeAuther) {
		this.recipeAuther = recipeAuther;
	}

	public List<IngredientVolume> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<IngredientVolume> ingredients) {
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

	public String getPhotoFolder() {
		return photoFolder;
	}

	public void setPhotoFolder(String photoFolder) {
		this.photoFolder = photoFolder;
	}

	public byte[] getPreviewImage() {
		return previewImage;
	}

	public void setPreviewImage(byte[] previewImage) {
		this.previewImage = previewImage;
	}		
}