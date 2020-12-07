package pl.juniorjavaproject.testrestapi.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.juniorjavaproject.testrestapi.dto.ConstraintsViolationsDTO;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestExceptionsHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConstraintsViolationsDTO validationsConstraintsViolations(MethodArgumentNotValidException ex){

        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ConstraintsViolationsDTO constraintsViolationsDTO = new ConstraintsViolationsDTO();
        constraintsViolationsDTO.addFieldErrorList(fieldErrors);

        log.error("Raised MethodArgumentNotValidException with field violations:");
        log.error(constraintsViolationsDTO.toString());

        return constraintsViolationsDTO;
    }

    @ExceptionHandler({ElementNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String elementNotFoundException(ElementNotFoundException ex) {
        return getExceptionMessage(ex);
    }

    @ExceptionHandler({DifferentIdException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String differentIdException(DifferentIdException ex) {
        return getExceptionMessage(ex);
    }

    @ExceptionHandler({UserIdNotPresentException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundById(UserIdNotPresentException ex) {
        return getExceptionMessage(ex);
    }

    private String getExceptionMessage(Exception ex) {
        log.error("Raised exception " + ex.getClass().getSimpleName() + ":");
        log.error(ex.getMessage());
        return ex.getMessage();
    }
}
