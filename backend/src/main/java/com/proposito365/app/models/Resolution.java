package com.proposito365.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "resolutions")
public class Resolution {
	/*CREATE TABLE resolutions (
	`id` int AUTO_INCREMENT PRIMARY KEY,
	`user_id` int NOT NULL,
	`resolution` VARCHAR(255) NOT NULL,
	`details` VARCHAR(255),
	`status_id` int NOT NULL,
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (status_id) REFERENCES resolution_status(id)
); */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "user_id")
	private Long user_id;
	@Column(name = "resolution")
	private String resolution;
	@Column(name = "details")
	private String details;
	@Column(name = "status_id")
	private Long status_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Long getStatus_id() {
		return status_id;
	}

	public void setStatus_id(Long status_id) {
		this.status_id = status_id;
	}

	@Override
	public String toString() {
		return "Resolution [id=" + id + ", user_id=" + user_id + ", resolution=" + resolution + ", details=" + details
				+ ", status_id=" + status_id + "]";
	}	
}
