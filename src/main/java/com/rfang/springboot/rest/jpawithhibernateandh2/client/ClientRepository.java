package com.rfang.springboot.rest.jpawithhibernateandh2.client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
