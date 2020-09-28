package com.rfang.springboot.rest.jpawithhibernateandh2.score;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class ScoreModelAssembler implements RepresentationModelAssembler<Score, EntityModel<Score>> {

  @Override
  public EntityModel<Score> toModel(Score score) {

    return EntityModel.of(score, //
        linkTo(methodOn(ScoreController.class).getScore(score.getId())).withSelfRel(),
        linkTo(methodOn(ScoreController.class).getAllScores()).withRel("scores"));
  }
}
