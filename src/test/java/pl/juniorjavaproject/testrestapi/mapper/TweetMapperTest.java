package pl.juniorjavaproject.testrestapi.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementCanNotBeNull;
import pl.juniorjavaproject.testrestapi.model.Tweet;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@DisplayName("TweetMapper specification")
class TweetMapperTest {

    TweetMapper tweetMapper;
    private Tweet tweetTest;
    private TweetDTO tweetDTOTest;

    @BeforeEach
    public void prepareTest(){
        tweetMapper = new TweetMapper();

        tweetDTOTest = new TweetDTO();
        tweetDTOTest.setId(1L);
        tweetDTOTest.setTweetText("test");
        tweetDTOTest.setTweetTitle("test title");

        tweetTest = new Tweet();
        tweetTest.setId(1L);
        tweetTest.setCreatedOn(LocalDateTime.now());
        tweetTest.setTweetTitle("tweet");
        tweetTest.setTweetText("tweet text");

    }

    @DisplayName("Should map provided entity to DTO")
    @Test
    public void test1(){
        //given

        //when
        TweetDTO tweetDTO = tweetMapper.from(tweetTest);

        //then
        assertAll(
                () -> assertNotNull(tweetDTO),
                () -> assertEquals(tweetTest.getId(), tweetDTO.getId()),
                () -> assertEquals(tweetTest.getTweetText(), tweetDTO.getTweetText()),
                () -> assertEquals(tweetTest.getTweetTitle(), tweetDTO.getTweetTitle()));
    }

    @DisplayName("Should map provided DTO to entity")
    @Test
    public void test2(){
        //given

        //when
        Tweet tweet = tweetMapper.from(tweetDTOTest);

        //then
        assertAll(
                () -> assertNotNull(tweet),
                () -> assertEquals(tweetDTOTest.getId(), tweet.getId()),
                () -> assertEquals(tweetDTOTest.getTweetText(), tweet.getTweetText()),
                () -> assertEquals(tweetDTOTest.getTweetTitle(), tweet.getTweetTitle()));
    }

    @DisplayName("When given object to map is null should throw ElementShoudntBeNullException")
    @Test()
    public void test3(){
        //given
        Tweet nullTweet = null;
        //when

        //then
        assertThatThrownBy(() -> tweetMapper.from(nullTweet)).isInstanceOf(ElementCanNotBeNull.class);
    }

}