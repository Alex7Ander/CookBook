package ru.pavlov.domain.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_roles")
public class UserRole {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
	@Column(name="user_role_id")
	private Long id;
	
	@Column(name="userid")
	private Long userid;
	
	@Column(name="role")
	private String role;	

}