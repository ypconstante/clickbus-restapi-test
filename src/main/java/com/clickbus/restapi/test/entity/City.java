package com.clickbus.restapi.test.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
public class City implements Serializable{

	private static final long serialVersionUID = -8913603256931329530L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne()
	@JoinColumn(name = "state_id", referencedColumnName = "id")
	private State stateId;
	@Column(length = 100)
	private String name;
	@Column(name = "created_at")
	@CreationTimestamp
	private Date createdAt;
	@Column(name = "updated_at")
	@UpdateTimestamp
	private Date updatedAt;
}
