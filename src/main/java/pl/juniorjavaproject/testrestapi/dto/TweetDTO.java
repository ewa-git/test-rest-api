package pl.juniorjavaproject.testrestapi.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class TweetDTO {

    private Long id;

    @NotBlank
    @Size(max = 30)
    private String tweetTitle;

    @NotBlank
    private String tweetText;

    @NotNull
    private UserDTO userDTO;
}
