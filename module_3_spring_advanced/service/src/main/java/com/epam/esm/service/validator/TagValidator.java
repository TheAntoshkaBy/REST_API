package com.epam.esm.service.validator;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.pojo.InvalidDataMessage;
import com.epam.esm.pojo.TagPOJO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagValidator {
    private List<InvalidDataMessage> invalidDataMessageList;
    final static int TAG_MAX_LENGTH = 60;
    final static int TAG_MIN_LENGTH = 3;

    public void isCorrectTag(TagPOJO tag) {
        invalidDataMessageList = new ArrayList<>();
        checkName(tag.getName());
        if (!invalidDataMessageList.isEmpty()) {
            throw new ServiceException(invalidDataMessageList);
        }
    }

    private void checkName(String name) {
        final String FIELD = "tag_name";

        if (name.length() > TAG_MAX_LENGTH || name.length() < TAG_MIN_LENGTH) {
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.NAME_FIELD));
        }
    }
}
