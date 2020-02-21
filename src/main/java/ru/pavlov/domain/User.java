package ru.pavlov.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "cookbook_users")
public class User {
	public User(){		
	}
	
	public User(String userLoginName, String password, String name, String surname, String city, String temperament,
			String email, String phone, int enabled) {
		this.userLoginName = userLoginName;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.city = city;
		this.temperament = temperament;
		this.email = email;
		this.phone = phone;
		this.enabled = enabled;
	}
	
	public User(String userLoginName, String password, String email, List<UserRole> roles) {
		this.userLoginName = userLoginName;
		this.password = password;
		this.email = email;
		this.roles = roles;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
    private Long id;
	@Column(name = "login", unique = true)
	private String userLoginName;
    private String password; 
	
    private String name;
	private String surname;
	private String city;
	private String temperament;
	private String email;
	private String phone;	   
	private int enabled;
		
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns=@JoinColumn(name = "role_id"))
	private List<UserRole> roles;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTemperament() {
		return temperament;
	}

	public void setTemperament(String temperament) {
		this.temperament = temperament;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public List<UserRole> getRoles() {
		return roles;
	}
	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}
	
}