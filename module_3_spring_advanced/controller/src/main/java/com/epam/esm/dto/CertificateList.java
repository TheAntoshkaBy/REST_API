package com.epam.esm.dto;

import com.epam.esm.controller.CertificateController;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateList {

    @JsonIgnore
    private final String nextPageModelParam = "next";

    @JsonIgnore
    private final String previousPageModelParam = "previous";

    @JsonIgnore
    private final String currentPageModelParam = "current";

    private CollectionModel<EntityModel<CertificateDTO>> certificates;

    public CertificateList(List<CertificateDTO> certificateList,
                           int certificatesCount,
                           Map<String, String> params,
                           int page,
                           int size) {
        this.certificates = CollectionModel.of(
                certificateList
                        .stream()
                        .map(CertificateDTO::getModel)
                        .collect(Collectors.toList())
        );

        if (certificatesCount > page * size) {
            int nextPage = page + 1;
            params.put("page", String.valueOf(nextPage));
            this.certificates.add(linkTo(methodOn(CertificateController.class)
                    .find(params, null)).withRel(nextPageModelParam));
        }

        this.certificates.add(linkTo(methodOn(CertificateController.class)
                .find(params, null)).withRel(currentPageModelParam));

        if (page != 1) {
            int prevPage = page - 1;
            params.put("page", String.valueOf(prevPage));
            this.certificates.add(linkTo(methodOn(CertificateController.class)
                    .find(params, null)).withRel(previousPageModelParam));
        }
    }

    public CertificateList(List<CertificateDTO> certificateList,
                           int certificatesCount,
                           int page,
                           int size) {
        this.certificates = CollectionModel.of(
                certificateList
                        .stream()
                        .map(CertificateDTO::getModel)
                        .collect(Collectors.toList())
        );

        if (certificatesCount > page * size) {
            int nextPage = page + 1;
            this.certificates.add(linkTo(methodOn(CertificateController.class)
                    .findAll(nextPage, size)).withRel(nextPageModelParam));
        }

        this.certificates.add(linkTo(methodOn(CertificateController.class)
                .findAll(page, size)).withRel(currentPageModelParam));

        if (page != 1) {
            int prevPage = page - 1;
            this.certificates.add(linkTo(methodOn(CertificateController.class)
                    .findAll(prevPage, size)).withRel(previousPageModelParam));
        }
    }
}
