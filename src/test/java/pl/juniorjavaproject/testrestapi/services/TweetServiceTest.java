package pl.juniorjavaproject.testrestapi.services;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.exceptions.UserIdNotPresentException;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.model.User;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;
import pl.juniorjavaproject.testrestapi.mapper.TweetMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private UserService userService;
    @Mock
    private TweetMapper tweetMapper;

    @InjectMocks
    private TweetService tweetService;

    private Tweet tweet1;
    private Tweet tweet2;
    private TweetDTO tweetDTO1;
    private TweetDTO tweetDTO2;
    private List<Tweet> tweetList;
    private List<TweetDTO> tweetDTOList;
    private User user1;
    private long id1;

    private ModelMapper modelMapper;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper();

        id1 = 1L;

        user1 = new User();
        user1.setId(id1);
        user1.setFirstName("Damian");
        user1.setLastName("Rowiński");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Anna");
        user2.setLastName("Nowak");

        tweet1 = new Tweet();
        tweet1.setId(id1);
        tweet1.setUser(user1);
        tweet1.setTweetText("text1 tweet1");
        tweet1.setTweetTitle("TITLE_1 tweet1");

        tweet2 = new Tweet();
        tweet2.setId(2L);
        tweet2.setUser(user2);
        tweet2.setTweetText("2text2 2tweet2");
        tweet2.setTweetTitle("2_TITLE_2 2_tweet_2");

        tweetDTO1 = modelMapper.map(tweet1, TweetDTO.class);
        tweetDTO2 = modelMapper.map(tweet2, TweetDTO.class);

        tweetList = List.of(tweet1, tweet2);
        tweetDTOList = List.of(tweetDTO1, tweetDTO2);
    }

    @Test
    void shouldReturnTweetDtoList() {
        when(tweetRepository.findAll()).thenReturn(tweetList);
        when(tweetMapper.from(ArgumentMatchers.any(Tweet.class))).thenCallRealMethod();

        List<TweetDTO> returnedTweetDTOList = tweetService.list();

        SoftAssertions softAssertions = new SoftAssertions();
        for (int i = 0; i < returnedTweetDTOList.size(); i++) {
            TweetDTO currentTweetDTO = returnedTweetDTOList.get(i);
            TweetDTO compareTweetDTO = tweetDTOList.get(i);
            softAssertions.assertThat(currentTweetDTO.getUser()).isEqualTo(compareTweetDTO.getUser());
            softAssertions.assertThat(currentTweetDTO.getId()).isEqualTo(compareTweetDTO.getId());
            softAssertions.assertThat(currentTweetDTO.getTweetText()).isEqualTo(compareTweetDTO.getTweetText());
            softAssertions.assertThat(currentTweetDTO.getTweetTitle()).isEqualTo(compareTweetDTO.getTweetTitle());
        }
        softAssertions.assertAll();
    }

    @Test
    void givenTweetDtoShouldReturnSavedTweetId() throws UserIdNotPresentException, ElementNotFoundException {
        when(tweetMapper.from(tweetDTO1)).thenReturn(tweet1);
        when(userService.findUserById(ArgumentMatchers.anyLong())).thenReturn(user1);
        when(tweetRepository.save(ArgumentMatchers.any(Tweet.class))).thenReturn(tweet1);

        Long tweetReturnedId = tweetService.create(tweetDTO1);

        assertThat(tweetReturnedId).isEqualTo(tweet1.getId());
    }

    @Test
    void givenTweetDtoShouldSaveTweetWithTheSameFields() throws UserIdNotPresentException, ElementNotFoundException {
        when(tweetMapper.from(tweetDTO1)).thenReturn(tweet1);
        when(userService.findUserById(ArgumentMatchers.anyLong())).thenReturn(user1);
        when(tweetRepository.save(ArgumentMatchers.any(Tweet.class))).thenReturn(tweet1);

        tweetService.create(tweetDTO1);
        ArgumentCaptor<Tweet> argumentCaptor = ArgumentCaptor.forClass(Tweet.class);

        verify(tweetRepository).save(argumentCaptor.capture());
        Tweet savedTweet = argumentCaptor.getValue();

        assertAll(
                () -> assertThat(savedTweet.getTweetTitle()).isEqualTo(tweet1.getTweetTitle()),
                () -> assertThat(savedTweet.getTweetText()).isEqualTo(tweet1.getTweetText()),
                () -> assertThat(savedTweet.getUser()).isEqualTo(tweet1.getUser())
        );
    }

    @ParameterizedTest
    @MethodSource("dataForUserNotPresentExceptions")
    void shouldThrowExceptionWhenUserDataNotPresent(TweetDTO tweetDTO) {
        assertThrows(UserIdNotPresentException.class, () -> tweetService.create(tweetDTO));
    }

    private static List<Arguments> dataForUserNotPresentExceptions() {
        TweetDTO tweetDtoNoUser = new TweetDTO();
        TweetDTO tweetDtoNoUserId = new TweetDTO();
        tweetDtoNoUserId.setUser(new UserDTO());
        return List.of(Arguments.of(tweetDtoNoUser), Arguments.of(tweetDtoNoUserId));
    }

    @Test
    void givenIdShouldReturnTweetDTO() throws ElementNotFoundException {
        when(tweetRepository.findById(id1)).thenReturn(Optional.of(tweet1));
        when(tweetMapper.from(tweet1)).thenReturn(tweetDTO1);

        TweetDTO tweetDTO = tweetService.read(id1);

        assertAll(
                () -> assertThat(tweetDTO.getId()).isEqualTo(tweetDTO1.getId()),
                () -> assertThat(tweetDTO.getTweetText()).isEqualTo(tweetDTO1.getTweetText()),
                () -> assertThat(tweetDTO.getTweetTitle()).isEqualTo(tweetDTO1.getTweetTitle()),
                () -> assertThat(tweetDTO.getUser()).isEqualTo(tweetDTO1.getUser())
        );
    }

    @Test
    void givenIdAndTweetDtoShouldSaveTweetAndReturnTweetDTO() throws ElementNotFoundException {
        when(tweetRepository.findById(id1)).thenReturn(Optional.of(tweet1));
        when(tweetMapper.from(tweetDTO1)).thenReturn(tweet1);
        when(tweetRepository.save(tweet1)).thenReturn(tweet1);
        when(tweetMapper.from(tweet1)).thenReturn(tweetDTO1);

        TweetDTO updatedTweetDTO = tweetService.update(id1, tweetDTO1);

        assertAll(
                () -> assertThat(updatedTweetDTO.getId()).isEqualTo(tweetDTO1.getId()),
                () -> assertThat(updatedTweetDTO.getTweetText()).isEqualTo(tweetDTO1.getTweetText()),
                () -> assertThat(updatedTweetDTO.getTweetTitle()).isEqualTo(tweetDTO1.getTweetTitle()),
                () -> assertThat(updatedTweetDTO.getUser()).isEqualTo(tweetDTO1.getUser())
        );
    }

    @Test
    void givenIdShouldDeleteTweet() throws ElementNotFoundException {
        when(tweetRepository.findById(id1)).thenReturn(Optional.of(tweet1));

        tweetService.delete(id1);
        ArgumentCaptor<Tweet> argumentCaptor = ArgumentCaptor.forClass(Tweet.class);

        verify(tweetRepository).delete(argumentCaptor.capture());
        Tweet deletedTweet = argumentCaptor.getValue();

        assertAll(
                () -> assertThat(deletedTweet.getId()).isEqualTo(tweet1.getId()),
                () -> assertThat(deletedTweet.getTweetTitle()).isEqualTo(tweet1.getTweetTitle()),
                () -> assertThat(deletedTweet.getTweetText()).isEqualTo(tweet1.getTweetText()),
                () -> assertThat(deletedTweet.getUser()).isEqualTo(tweet1.getUser())
        );


    }
}