package pl.juniorjavaproject.testrestapi.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TweetServiceIntegrationTest {
    private static final String TWEET_TITLE_X_RAY = "X-Ray";
    private static final String TWEET_TEXT_X_RAY = "How to use X-Ray";
    private static final int EXPECTED_TWEET_DTOS_LIST_SIZE_1 = 1;

    @Autowired
    private TweetService tweetService;

    @Test
    void givenTweetDTO_whenServiceSave_thenDtoAndIdNotNull() {
        // given
        TweetDTO tweetDto = TweetDTO.builder()
                .tweetTitle(TWEET_TITLE_X_RAY)
                .tweetText(TWEET_TEXT_X_RAY)
                .build();

        // when
        TweetDTO savedTweetDto = tweetService.saveTweet(tweetDto);
        Long savedTweetDtoId = savedTweetDto.getId();

        // then
        assertAll(
                () -> assertNotNull(savedTweetDto, "Saved TweetDTO is null"),
                () -> assertNotNull(savedTweetDtoId, "Saved TweetDTO ID is null")
        );
    }

    @Test
    void givenTweetDTO_whenServiceSaveAndList_thenTweetListSizeEqualsOne() {
        // given
        TweetDTO tweetDto = TweetDTO.builder()
                .tweetTitle(TWEET_TITLE_X_RAY)
                .tweetText(TWEET_TEXT_X_RAY)
                .build();

        // when
        TweetDTO savedTweetDto = tweetService.saveTweet(tweetDto);
        List<TweetDTO> tweetDTOs = tweetService.list();

        // then
        assertAll(
                () -> assertNotNull(tweetDTOs, "TweetDTOs List is null"),
                () -> assertEquals(EXPECTED_TWEET_DTOS_LIST_SIZE_1, tweetDTOs.size(),
                        "TweetDTOs List size is not equals: " + EXPECTED_TWEET_DTOS_LIST_SIZE_1)
        );
    }
}