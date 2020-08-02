package com.epam.esm.controller.support;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.pojo.TagPOJO;
import java.util.List;
import java.util.stream.Collectors;

public class TagSupporter {

    public static final String ERROR_ID = "Id must be null";
    public static final String ERROR_NAME = "Name must be between 2 and 70 characters";
    public static final String ERROR_NAME_NOT_NULL = "Name must be not null!";

    public static List<TagDTO> tagPojoListToTagDtoList(List<TagPOJO> tags) {
        return tags
            .stream()
            .map(TagDTO::new)
            .collect(Collectors.toList());
    }

    public static TagPOJO tagDtoToTagPOJO(TagDTO tag) {
        return new TagPOJO(tag.getId(), tag.getName());
    }
}
