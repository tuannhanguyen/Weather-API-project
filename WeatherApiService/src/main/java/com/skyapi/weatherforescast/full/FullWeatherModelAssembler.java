package com.skyapi.weatherforescast.full;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class FullWeatherModelAssembler implements RepresentationModelAssembler<FullWeatherDTO, EntityModel<FullWeatherDTO>> {

	@Override
	public EntityModel<FullWeatherDTO> toModel(FullWeatherDTO dto) {
		EntityModel<FullWeatherDTO> entity = EntityModel.of(dto);
		
		entity.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null))
    			.withSelfRel());
		
		return entity;
	}

}
