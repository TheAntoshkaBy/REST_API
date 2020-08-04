package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.support.ControllerParamNames;
import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.pojo.TagPOJO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagList {

    private CollectionModel<EntityModel<TagDTO>> tags;

    private TagList() {
    }

    public static class TagListBuilder {

        private List<TagPOJO> tagsPOJO;
        private List<TagDTO> tagsDTO;
        private DtoConverter<TagDTO, TagPOJO> converter;
        private int tagsCount = 0;
        private int page = ControllerParamNames.DEFAULT_PAGE;
        private int size = ControllerParamNames.DEFAULT_SIZE;
        private CollectionModel<EntityModel<TagDTO>> tags;

        public TagListBuilder(List<TagPOJO> tags,
            DtoConverter<TagDTO, TagPOJO> converter) {
            this.tagsPOJO = tags;
            this.converter = converter;
        }

        public TagListBuilder page(int page) {
            this.page = page;
            return this;
        }

        public TagListBuilder size(int size) {
            this.size = size;
            return this;
        }

        public TagListBuilder resultCount(int resultCount) {
            this.tagsCount = resultCount;
            return this;
        }

        public TagList build() {
            TagList tagList = new TagList();
            CollectionModel<EntityModel<TagDTO>> tagsListModel = buildModelWithPagination();
            tagList.setTags(tagsListModel);
            return tagList;
        }

        private CollectionModel<EntityModel<TagDTO>> buildModelWithPagination() {
            this.tagsDTO = converter.convert(this.tagsPOJO);

            this.tags = CollectionModel.of(
                this.tagsDTO
                    .stream()
                    .map(TagDTO::getModel)
                    .collect(Collectors.toList())
            );

            if (this.tagsCount > page * size) {
                int nextPage = page + 1;
                this.tags.add(linkTo(methodOn(TagController.class)
                    .findAll(nextPage, size)).withRel(
                    ControllerParamNames.NEXT_PAGE_MODEL_PARAM));
            }

            this.tags.add(linkTo(methodOn(TagController.class)
                .findAll(page, size)).withRel(
                    ControllerParamNames.CURRENT_PAGE_MODEL_PARAM));

            if (page != 1) {
                int prevPage = page - 1;
                this.tags.add(linkTo(methodOn(TagController.class)
                    .findAll(prevPage, size)).withRel(
                    ControllerParamNames.PREVIOUS_PAGE_MODEL_PARAM));
            }

            return this.tags;
        }
    }
}
