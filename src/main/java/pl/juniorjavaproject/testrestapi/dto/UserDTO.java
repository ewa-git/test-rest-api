package pl.juniorjavaproject.testrestapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;
}
