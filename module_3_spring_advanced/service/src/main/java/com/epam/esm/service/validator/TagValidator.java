package com.epam.esm.service.validator;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.pojo.InvalidDataMessage;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.repository.jpa.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagValidator {
    final static int TAG_MAX_LENGTH = 60;
    final static int TAG_MIN_LENGTH = 3;
    private List<InvalidDataMessage> invalidDataMessageList;
    private final TagRepository tagRepository;

    @Autowired
    public TagValidator(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public void isCorrectTag(TagPOJO tag) {
        invalidDataMessageList = new ArrayList<>();
        checkNameUnique(tag.getName());
        if (!invalidDataMessageList.isEmpty()) {
            throw new ServiceException(invalidDataMessageList);
        }
    }

    private void checkNameUnique(String name) {
        String field = "Name";
        if (tagRepository.findByName(name) != null) {
            invalidDataMessageList.add(new InvalidDataMessage(field,
                    ErrorTextMessageConstants.TAG_NAME_FIELD_IS_EXIST));
        }
    }
}
