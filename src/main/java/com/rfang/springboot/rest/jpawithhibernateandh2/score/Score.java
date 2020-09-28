package com.rfang.springboot.rest.jpawithhibernateandh2.score;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rfang.springboot.rest.jpawithhibernateandh2.client.Client;

@Entity
public class Score {
	
	private @Id @GeneratedValue Long id;
	private String name;
	private Long value;
	
	@OneToOne
	@JoinColumn(name = "client_id")
	@JsonBackReference
    private Client client;
	
	Score() {}
	
	Score(String name, Long value, Client client) {
		this.setName(name);
		this.setValue(value);
		this.setClient(client);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	@PreRemove
    private void preRemove() {
        client.setScore(null);
    }
	
}
