package com.epam.esm.dto;

import com.epam.esm.controller.CertificateController;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateList {

    private CollectionModel<EntityModel<CertificateDTO>> certificates;

    public CertificateList(List<CertificateDTO> certificateList, int certificatesCount, Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));

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
                    .findComplex(params)).withRel("next"));
        }

        this.certificates.add(linkTo(methodOn(CertificateController.class)
                .findComplex(params)).withRel("current"));

        if (page != 1) {
            int prevPage = page - 1;
            params.put("page", String.valueOf(prevPage));
            this.certificates.add(linkTo(methodOn(CertificateController.class)
                    .findComplex(params)).withRel("previous"));
        }
    }

    public CertificateList(List<CertificateDTO> certificateList, int certificatesCount, int page, int size) {
        this.certificates = CollectionModel.of(
                certificateList
                        .stream()
                        .map(CertificateDTO::getModel)
                        .collect(Collectors.toList())
        );

        if (certificatesCount > page * size) {
            int nextPage = page + 1;
            this.certificates.add(linkTo(methodOn(CertificateController.class)
                    .findAll(nextPage, size, "find")).withRel("next"));
        }

        this.certificates.add(linkTo(methodOn(CertificateController.class)
                .findAll(page, size, "find")).withRel("current"));

        if (page != 1) {
            int prevPage = page - 1;
            this.certificates.add(linkTo(methodOn(CertificateController.class)
                    .findAll(prevPage, size, "find")).withRel("previous"));
        }
    }
}
