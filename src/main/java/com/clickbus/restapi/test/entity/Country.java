package com.clickbus.restapi.test.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
public class Country implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7772796242354984032L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 100)
	private String name;
	@Column(name = "created_at")
	@CreationTimestamp
	private Date createdAt;
	@Column(name = "updated_at")
	@UpdateTimestamp
	private Date updatedAt;
}
