package pl.juniorjavaproject.testrestapi.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.exceptions.UserIdNotPresentException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

// test with live server and H2 database, repository is not mocked

@SpringBootTest
public class TweetServiceIntegrationTest {

    @Autowired
    TweetService tweetService;

    @Test
    void givenTweetDtoShouldReturnSavedTweetLongId() throws UserIdNotPresentException, ElementNotFoundException {
        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        TweetDTO tweetDTO = TweetDTO.builder()
                .tweetText("TEST TEXT")
                .tweetTitle("TITLE TEST")
                .user(userDTO).build();

        //when
        Long tweetId = tweetService.create(tweetDTO);

        //then
        assertThat(tweetId).isNotNull();
    }

    @Test
    void shouldReturnListOfTweets() throws UserIdNotPresentException, ElementNotFoundException {
        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        TweetDTO tweetDTO = TweetDTO.builder()
                .tweetText("TEST TEXT")
                .tweetTitle("TITLE TEST")
                .user(userDTO).build();

        tweetService.create(tweetDTO);

        //when
        List<TweetDTO> listTweetsDTO = tweetService.list();
        TweetDTO firstTweetDTO = listTweetsDTO.get(0);

        //then
        assertAll(
                () -> assertThat(listTweetsDTO.size()).isEqualTo(1),
                () -> assertThat(firstTweetDTO.getTweetTitle()).isEqualTo(tweetDTO.getTweetTitle()),
                () -> assertThat(firstTweetDTO.getTweetText()).isEqualTo(tweetDTO.getTweetText()),
                () -> assertThat(firstTweetDTO.getUser().getId()).isEqualTo(userDTO.getId())
        );

    }


}
