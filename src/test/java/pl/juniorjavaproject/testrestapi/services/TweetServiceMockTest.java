package pl.juniorjavaproject.testrestapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TweetServiceMockTest {
    private static final String TWEET_TITLE_X_RAY = "X-Ray";
    private static final String TWEET_TEXT_X_RAY = "How to use X-Ray";

    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TweetService tweetService;

    @Test
    void givenTweetDTO_whenServiceSave_thenDtoNotNull() {
        // given
        // mocked test fields and...
        TweetDTO tweetDto = TweetDTO.builder()
                .tweetTitle(TWEET_TITLE_X_RAY)
                .tweetText(TWEET_TEXT_X_RAY)
                .build();
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        Tweet tweet = new Tweet(1L, localDateTimeNow, localDateTimeNow,
                TWEET_TITLE_X_RAY, TWEET_TEXT_X_RAY, null);

        // when
        Mockito.when(tweetService.saveTweet(tweetDto)).thenReturn(tweetDto);
//        Mockito.when(tweetRepository.save(tweet)).thenReturn(tweet);
        TweetDTO savedTweetDto = tweetService.saveTweet(tweetDto);

        // then
        assertAll(
                () -> assertNotNull(savedTweetDto, "Saved TweetDTO is null")
        );
    }

    void givenTweetDTO_whenRepositorySave_thenDtoNotNull() {
        // given
        // mocked test fields and...
        TweetDTO tweetDto = TweetDTO.builder()
                .tweetTitle(TWEET_TITLE_X_RAY)
                .tweetText(TWEET_TEXT_X_RAY)
                .build();
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        Tweet tweet = new Tweet(1L, localDateTimeNow, localDateTimeNow,
                TWEET_TITLE_X_RAY, TWEET_TEXT_X_RAY, null);

        // when
        Mockito.when(tweetService.saveTweet(tweetDto)).thenReturn(tweetDto);
//        Mockito.when(tweetRepository.save(tweet)).thenReturn(tweet);
        TweetDTO savedTweetDto = tweetService.saveTweet(tweetDto);

        // then
        assertAll(
                () -> assertNotNull(savedTweetDto, "Saved TweetDTO is null")
        );
    }
}
