package com.rfang.springboot.rest.jpawithhibernateandh2.client;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rfang.springboot.rest.jpawithhibernateandh2.fund.Fund;
import com.rfang.springboot.rest.jpawithhibernateandh2.fund.FundNotFoundException;
import com.rfang.springboot.rest.jpawithhibernateandh2.fund.FundRepository;

@RestController
public class ClientController {
	
	private final ClientRepository clientRepository;
	
	private final FundRepository fundRepository;
	
	private final ClientModelAssembler assembler;
	
	ClientController(ClientRepository clientRepository, FundRepository fundRepository, ClientModelAssembler assembler) {
		this.clientRepository = clientRepository;
		this.fundRepository = fundRepository;
		this.assembler = assembler;
	}
	
	@GetMapping("/clients")
	CollectionModel<EntityModel<Client>> getAllClients() {
		List<EntityModel<Client>> clients = this.clientRepository.findAll().stream() //
			      .map(this.assembler::toModel) //
			      .collect(Collectors.toList());

		return CollectionModel.of(clients, linkTo(methodOn(ClientController.class).getAllClients()).withSelfRel());
	}
		
	@PostMapping("/clients/{id}")
	ResponseEntity<?> createClientByFundId(@RequestBody Client newClient, @PathVariable Long id) {
		Fund fund = this.fundRepository.findById(id).orElseThrow(() -> new FundNotFoundException(id));
		
		newClient.setFund(fund);
		Client updatedClient = this.clientRepository.save(newClient);
		
		Collection<Client> newClientsList = fund.getClients();
		newClientsList.add(updatedClient);
		fund.setClients(newClientsList);
		this.fundRepository.save(fund);
		
		EntityModel<Client> entityModel = this.assembler.toModel(updatedClient);
		
		return ResponseEntity //
				  .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
			      .body(entityModel);
	}
	
	@GetMapping("/clients/{id}")
	EntityModel<Client> getClient(@PathVariable Long id) {
	    Client client = this.clientRepository.findById(id)
	      .orElseThrow(() -> new ClientNotFoundException(id));
	    
	    return this.assembler.toModel(client);
	}
	
	@PutMapping("/clients/{id}")
	ResponseEntity<?> modifyClient(@RequestBody Client newClient, @PathVariable Long id) {
		Client client = this.clientRepository.findById(id)
			      .orElseThrow(() -> new ClientNotFoundException(id));
		
		client.setName(newClient.getName());
		client.setScore(newClient.getScore());
		client.setFund(newClient.getFund());
		Client updatedClient = this.clientRepository.save(client);
		
		EntityModel<Client> entityModel = this.assembler.toModel(updatedClient);

		return ResponseEntity //
			.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
			.body(entityModel);
	}
	
	@DeleteMapping("/clients/{id}")
	ResponseEntity<?> deleteClient(@PathVariable Long id) {
		this.clientRepository.deleteById(id);
	    
	    return ResponseEntity.noContent().build();
	}

}
