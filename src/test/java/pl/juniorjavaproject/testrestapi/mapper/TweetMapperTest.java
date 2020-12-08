package pl.juniorjavaproject.testrestapi.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.model.User;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class TweetMapperTest {

    private static Tweet tweet;
    private static TweetDTO tweetDTO;
    private static TweetMapper tweetMapper;
    private static User user;

    @BeforeAll
    static void init() {
        long id = 1L;
        String firstName = "Damian";
        String lastName = "Rowiński";
        String textTweet = "test tweet text";
        String titleTweet = "test title from tweet";

        user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setId(id);

        tweet = new Tweet();
        tweet.setId(id);
        tweet.setUser(user);
        tweet.setTweetText(textTweet);
        tweet.setTweetTitle(titleTweet);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);

        tweetDTO = new TweetDTO();
        tweetDTO.setUser(userDTO);
        tweetDTO.setId(id);
        tweetDTO.setTweetText(textTweet);
        tweetDTO.setTweetTitle(titleTweet);

        tweetMapper = new TweetMapper();
    }

    @Test
    void givenTweetShouldReturnTweetDtoWithDtoFieldsMappedFromTweet() {
        TweetDTO returnedTweetDTO = tweetMapper.from(tweet);

        assertAll(
                () -> assertThat(returnedTweetDTO.getUser()).isEqualTo(tweetDTO.getUser()),
                () -> assertThat(returnedTweetDTO.getId()).isEqualTo(tweetDTO.getId()),
                () -> assertThat(returnedTweetDTO.getTweetText()).isEqualTo(tweetDTO.getTweetText()),
                () -> assertThat(returnedTweetDTO.getTweetTitle()).isEqualTo(tweetDTO.getTweetTitle())
        );
    }

    @Test
    void givenTweetDtoShouldReturnTweetWithFieldsMappedFromDto() {
        Tweet returnedTweet = tweetMapper.from(tweetDTO);

        assertAll(
                () -> assertThat(returnedTweet.getUser()).isEqualTo(tweet.getUser()),
                () -> assertThat(returnedTweet.getId()).isEqualTo(tweet.getId()),
                () -> assertThat(returnedTweet.getTweetText()).isEqualTo(tweet.getTweetText()),
                () -> assertThat(returnedTweet.getTweetTitle()).isEqualTo(tweet.getTweetTitle())
        );
    }
}