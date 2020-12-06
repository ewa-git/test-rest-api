package pl.juniorjavaproject.testrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TweetDTO {

    private Long id;

    @NotBlank
    @Size(max = 30)
    private String tweetTitle;

    @NotBlank(message = "Tweet text must be filled")
    private String tweetText;

    @NotNull(message = "User can not be null")
    private UserDTO user;
}
