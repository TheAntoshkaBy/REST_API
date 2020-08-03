package com.epam.esm.service.validator;

import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.pojo.InvalidDataMessage;
import com.epam.esm.pojo.UserPOJO;
import com.epam.esm.repository.jpa.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private UserRepository repository;

    @Autowired
    public UserValidator(UserRepository repository) {
        this.repository = repository;
    }

    private void checkLoginUnique(String login, List<InvalidDataMessage> invalidDataMessageList) {
        String field = "login";

        if (repository.findByLogin(login) != null) {
            invalidDataMessageList.add(new InvalidDataMessage(field,
                ErrorTextMessageConstants.USER_LOGIN_FIELD_IS_EXIST));
        }
    }

    private void checkEmailUnique(String email, List<InvalidDataMessage> invalidDataMessageList) {
        String field = "email";

        if (repository.findByEmail(email) != null) {
            invalidDataMessageList.add(new InvalidDataMessage(field,
                ErrorTextMessageConstants.USER_LOGIN_FIELD_IS_EXIST));
        }
    }

    public void isCorrectUser(UserPOJO user) {
        List<InvalidDataMessage> invalidDataMessageList = new ArrayList<>();

        checkEmailUnique(user.getEmail(), invalidDataMessageList);
        checkLoginUnique(user.getLogin(), invalidDataMessageList);

        if (!invalidDataMessageList.isEmpty()) {
            throw new ServiceValidationException(invalidDataMessageList);
        }
    }
}
