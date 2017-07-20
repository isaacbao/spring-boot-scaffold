package io.github.isaacbao.scaffold.domain.entity;

import io.github.isaacbao.scaffold.domain.base.exception.BeanValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 
 * Created by rongyang_lu on 2017/7/10.
 */
public interface Validateable {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    default void validate(Validateable v) throws BeanValidationException {
        Set<ConstraintViolation<Validateable>> constraintViolations = validator.validate(v);
        if (constraintViolations.size() != 0) {
            StringBuilder sb = new StringBuilder();
            constraintViolations.forEach(cons -> sb.append(cons.getPropertyPath()).append(cons.getMessage()).append(','));
            throw new BeanValidationException(sb.toString());
        }
    }

    void validate();
}