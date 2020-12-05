package pl.juniorjavaproject.testrestapi.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.mapper.TweetMapper;
import pl.juniorjavaproject.testrestapi.mapper.UserMapper;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.model.User;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TweetService Specification")
@ExtendWith(MockitoExtension.class)
class TweetServiceTest {
    private static final String TWEET_TITLE = "test title";
    private static final String TWEET_TEXT = "test tweet text";
    private static final Long TWEET_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final String USER_FIRSTNAME = "test user firstname";
    private static final String USER_LASTNAME = "test user lastname";

    @InjectMocks
    private TweetService tweetService;

    //zestaw zależności do zmockowania
    @Mock
    private UserService userService;
    @Mock
    private TweetMapper tweetMapper;
    @Mock
    private TweetRepository tweetRepository;

    @Nested
    @DisplayName("Create method tests")
    class createMethodTest{

        @DisplayName(" - should ask for user")
        @Test
        public void test1() throws ElementNotFoundException {
            //given
            UserDTO userDTO = UserDTO.builder()
                    .id(USER_ID)
                    .firstName(USER_FIRSTNAME)
                    .lastName(USER_LASTNAME)
                    .build();

            User user = new User();
            user.setId(1L);
            user.setFirstName("ala");
            user.setLastName("makota");
            user.setEmail("ala.makota@gmail.com");
            user.setPassword("123456");

            TweetDTO tweetDTO = TweetDTO.builder()
                    .id(TWEET_ID)
                    .tweetTitle(TWEET_TITLE)
                    .tweetText(TWEET_TEXT)
                    .user(userDTO)
                    .build();

            Tweet tweet = new Tweet();
            tweet.setUser(user);
            tweet.setTweetText("tweet text");
            tweet.setTweetTitle("tweet title");
            tweet.setCreatedOn(LocalDateTime.now());


            Mockito.when(userService.findUserById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
            Mockito.when(tweetRepository.save(tweet)).thenReturn(tweet);

            //when
            tweetService.create(tweetDTO);

            //then
            Mockito.verify(userService, Mockito.atLeastOnce()).findUserById(tweetDTO.getUser().getId());
        }
    }

    @Nested
    @DisplayName("Read method tests")
    class readMethodTest{

        @DisplayName("should return TweetDTO")
        @Test
        public void test1() throws ElementNotFoundException {

            LocalDateTime localDateTimeNow = LocalDateTime.now();
            Tweet tweet = new Tweet(1L, localDateTimeNow, localDateTimeNow,
                    TWEET_TITLE, TWEET_TEXT, null);

            Mockito.when(tweetRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(tweet));
            TweetDTO tweetMapped = tweetMapper.from(tweet);

            TweetDTO tweetFromMethod = tweetService.read(1L);

            assertEquals(tweetFromMethod, tweetMapped);
        }

        @DisplayName("should find tweet by id")
        @Test
        public void test2() throws ElementNotFoundException {
            //given
            LocalDateTime localDateTimeNow = LocalDateTime.now();
            Tweet tweet = new Tweet(1L, localDateTimeNow, localDateTimeNow,
                    TWEET_TITLE, TWEET_TEXT, null);

            Mockito.when(tweetRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(tweet));

            //when
            tweetService.read(1L);

            //then
            Mockito.verify(tweetRepository, Mockito.atLeastOnce()).findById(1L);
        }

        @DisplayName("should use tweet mapper")
        @Test
        public void test3() throws ElementNotFoundException {
            //given
            LocalDateTime localDateTimeNow = LocalDateTime.now();
            Tweet tweet = new Tweet(1L, localDateTimeNow, localDateTimeNow,
                    TWEET_TITLE, TWEET_TEXT, null);

            Mockito.when(tweetRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(tweet));

            //when
            tweetService.read(1L);

            //then
            Mockito.verify(tweetMapper, Mockito.atLeastOnce()).from(ArgumentMatchers.any(Tweet.class));
        }

        @DisplayName("should throw ElementNotFoundException when given tweet id is not in DB")
        @Test
        public void test4() throws ElementNotFoundException {
            //given

            Mockito.when(tweetRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

            //when
            //then
            assertThrows(ElementNotFoundException.class, () -> tweetService.read(ArgumentMatchers.anyLong()));
        }
    }

    @Nested
    @DisplayName("List method tests")
    class listMethodTest{

    }

}