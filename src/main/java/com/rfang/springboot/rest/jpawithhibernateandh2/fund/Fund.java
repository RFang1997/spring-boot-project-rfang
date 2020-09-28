package com.rfang.springboot.rest.jpawithhibernateandh2.fund;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rfang.springboot.rest.jpawithhibernateandh2.client.Client;

@Entity
public class Fund {
	
	private @Id @GeneratedValue Long id;
	private String name;
	
	@OneToMany(mappedBy = "fund", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Collection<Client> clients;
	
	public Fund() {}
	
	public Fund(String name, Collection<Client> clients) {
		this.setName(name);
		this.setClients(clients);
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
	
	public Collection<Client> getClients() {
		return clients;
	}

	public void setClients(Collection<Client> clients) {
		this.clients = clients;
	}

}
