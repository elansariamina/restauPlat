package com.example.registrationValidators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String password = (String) value;

        if (password.length() < 8 || !password.matches(".*\\d.*")) {
            FacesMessage message = new FacesMessage("Password must be at least 8 characters long and contain at least one digit.");
            throw new ValidatorException(message);
        }
        else    if (password.isEmpty() || password == null) {
            FacesMessage message = new FacesMessage("Password should not be empty.");
            throw new ValidatorException(message);
        }
    }
}
