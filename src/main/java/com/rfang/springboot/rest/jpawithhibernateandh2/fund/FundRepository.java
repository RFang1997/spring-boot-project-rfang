package com.rfang.springboot.rest.jpawithhibernateandh2.fund;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FundRepository extends JpaRepository<Fund, Long> {
	
	 public Optional<Fund> findByName(String name);

}
