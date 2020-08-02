package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.support.TagSupporter;
import com.epam.esm.pojo.TagPOJO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagList {

    @JsonIgnore
    private final static String NEXT_PAGE_MODEL_PARAM = "next";

    @JsonIgnore
    private final static String PREVIOUS_PAGE_MODEL_PARAM = "previous";

    @JsonIgnore
    private final static String CURRENT_PAGE_MODEL_PARAM = "current";

    private CollectionModel<EntityModel<TagDTO>> tags;

    private TagList() {
    }

    public static class TagListBuilder {

        private final static String NEXT_PAGE_MODEL_PARAM = "next";
        private final static String PREVIOUS_PAGE_MODEL_PARAM = "previous";
        private final static String CURRENT_PAGE_MODEL_PARAM = "current";
        private final List<TagPOJO> tagsPOJO;
        private List<TagDTO> tagsDTO;
        private int tagsCount = 0;
        private int page = 1;
        private int size = 5;
        private CollectionModel<EntityModel<TagDTO>> tags;

        public TagListBuilder(List<TagPOJO> tags) {
            this.tagsPOJO = tags;
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
            this.tagsDTO = TagSupporter.tagPojoListToTagDtoList(this.tagsPOJO);

            this.tags = CollectionModel.of(
                this.tagsDTO
                    .stream()
                    .map(TagDTO::getModel)
                    .collect(Collectors.toList())
            );

            if (this.tagsCount > page * size) {
                int nextPage = page + 1;
                this.tags.add(linkTo(methodOn(TagController.class)
                    .findAll(nextPage, size)).withRel(NEXT_PAGE_MODEL_PARAM));
            }

            this.tags.add(linkTo(methodOn(TagController.class)
                .findAll(page, size)).withRel(CURRENT_PAGE_MODEL_PARAM));

            if (page != 1) {
                int prevPage = page - 1;
                this.tags.add(linkTo(methodOn(TagController.class)
                    .findAll(prevPage, size)).withRel(PREVIOUS_PAGE_MODEL_PARAM));
            }

            return this.tags;
        }
    }
}
