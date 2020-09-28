package com.rfang.springboot.rest.jpawithhibernateandh2.fund;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class FundModelAssembler implements RepresentationModelAssembler<Fund, EntityModel<Fund>> {

  @Override
  public EntityModel<Fund> toModel(Fund fund) {

    return EntityModel.of(fund, //
        linkTo(methodOn(FundController.class).getFund(fund.getId())).withSelfRel(),
        linkTo(methodOn(FundController.class).getAllFunds()).withRel("funds"));
  }
}
