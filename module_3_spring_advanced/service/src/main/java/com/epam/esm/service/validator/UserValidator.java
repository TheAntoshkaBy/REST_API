package com.epam.esm.service.validator;

import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.pojo.InvalidDataMessage;
import com.epam.esm.pojo.UserPOJO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidator {
    private List<InvalidDataMessage> invalidDataMessageList;

    private void name(String name) {
        final String FIELD = "name";
        final int tag_max_length = 300;
        final int tag_min_length = 3;

        if (name.length() > tag_max_length || name.length() < tag_min_length) {
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.NAME_FIELD));
        }
    }

    private void surname(String surname) {
        final String FIELD = "surname";
        final int tag_max_length = 300;
        final int tag_min_length = 3;

        if (surname.length() > tag_max_length || surname.length() < tag_min_length) {
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.NAME_FIELD));
        }
    }

    private void login(String login) {
        final String FIELD = "login";
        final int tag_max_length = 300;
        final int tag_min_length = 3;

        if (login.length() > tag_max_length || login.length() < tag_min_length) {
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.NAME_FIELD));
        }
    }

    private void password(String password) {
        final String FIELD = "password";
        final int tag_max_length = 300;
        final int tag_min_length = 3;

        if (password.length() > tag_max_length || password.length() < tag_min_length) {
            invalidDataMessageList.add(new InvalidDataMessage(FIELD,
                    ErrorTextMessageConstants.NAME_FIELD));
        }
    }

    public void isCorrectUser(UserPOJO user) {
        invalidDataMessageList = new ArrayList<>();
        name(user.getName());
        surname(user.getSurname());
        login(user.getLogin());
        password(user.getPassword());
        if (!invalidDataMessageList.isEmpty()) {
            throw new ServiceException(invalidDataMessageList);
        }
    }
}
