package com.example.registrationValidators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.util.regex.Pattern;

@FacesValidator("phoneNumberValidator")
public class PhoneNumberValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String phoneNumber = (String) value;
        Pattern pattern = Pattern.compile("\\d{10}");
        if (!pattern.matcher(phoneNumber).matches()) {
            FacesMessage message = new FacesMessage("Invalid phone number format. Please enter a valid 10-digit phone number.");
            throw new ValidatorException(message);
        } else if (phoneNumber.isEmpty() || phoneNumber==null) {
            FacesMessage message = new FacesMessage("Phone number should not be empty.");
            throw new ValidatorException(message);
        }
    }
}
