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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
public class Place implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2909853504640323264L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 255)
	private String name;
	@Column(name = "terminal_name", length = 255)
	private String terminalName;
	@Column(unique = true)
	private String slug;
	@Column()
	private String adress;
	@ManyToOne()
	@JoinColumn(name = "city_id", referencedColumnName = "id")
	private City cityId;
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

}
