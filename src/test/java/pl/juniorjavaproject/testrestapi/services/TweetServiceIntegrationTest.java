package pl.juniorjavaproject.testrestapi.services;

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

// test with live server and H2 database
@SpringBootTest
public class TweetServiceIntegrationTest {

    private UserDTO userDTO;
    private TweetDTO tweetDTO;
    private long userId;

    @Autowired
    private TweetService tweetService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        User user = new User();
        user.setFirstName("Damian");
        user.setLastName("Rowiński");
        user.setPassword("password");
        user.setEmail("damian.rowinski@gmail.com");
        User savedUser = userRepository.save(user);
        userId = savedUser.getId();

        userDTO = new UserDTO();
        userDTO.setId(userId);

        tweetDTO = TweetDTO.builder()
                .tweetText("TEST TEXT")
                .tweetTitle("TITLE TEST")
                .user(userDTO).build();
    }

    @Test
    void givenTweetDtoShouldReturnSavedTweetId() throws UserIdNotPresentException, ElementNotFoundException {
        Long tweetId = tweetService.create(tweetDTO);

        assertThat(tweetId).isNotNull();
    }

    @Test
    void shouldReturnListOfTweets() throws UserIdNotPresentException, ElementNotFoundException {
        //given
        List<TweetDTO> initialList = tweetService.list();
        int expectedListSize = initialList.size() + 1;
        tweetService.create(tweetDTO);

        //when
        List<TweetDTO> listTweetsDTO = tweetService.list();

        //then
        assertAll(
                () -> assertThat(listTweetsDTO).isNotNull(),
                () -> assertThat(listTweetsDTO.size()).isEqualTo(expectedListSize)
        );
    }

    @Test
    void givenIdShouldReturnTweet() throws UserIdNotPresentException, ElementNotFoundException {
        //given
        Long createdTweetId = tweetService.create(tweetDTO);

        //when
        TweetDTO readTweet = tweetService.read(createdTweetId);

        //then
        assertThat(readTweet).isNotNull();
    }

    @Test
    void givenIdAndTweetDtoShouldReturnUpdatedTweet() throws UserIdNotPresentException, ElementNotFoundException {
        //given
        Long savedTweetId = tweetService.create(tweetDTO);
        TweetDTO updateTweetDTO = new TweetDTO();
        updateTweetDTO.setTweetText("Nowy text");
        updateTweetDTO.setTweetTitle("New title");
        updateTweetDTO.setUser(userDTO);

        //when
        TweetDTO updatedTweet = tweetService.update(savedTweetId, updateTweetDTO);

        //then
        assertThat(updatedTweet).isNotNull();
    }

    @Test
    void givenIdShouldDeleteTweet() throws ElementNotFoundException, UserIdNotPresentException {
        //given
        Long createdTweetId = tweetService.create(tweetDTO);
        List<TweetDTO> listWithCreatedTweet = tweetService.list();
        int expectedListSize = listWithCreatedTweet.size() - 1;

        //when
        tweetService.delete(createdTweetId);
        List<TweetDTO> updatedList = tweetService.list();
        int listSizeWithDeletedTweet = updatedList.size();

        //then
        assertThat(listSizeWithDeletedTweet).isEqualTo(expectedListSize);
    }


}
