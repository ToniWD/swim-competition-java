package Validators;

public interface Validator<T> {

    void validate(T t) throws ValidatorException;
}
