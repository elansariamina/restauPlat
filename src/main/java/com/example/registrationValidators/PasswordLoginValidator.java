package com.example.registrationValidators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("passwordLoginValidator")
public class PasswordLoginValidator implements Validator<String> {
    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        if (value == null || value.trim().isEmpty()) {
            FacesMessage message = new FacesMessage("Password is required.");
            throw new ValidatorException(message);
        }
    }
}
