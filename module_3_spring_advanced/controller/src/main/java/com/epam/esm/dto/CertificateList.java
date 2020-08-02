package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.CertificateController;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class
CertificateList {

    private CollectionModel<EntityModel<CertificateDTO>> certificates;

    private CertificateList() {
    }

    public static class CertificateListBuilder {

        private final static String NEXT_PAGE_MODEL_PARAM = "next";
        private final static String PREVIOUS_PAGE_MODEL_PARAM = "previous";
        private final static String CURRENT_PAGE_MODEL_PARAM = "current";
        private final List<CertificateDTO> certificatesDTO;
        private int certificatesCount = 0;
        private int page = 1;
        private int size = 5;
        private Map<String, String> params;
        private CollectionModel<EntityModel<CertificateDTO>> certificates;
        private List<TagDTO> tags;

        public CertificateListBuilder(List<CertificateDTO> certificates) {
            this.certificatesDTO = certificates;
        }

        public CertificateListBuilder tags(List<TagDTO> tags) {
            this.tags = tags;
            return this;
        }

        public CertificateListBuilder page(int page) {
            this.page = page;
            return this;
        }

        public CertificateListBuilder size(int size) {
            this.size = size;
            return this;
        }

        public CertificateListBuilder resultCount(int resultCount) {
            this.certificatesCount = resultCount;
            return this;
        }

        public CertificateListBuilder parameters(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public CertificateList build() {
            CertificateList certificateList = new CertificateList();
            CollectionModel<EntityModel<CertificateDTO>> certificateListModel =
                buildModelWithPagination();
            certificateList.setCertificates(certificateListModel);
            return certificateList;
        }

        private CollectionModel<EntityModel<CertificateDTO>> buildModelWithPagination() {
            this.certificates = CollectionModel.of(
                certificatesDTO
                    .stream()
                    .map(CertificateDTO::getModel)
                    .collect(Collectors.toList())
            );

            if (certificatesCount != 0 && params != null) {
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
            }
            return this.certificates;
        }
    }
}
