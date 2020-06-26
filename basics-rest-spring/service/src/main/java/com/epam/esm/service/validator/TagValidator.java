package com.epam.esm.service.validator;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.tag.TagInvalidDataException;
import org.springframework.stereotype.Component;

@Component
public class TagValidator {

    public boolean isCorrectTag(Tag tag) {
        return nameLength(tag.getName());
    }

    private boolean nameLength(String name) {
        if (name.length() > 60 || name.length() < 3) {
            throw new TagInvalidDataException("name");
        }
        return true;
    }
}
