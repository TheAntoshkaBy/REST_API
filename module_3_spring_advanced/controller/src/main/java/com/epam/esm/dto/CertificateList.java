package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.support.ControllerParamNames;
import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.pojo.CertificatePOJO;
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

        private final List<CertificatePOJO> certificatesPOJO;
        private final DtoConverter<CertificateDTO, CertificatePOJO> converter;
        private int certificatesCount = 0;
        private int page = ControllerParamNames.DEFAULT_PAGE;
        private int size = ControllerParamNames.DEFAULT_SIZE;
        private Map<String, String> params;
        private List<TagDTO> tags;

        public CertificateListBuilder(List<CertificatePOJO> certificates,
            DtoConverter<CertificateDTO, CertificatePOJO> converter) {
            this.certificatesPOJO = certificates;
            this.converter = converter;
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
            List<CertificateDTO> certificatesDTO = converter.convert(this.certificatesPOJO);

            CollectionModel<EntityModel<CertificateDTO>> certificates = CollectionModel.of(
                certificatesDTO
                    .stream()
                    .map(CertificateDTO::getModel)
                    .collect(Collectors.toList())
            );

            if (certificatesCount != 0 && params != null) {
                if (certificatesCount > page * size) {
                    int nextPage = page + 1;
                    params.put("page", String.valueOf(nextPage));
                    certificates.add(linkTo(methodOn(CertificateController.class)
                        .find(params, this.tags)).withRel(
                        ControllerParamNames.NEXT_PAGE_MODEL_PARAM));
                }

                params.put("page", String.valueOf(page));
                certificates.add(linkTo(methodOn(CertificateController.class)
                    .find(params, this.tags)).withRel(
                        ControllerParamNames.CURRENT_PAGE_MODEL_PARAM));

                if (page != 1) {
                    int prevPage = page - 1;
                    params.put("page", String.valueOf(prevPage));
                    certificates.add(linkTo(methodOn(CertificateController.class)
                        .find(params, this.tags)).withRel(
                        ControllerParamNames.PREVIOUS_PAGE_MODEL_PARAM));
                }

                int lastPage = certificatesCount/size;
                if(lastPage%size != 0){
                    lastPage+=1;
                }
                params.put("page", String.valueOf(lastPage));
                certificates.add(linkTo(methodOn(CertificateController.class)
                    .find(params, this.tags)).withRel(
                    ControllerParamNames.LAST_PAGE_MODEL_PARAM));
            }
            return certificates;
        }
    }
}
