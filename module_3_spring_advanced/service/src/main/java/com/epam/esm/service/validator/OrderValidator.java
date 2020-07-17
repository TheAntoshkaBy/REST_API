package com.epam.esm.service.validator;

import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.pojo.InvalidDataMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderValidator {

    private List<InvalidDataMessage> invalidDataMessageList;

    private void descriptionLength(String description) {
        final String FIELD = "description";
        final int tag_max_length = 300;
        final int tag_min_length = 3;

        if (description.length() > tag_max_length || description.length() < tag_min_length) {
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.NAME_FIELD));
        }
    }

    public void isCorrectOrder(CertificateOrderPOJO certificateOrderPOJO) {
        invalidDataMessageList = new ArrayList<>();
        descriptionLength(certificateOrderPOJO.getDescription());
        if (!invalidDataMessageList.isEmpty()) {
            throw new ServiceException(invalidDataMessageList);
        }
    }
}
