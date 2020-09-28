package com.rfang.springboot.rest.jpawithhibernateandh2.fund;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FundController {
	
	private final FundRepository repository;
	
	private final FundModelAssembler assembler;
	
	FundController(FundRepository repository, FundModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}
	
	@GetMapping("/funds")
	CollectionModel<EntityModel<Fund>> getAllFunds() {
		List<EntityModel<Fund>> funds = this.repository.findAll().stream() //
			      .map(this.assembler::toModel) //
			      .collect(Collectors.toList());

		return CollectionModel.of(funds, linkTo(methodOn(FundController.class).getAllFunds()).withSelfRel());
	}
		
	@PostMapping("/funds")
	ResponseEntity<EntityModel<Fund>> createFund(@RequestBody Fund newFund) {
		EntityModel<Fund> entityModel = this.assembler.toModel(this.repository.save(newFund));
		
		return ResponseEntity //
				  .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
			      .body(entityModel);
	}
	
	@GetMapping(path="/funds/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	EntityModel<Fund> getFund(@PathVariable Long id) {
	    Fund fund = this.repository.findById(id)
	      .orElseThrow(() -> new FundNotFoundException(id));
	    
	    return this.assembler.toModel(fund);
	}
	
	@PutMapping("/funds/{id}")
	ResponseEntity<?> modifyFund(@RequestBody Fund newFund, @PathVariable Long id) {
		Fund updatedFund = this.repository.findById(id)
	      .map(fund -> {
	        fund.setName(newFund.getName());
	        fund.setClients(newFund.getClients());
	        return this.repository.save(fund);
	      })
	      .orElseThrow(() -> new FundNotFoundException(id));
		
		EntityModel<Fund> entityModel = this.assembler.toModel(updatedFund);

		return ResponseEntity //
			.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
			.body(entityModel);
	}
	
	@DeleteMapping("/funds/{id}")
	ResponseEntity<?> deleteFund(@PathVariable Long id) {
	    this.repository.deleteById(id);
	    
	    return ResponseEntity.noContent().build();
	}
	
	@GetMapping(path="/funds/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	EntityModel<Fund> getFund(@PathVariable String name) {
	    Fund fund = this.repository.findByName(name)
	      .orElseThrow(() -> new FundNotFoundException(name));
	    
	    return this.assembler.toModel(fund);
	}
}
