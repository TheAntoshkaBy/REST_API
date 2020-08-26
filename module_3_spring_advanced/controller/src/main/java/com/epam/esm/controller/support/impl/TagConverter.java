package com.epam.esm.controller.support.impl;

import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.pojo.TagPOJO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TagConverter implements DtoConverter<TagDTO, TagPOJO> {

    @Override
    public List<TagDTO> convert(List<TagPOJO> tags) {
        return tags
            .stream()
            .map(TagDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    public TagPOJO convert(TagDTO tag) {
        return new TagPOJO(tag.getId(), tag.getName());
    }
}
