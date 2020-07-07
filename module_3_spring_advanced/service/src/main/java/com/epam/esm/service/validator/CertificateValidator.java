package com.epam.esm.service.validator;

import com.epam.esm.constant.ErrorTextMessageConstants;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.InvalidDataMessage;
import com.epam.esm.exception.certificate.CertificateInvalidParameterDataException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Scope("prototype")
@Component
public class CertificateValidator {

    private List<InvalidDataMessage> invalidDataMessageList;

    public CertificateValidator() {
    }

    private void nameLength(String name) {
        final String FIELD = "name";
        final int name_max_length = 60;
        final int name_min_length = 3;

        if (name.length() > name_max_length || name.length() < name_min_length) {
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.NAME_FIELD));
        }
    }

    private void priceCheck(double price) {
        final String FIELD = "price";
        final int price_min_value = 0;

        if (price < price_min_value) {
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.PRICE_FIELD));
        }
    }

    private void isCorrectModificationDate(Date certificateData) {

        if (certificateData != null) {
            final String FIELD = "modification date";
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.MODIFICATION_DATE_FIELD));
        }
    }

    private void isCorrectCreationDate(Date certificateData) {
        final String FIELD = "creation date";

        if (certificateData != null) {
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.CREATION_DATE_FIELD));
        }
    }

    public void isCorrectCertificateUpdateData(Certificate certificate) {
        invalidDataMessageList = new ArrayList<>();
        nameLength(certificate.getName());
        priceCheck(certificate.getPrice());
        isCorrectModificationDate(certificate.getModification());
        if (!invalidDataMessageList.isEmpty()) {
            throw new CertificateInvalidParameterDataException(invalidDataMessageList);
        }
    }

    public void isCorrectCertificateCreateData(Certificate certificate) {
        invalidDataMessageList = new ArrayList<>();
        nameLength(certificate.getName());
        priceCheck(certificate.getPrice());
        isCorrectCreationDate(certificate.getCreationDate());
        isCorrectModificationDate(certificate.getModification());
        if (!invalidDataMessageList.isEmpty()) {
            throw new CertificateInvalidParameterDataException(invalidDataMessageList);
        }
    }
}
