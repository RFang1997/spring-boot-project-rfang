package com.rfang.springboot.rest.jpawithhibernateandh2.score;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {

}
