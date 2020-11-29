package pl.juniorjavaproject.testrestapi.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementCanNotBeNull;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.model.User;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@DisplayName("TweetMapper specification")
class TweetMapperTest {

    TweetMapper tweetMapper;
    private Tweet tweetTest;
    private TweetDTO tweetDTOTest;

    @BeforeEach
    public void prepareTest() {
        tweetMapper = new TweetMapper();
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .build();


        tweetDTOTest = TweetDTO.builder()
                .id(1L)
                .tweetTitle("test title")
                .tweetText("test")
                .user(userDTO)
                .build();


        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setPassword("123456");

        tweetTest = new Tweet();
        tweetTest.setId(1L);
        tweetTest.setCreatedOn(LocalDateTime.now());
        tweetTest.setTweetTitle("tweet");
        tweetTest.setTweetText("tweet text");
        tweetTest.setUser(user);

    }

    @DisplayName("Should map provided entity to DTO")
    @Test
    public void test1() {
        //given

        //when
        TweetDTO tweetDTO = tweetMapper.from(tweetTest);

        //then
        assertAll(
                () -> assertNotNull(tweetDTO),
                () -> assertEquals(tweetTest.getId(), tweetDTO.getId()),
                () -> assertEquals(tweetTest.getTweetText(), tweetDTO.getTweetText()),
                () -> assertEquals(tweetTest.getTweetTitle(), tweetDTO.getTweetTitle()),
                () -> assertEquals(tweetTest.getUser().getId(), tweetDTO.getUser().getId()),
                () -> assertEquals(tweetTest.getUser().getFirstName(), tweetDTO.getUser().getFirstName()),
                () -> assertEquals(tweetTest.getUser().getLastName(), tweetDTO.getUser().getLastName()));
    }

    @DisplayName("Should map provided DTO to entity")
    @Test
    public void test2() {
        //given

        //when
        Tweet tweet = tweetMapper.from(tweetDTOTest);

        //then
        assertAll(
                () -> assertNotNull(tweet),
                () -> assertEquals(tweetDTOTest.getId(), tweet.getId()),
                () -> assertEquals(tweetDTOTest.getTweetText(), tweet.getTweetText()),
                () -> assertEquals(tweetDTOTest.getTweetTitle(), tweet.getTweetTitle()),
                () -> assertEquals(tweetDTOTest.getUser().getId(), tweet.getUser().getId()),
                () -> assertEquals(tweetDTOTest.getUser().getFirstName(), tweet.getUser().getFirstName()),
                () -> assertEquals(tweetDTOTest.getUser().getLastName(), tweet.getUser().getLastName()));
    }


}