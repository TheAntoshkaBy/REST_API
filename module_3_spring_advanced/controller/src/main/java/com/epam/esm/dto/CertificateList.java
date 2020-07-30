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

    private CollectionModel<EntityModel<CertificateDTO>> certificates;

    private CertificateList(){
    }

    public static class CertificateListBuilder {
        private final static String NEXT_PAGE_MODEL_PARAM = "next";
        private final static String PREVIOUS_PAGE_MODEL_PARAM = "previous";
        private final static String CURRENT_PAGE_MODEL_PARAM = "current";
        private List<CertificateDTO> certificatesDTO;
        private int certificatesCount;
        private int page;
        private int size;
        private Map<String, String> params;
        private CollectionModel<EntityModel<CertificateDTO>> certificates;
        private List<TagDTO> tags;

        public CertificateListBuilder(List<CertificateDTO> certificates,
                                      Map<String, String> params, int certificatesCount, int page, int size) {
            this.certificatesCount = certificatesCount;
            this.certificatesDTO = certificates;
            this.params = params;
            this.page = page;
            this.size = size;
        }

        public CertificateListBuilder tags(List<TagDTO> tags) {
            this.tags = tags;
            return this;
        }

        public CertificateList build() {
            CertificateList certificateList = new CertificateList();
            CollectionModel<EntityModel<CertificateDTO>> certificateListModel = buildModel();
            certificateList.setCertificates(certificateListModel);
            return certificateList;
        }

        private CollectionModel<EntityModel<CertificateDTO>> buildModel() {
            this.certificates = CollectionModel.of(
                    certificatesDTO
                            .stream()
                            .map(CertificateDTO::getModel)
                            .collect(Collectors.toList())
            );
            if (certificatesCount > page * size) {
                int nextPage = page + 1;
                params.put("page", String.valueOf(nextPage));
                this.certificates.add(linkTo(methodOn(CertificateController.class)
                        .find(params, this.tags)).withRel(NEXT_PAGE_MODEL_PARAM));
            }

            this.certificates.add(linkTo(methodOn(CertificateController.class)
                    .find(params, this.tags)).withRel(CURRENT_PAGE_MODEL_PARAM));

            if (page != 1) {
                int prevPage = page - 1;
                params.put("page", String.valueOf(prevPage));
                this.certificates.add(linkTo(methodOn(CertificateController.class)
                        .find(params, this.tags)).withRel(PREVIOUS_PAGE_MODEL_PARAM));
            }
            return this.certificates;
        }
    }
}
