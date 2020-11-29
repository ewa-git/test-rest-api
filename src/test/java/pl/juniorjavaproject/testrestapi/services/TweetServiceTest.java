package pl.juniorjavaproject.testrestapi.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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
class TweetServiceTest {

    private TweetService tweetService;

    //zestaw zależności do zmockowania
    private UserService userService;
    private TweetMapper tweetMapper;
    private TweetRepository tweetRepository;
    private UserMapper userMapper;

    private TweetDTO tweetDTO;
    private Tweet tweet;
    private User user;

    @BeforeEach
    public void prepareTest() {
        userService = Mockito.mock(UserService.class);
        tweetMapper = Mockito.mock(TweetMapper.class);
        userMapper = Mockito.mock(UserMapper.class);
        tweetRepository = Mockito.mock(TweetRepository.class);
        tweetService = new TweetService(tweetRepository, userService, tweetMapper, userMapper);

        tweetDTO = new TweetDTO();
        tweetDTO.setId(1L);
        tweetDTO.setTweetText("test1");
        tweetDTO.setTweetTitle("test1 title");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Jan");
        userDTO.setLastName("Kowalski");

        tweetDTO.setUser(userDTO);

        tweet = new Tweet();
        tweet.setId(1L);
        tweet.setTweetTitle("tweet");
        tweet.setTweetText("tweet text");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setPassword("123456");

        tweet.setUser(user);

    }


    @Nested
    @DisplayName("Read method tests")
    class readMethodTest{

        @DisplayName("should return TweetDTO")
        @Test
        public void test1() throws ElementNotFoundException {

            Mockito.when(tweetRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(tweet));
            TweetDTO tweetMapped = tweetMapper.from(tweet);

            TweetDTO tweetFromMethod = tweetService.read(1L);

            assertEquals(tweetFromMethod, tweetMapped);
        }

    }

}