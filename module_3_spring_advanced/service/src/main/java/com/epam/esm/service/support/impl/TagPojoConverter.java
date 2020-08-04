package com.epam.esm.service.support.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.service.support.PojoConverter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TagPojoConverter implements PojoConverter<TagPOJO, Tag> {

    @Override
    public List<TagPOJO> convert(List<Tag> tags) {
        return tags
                    .stream()
                    .map(TagPOJO::new)
                    .collect(Collectors.toList());
    }

    @Override
    public Tag convert(TagPOJO tag) {
        return new Tag(tag.getId(), tag.getName());
    }
}
