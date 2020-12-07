package pl.juniorjavaproject.testrestapi.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class ConstraintsViolationsDTO {
    private List<FieldErrorDTO> fieldErrorList;

    public ConstraintsViolationsDTO () {
        fieldErrorList = new ArrayList<>();
    }

    public void addFieldErrorList(List<FieldError> errorsToAdd) {
        for (FieldError fieldError : errorsToAdd) {
            log.info("Added fieldError to list of FieldErrors: " + fieldError);

            FieldErrorDTO fieldErrorDTO = new FieldErrorDTO();
            fieldErrorDTO.setFieldMessage(fieldError.getDefaultMessage());
            fieldErrorDTO.setFieldName(fieldError.getField());
            String rejectedValue = ObjectUtils.nullSafeToString(fieldError.getRejectedValue());
            fieldErrorDTO.setRejectedValue(rejectedValue);

            fieldErrorList.add(fieldErrorDTO);
        }
    }
}
