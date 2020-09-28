package com.rfang.springboot.rest.jpawithhibernateandh2.score;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

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

import com.rfang.springboot.rest.jpawithhibernateandh2.client.Client;
import com.rfang.springboot.rest.jpawithhibernateandh2.client.ClientNotFoundException;
import com.rfang.springboot.rest.jpawithhibernateandh2.client.ClientRepository;

@RestController
public class ScoreController {
	
	private final ScoreRepository scoreRepository;
	
	private final ClientRepository clientRepository;
	
	private final ScoreModelAssembler assembler;
	
	ScoreController(ScoreRepository scoreRepository, ClientRepository clientRepository, ScoreModelAssembler assembler) {
		this.scoreRepository = scoreRepository;
		this.clientRepository = clientRepository;
		this.assembler = assembler;
	}
	
	@GetMapping("/scores")
	CollectionModel<EntityModel<Score>> getAllScores() {
		List<EntityModel<Score>> scores = this.scoreRepository.findAll().stream() //
			      .map(this.assembler::toModel) //
			      .collect(Collectors.toList());

		return CollectionModel.of(scores, linkTo(methodOn(ScoreController.class).getAllScores()).withSelfRel());
	}
		
	@PostMapping("/scores/{id}")
	ResponseEntity<?> createScoreByClientId(@RequestBody Score newScore, @PathVariable Long id) {
		Client client = this.clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
		
		newScore.setClient(client);
		Score updatedScore = this.scoreRepository.save(newScore);
		
		client.setScore(updatedScore);
		this.clientRepository.save(client);
		
		EntityModel<Score> entityModel = this.assembler.toModel(updatedScore);
		
		return ResponseEntity //
				  .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
			      .body(entityModel);
	}
	
	@GetMapping("/scores/{id}")
	EntityModel<Score> getScore(@PathVariable Long id) {
	    Score score = this.scoreRepository.findById(id)
	      .orElseThrow(() -> new ScoreNotFoundException(id));
	    
	    return this.assembler.toModel(score);
	}
	
	@PutMapping("/scores/{id}")
	ResponseEntity<?> modifyScore(@RequestBody Score newScore, @PathVariable Long id) {
	    Score updatedScore = this.scoreRepository.findById(id)
	      .map(score -> {
	        score.setName(newScore.getName());
	        score.setValue(newScore.getValue());
	        score.setClient(newScore.getClient());
	        return this.scoreRepository.save(score);
	      })
	      .orElseThrow(() -> new ScoreNotFoundException(id));
	    
	    EntityModel<Score> entityModel = this.assembler.toModel(updatedScore);

		return ResponseEntity //
			.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
			.body(entityModel);
	}
	
	@DeleteMapping("/scores/{id}")
	ResponseEntity<?> deleteScore(@PathVariable Long id) {
	    this.scoreRepository.deleteById(id);
	    
	    return ResponseEntity.noContent().build();
	}

}
