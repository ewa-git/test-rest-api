package pl.juniorjavaproject.testrestapi.services;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.juniorjavaproject.testrestapi.dto.CreateUserDTO;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TweetServiceIntegratedTest {
    private static final String TWEET_TITLE = "test title";
    private static final String TWEET_TEXT = "test tweet text";
    private static final Long TWEET_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final String USER_FIRSTNAME = "test user firstname";
    private static final String USER_LASTNAME = "test user lastname";
    private static final String USER_EMAIL = "test@gmail.com";
    private static final String USER_PASSWORD = "123";

    @Autowired
    TweetService tweetService;
    @Autowired
    UserService userService;

/*
    @BeforeAll
    public static void createUserInDatabase(){
        CreateUserDTO createUserDTO = CreateUserDTO.builder()
                .firstName(USER_FIRSTNAME)
                .lastName(USER_LASTNAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
        userService.create(createUserDTO);
    }
*/


    @DisplayName("given tweetDTO when create method should return tweet id")
    @Test
    public void test1() throws ElementNotFoundException {
        //given
        CreateUserDTO createUserDTO = CreateUserDTO.builder()
                .firstName(USER_FIRSTNAME)
                .lastName(USER_LASTNAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
        userService.create(createUserDTO);

        UserDTO userDTO = UserDTO.builder()
                .id(USER_ID)
                .firstName(USER_FIRSTNAME)
                .lastName(USER_LASTNAME)
                .build();
        TweetDTO tweetDTO = TweetDTO.builder()
                .tweetTitle(TWEET_TITLE)
                .tweetText(TWEET_TEXT)
                .user(userDTO)
                .build();

        // when
        Long tweetId = tweetService.create(tweetDTO);

        // then
        assertNotNull(tweetDTO);
    }

    @DisplayName("list method should return tweetDTO list")
    @Test
    public void test2() throws ElementNotFoundException {

        //when
        List<TweetDTO> tweetDTOList = tweetService.list();

        //then
        assertAll(() -> assertTrue(!tweetDTOList.isEmpty()));
    }

    @DisplayName("given id when read method should return tweetDTO")
    @Test
    public void test3() throws ElementNotFoundException {
        //given
        //when
        TweetDTO tweetDTO = tweetService.read(TWEET_ID);
        //then
        assertAll(() -> assertNotNull(tweetDTO));
    }

    @DisplayName("given id and tweetDTO when update should return updated tweetDTO")
    @Test
    public void test4() throws ElementNotFoundException {
        //given
        UserDTO userDTO = UserDTO.builder()
                .id(USER_ID)
                .firstName(USER_FIRSTNAME)
                .lastName(USER_LASTNAME)
                .build();
        TweetDTO tweetDTO = TweetDTO.builder()
                .id(TWEET_ID)
                .tweetTitle("changed title")
                .tweetText("changed text")
                .user(userDTO)
                .build();

        //when
        TweetDTO updatedTweetDTO = tweetService.update(TWEET_ID, tweetDTO);
        //then
        assertAll(() -> assertNotNull(updatedTweetDTO),
                () -> assertEquals(tweetDTO.getId(), updatedTweetDTO.getId()),
                () -> assertEquals(tweetDTO.getTweetTitle(), updatedTweetDTO.getTweetTitle()),
                () -> assertEquals(tweetDTO.getTweetText(), updatedTweetDTO.getTweetText()),
                () -> assertEquals(tweetDTO.getUser().getId(), updatedTweetDTO.getUser().getId()),
                () -> assertEquals(tweetDTO.getUser().getFirstName(), updatedTweetDTO.getUser().getFirstName()),
                () -> assertEquals(tweetDTO.getUser().getLastName(), updatedTweetDTO.getUser().getLastName()));
    }

    @DisplayName("given id when delete method then should delete tweet")
    @Test
    public void test5() throws ElementNotFoundException {
        //when
        tweetService.delete(TWEET_ID);
        //then

    }

}