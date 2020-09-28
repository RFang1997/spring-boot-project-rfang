package com.rfang.springboot.rest.jpawithhibernateandh2.client;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rfang.springboot.rest.jpawithhibernateandh2.fund.Fund;
import com.rfang.springboot.rest.jpawithhibernateandh2.score.Score;

@Entity
public class Client {
	
	private @Id @GeneratedValue Long id;
	private String name;

	@OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
	@JsonManagedReference
    private Score score;
	
	@ManyToOne
	@JoinColumn(name = "fund_id")
	@JsonBackReference
    private Fund fund;
	
	Client() {}
	
	Client(String name, Score score, Fund fund) {
		this.setName(name);
		this.setScore(score);
		this.setFund(fund);
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
	
	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

}
