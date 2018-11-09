package com.clickbus.restapi.test.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "place_client_application")
@Data
public class PlaceClientApplication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7718263320921980770L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne()
	@JoinColumn(name = "place_id", referencedColumnName = "id")
	private Place placeId;
	@ManyToOne()
	@JoinColumn(name = "client_id", referencedColumnName = "id")
	private ClientApplication clientId;

}
