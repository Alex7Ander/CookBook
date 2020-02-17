package ru.pavlov.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;;

@Entity
@Table(name="users_roles")
public class UserRole {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
	private long id;
	
	@Column(name="role")
	private String role;
	
	@ManyToMany
	@JoinTable(name="users_roles", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users;
	
	public UserRole() {}
	public UserRole(String role) {
		this.role = role;
	}
	
	public long getId() {
		return this.id;
	}
	public String getRole() {
		return this.role;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
}