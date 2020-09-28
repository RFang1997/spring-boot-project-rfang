package com.rfang.springboot.rest.jpawithhibernateandh2.fund;

public class FundNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FundNotFoundException(Long id) {
	    super("Could not find fund by id : " + id);
	}
	
	public FundNotFoundException(String name) {
	    super("Could not find fund by name : " + name);
	} 

}
