package vladflore.tech.webquizengine.model.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {
    public void initialize(EmailConstraint constraint) {
    }

    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email.matches("^.+@.+\\.[a-z]{3}$");
    }
}
