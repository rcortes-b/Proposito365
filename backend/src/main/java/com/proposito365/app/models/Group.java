package com.proposito365.app.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "groups_table")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@Column(name="name")
	private String name;
	@Column(name="description")
	private String description;
	@Column(name="capacity")
	private int capacity;

	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<UserGroup> users;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Set<UserGroup> getUsers() {
		return users;
	}

	public void setUsers(Set<UserGroup> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", description=" + description + ", capacity=" + capacity + "]";
	}
}
