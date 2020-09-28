package com.rfang.springboot.rest.jpawithhibernateandh2.client;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class ClientModelAssembler implements RepresentationModelAssembler<Client, EntityModel<Client>> {

  @Override
  public EntityModel<Client> toModel(Client client) {

    return EntityModel.of(client, //
        linkTo(methodOn(ClientController.class).getClient(client.getId())).withSelfRel(),
        linkTo(methodOn(ClientController.class).getAllClients()).withRel("clients"));
  }
}
