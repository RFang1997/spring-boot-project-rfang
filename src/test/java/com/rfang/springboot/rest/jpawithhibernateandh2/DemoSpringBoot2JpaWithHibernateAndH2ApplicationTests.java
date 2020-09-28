package com.rfang.springboot.rest.jpawithhibernateandh2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfang.springboot.rest.jpawithhibernateandh2.fund.Fund;
import com.rfang.springboot.rest.jpawithhibernateandh2.fund.FundRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DemoSpringBoot2JpaWithHibernateAndH2ApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private FundRepository fundRepository;

    @Test()
    @Order(1)
    void testRegistrationWorksThroughRepository() throws Exception {
      Fund fund = new Fund("Fund 1", null);
      
      // 1) creation of a fund
      fundRepository.save(fund);
      System.out.println("Created Id : " + fund.getId());
      Assertions.assertNotNull(fund.getId());
      
      // 2) query a created fund by id
      Fund fundFound = fundRepository.findById(fund.getId()).orElse(null);
      System.out.println("found fund by id : id -> " + fundFound.getId() + " name -> " + fundFound.getName());
      Assertions.assertEquals(fundFound.getName(), fund.getName());
      
      // 3) query a created fund by name
      Fund fundFound2 = fundRepository.findByName("Fund 1").orElse(null);
      
      System.out.println("found fund : id -> " + fundFound2.getId() + " name -> " + fundFound2.getName());
      Assertions.assertEquals(fundFound2.getName(), fund.getName());
    }
    
    @Test
    @Order(2)
    void testRegistrationWorksThroughWebLayer() throws Exception {
      Fund fund = new Fund("Fund 2", null);
      
      // 1) creation of a fund
      MvcResult result = 
    		  mockMvc.perform(MockMvcRequestBuilders.post("/funds")
              .contentType("application/json")
              .content(objectMapper.writeValueAsString(fund)))
              .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))  // http code : 201 (created)
      		  .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Fund 2"))
      		  .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
      		  .andReturn();
      
	  String json = result.getResponse().getContentAsString();
	  Fund fundCreated = objectMapper.readValue(json, Fund.class);
	  
      System.out.println("Created Id : " + fundCreated.getId());
      
      // 2) query a fund by id
      mockMvc.perform(MockMvcRequestBuilders.get("/funds/{id}", fundCreated.getId()))
    		  .andExpect(MockMvcResultMatchers.status().isOk())
    	      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    	      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Fund 2"))
    	      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));
      
      // 3) query a fund by name
      mockMvc.perform(MockMvcRequestBuilders.get("/funds/name/{name}", fundCreated.getName()))
    		  .andExpect(MockMvcResultMatchers.status().isOk())
    	      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    	      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Fund 2"))
    	      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));
      
    }

}
