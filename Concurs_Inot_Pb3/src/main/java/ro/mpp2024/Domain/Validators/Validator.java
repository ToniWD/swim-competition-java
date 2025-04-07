package ro.mpp2024.Domain.Validators;

public interface Validator<T> {

    void validate(T t) throws ValidatorException;
}
