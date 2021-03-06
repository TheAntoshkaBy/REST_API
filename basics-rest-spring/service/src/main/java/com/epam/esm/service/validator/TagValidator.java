package com.epam.esm.service.validator;

import com.epam.esm.constant.ErrorTextMessageConstants;
import com.epam.esm.entity.InvalidDataMessage;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateInvalidParameterDataException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagValidator {
    private List<InvalidDataMessage> invalidDataMessageList;


    public void isCorrectTag(Tag tag) {
        invalidDataMessageList = new ArrayList<>();
        nameLength(tag.getName());
        if (!invalidDataMessageList.isEmpty()) {
            throw new CertificateInvalidParameterDataException(invalidDataMessageList);
        }
    }

    private void nameLength(String name) {
        final String FIELD = "tag_name";
        final int tag_max_length = 60;
        final int tag_min_length = 3;

        if (name.length() > tag_max_length || name.length() < tag_min_length) {
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.NAME_FIELD));
        }
    }
}
