package pl.juniorjavaproject.testrestapi.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.exceptions.UserIdNotPresentException;
import pl.juniorjavaproject.testrestapi.model.User;
import pl.juniorjavaproject.testrestapi.repositories.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

// test with live server and H2 database, repository is not mocked

@SpringBootTest
public class TweetServiceIntegrationTest {

    private UserDTO userDTO;
    private TweetDTO tweetDTO;
    private static long userId;

    private TweetService tweetService;
    private static UserRepository userRepository;



    @BeforeAll
    static void start() {
        User user = new User();
        user.setFirstName("Damian");
        user.setLastName("Rowiński");
        user.setPassword("password");
        User savedUser = userRepository.save(user);
        userId = savedUser.getId();
    }

    @BeforeEach
    void init() {
        userDTO = new UserDTO();
        userDTO.setId(userId);

        tweetDTO = TweetDTO.builder()
                .tweetText("TEST TEXT")
                .tweetTitle("TITLE TEST")
                .user(userDTO).build();
    }

    @Test
    void givenTweetDtoShouldReturnSavedTweetLongId() throws UserIdNotPresentException, ElementNotFoundException {
        Long tweetId = tweetService.create(tweetDTO);

        assertThat(tweetId).isNotNull();
    }

    @Test
    void shouldReturnListOfTweets() throws UserIdNotPresentException, ElementNotFoundException {
        //given
        List<TweetDTO> initialList = tweetService.list();
        int initialListSize = initialList.size();
        tweetService.create(tweetDTO);

        //when
        List<TweetDTO> listTweetsDTO = tweetService.list();

        //then

        assertAll(
                () -> assertThat(listTweetsDTO).isNotNull(),
                () -> assertThat(listTweetsDTO.size()).isEqualTo(initialListSize + 1)
        );

    }


}
