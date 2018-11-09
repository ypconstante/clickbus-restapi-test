package com.clickbus.restapi.test.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
public class City implements Serializable{

	private static final long serialVersionUID = -8913603256931329530L;
	
	@Id
	@GeneratedValue()
	private Long id;
	@ManyToOne()
	@JoinColumn(name = "state_id", referencedColumnName = "id")
	private State stateId;
	@Column(length = 100)
	private String name;
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
}
