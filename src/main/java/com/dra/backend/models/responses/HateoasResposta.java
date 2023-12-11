package com.dra.backend.models.responses;

import org.springframework.hateoas.Links;

import lombok.Data;

@Data
public class HateoasResposta {
    private Object resposta;
    private String error;
    private Links _links;
}
