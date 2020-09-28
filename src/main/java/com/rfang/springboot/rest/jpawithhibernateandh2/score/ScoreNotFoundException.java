package com.rfang.springboot.rest.jpawithhibernateandh2.score;

public class ScoreNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	ScoreNotFoundException(Long id) {
	    super("Could not find score " + id);
	} 

}
