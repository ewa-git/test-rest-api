package pl.juniorjavaproject.testrestapi.services;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.model.User;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;
import pl.juniorjavaproject.testrestapi.exceptions.UserIdNotPresentException;
import pl.juniorjavaproject.testrestapi.mapper.TweetMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private UserService userService;

    private Tweet tweet1;
    private Tweet tweet2;
    private TweetDTO tweetDTO1;
    private TweetDTO tweetDTO2;
    private List<Tweet> tweetList;
    private List<TweetDTO> tweetDTOList;
    private TweetService tweetService;
    private User user1;
    private UserDTO userDTO1;

    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private TweetMapper tweetMapper;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Damian");
        user1.setLastName("Rowiński");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Anna");
        user2.setLastName("Nowak");

        tweet1 = new Tweet();
        tweet1.setId(1L);
        tweet1.setUser(user1);
        tweet1.setTweetText("text1 tweet1");
        tweet1.setTweetTitle("TITLE_1 tweet1");

        tweet2 = new Tweet();
        tweet2.setId(2L);
        tweet2.setUser(user2);
        tweet2.setTweetText("2text2 2tweet2");
        tweet2.setTweetTitle("2_TITLE_2 2_tweet_2");

        tweetDTO1 = modelMapper.map(tweet1, TweetDTO.class);
        userDTO1 = modelMapper.map(user1, UserDTO.class);
        tweetDTO1.setUserDTO(userDTO1);

        tweetDTO2 = modelMapper.map(tweet2, TweetDTO.class);
        UserDTO userDTO2 = modelMapper.map(user2, UserDTO.class);
        tweetDTO2.setUserDTO(userDTO2);

        tweetList = List.of(tweet1, tweet2);
        tweetDTOList = List.of(tweetDTO1, tweetDTO2);

        tweetService = new TweetService(tweetRepository, userService, modelMapper, tweetMapper);
    }

    @Test
    void shouldReturnTweetDtoList() {
        //given
        when(tweetRepository.findAll()).thenReturn(tweetList);

        //when
        List<TweetDTO> returnedTweetDTOList = tweetService.list();

        //then
        SoftAssertions softAssertions = new SoftAssertions();
        for (int i = 0; i < returnedTweetDTOList.size(); i++) {
            TweetDTO currentTweetDTO = returnedTweetDTOList.get(i);
            TweetDTO compareTweetDTO = tweetDTOList.get(i);
            softAssertions.assertThat(currentTweetDTO.getUserDTO()).isEqualTo(compareTweetDTO.getUserDTO());
            softAssertions.assertThat(currentTweetDTO.getId()).isEqualTo(compareTweetDTO.getId());
            softAssertions.assertThat(currentTweetDTO.getTweetText()).isEqualTo(compareTweetDTO.getTweetText());
            softAssertions.assertThat(currentTweetDTO.getTweetTitle()).isEqualTo(compareTweetDTO.getTweetTitle());
        }
        softAssertions.assertAll();
    }

    @Test
    void givenTweetDtoShouldReturnSavedTweetId() throws UserIdNotPresentException {
        //given
        TweetDTO tweetDtoNoId = new TweetDTO();
        tweetDtoNoId.setUserDTO(userDTO1);

        when(userService.findUserById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user1));
        when(tweetRepository.save(ArgumentMatchers.any(Tweet.class))).thenReturn(tweet1);

        //when
        Long tweetReturnedId = tweetService.create(tweetDtoNoId);

        //then
        assertThat(tweetReturnedId).isEqualTo(tweet1.getId());
    }

    @Test
    void givenTweetDtoShouldSaveTweetWithTheSameFields() throws UserIdNotPresentException {
        //given
        when(userService.findUserById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user1));
        when(tweetRepository.save(ArgumentMatchers.any(Tweet.class))).thenReturn(tweet1);

        //when
        tweetService.create(tweetDTO1);
        ArgumentCaptor<Tweet> argumentCaptor = ArgumentCaptor.forClass(Tweet.class);

        //then
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
        tweetDtoNoUserId.setUserDTO(new UserDTO());
        return List.of(Arguments.of(tweetDtoNoUser), Arguments.of(tweetDtoNoUserId));
    }

     // poniższe metody mogą być przygotowane jak dla  shouldThrowExceptionWhenUserDataNotPresent, ale 2 różne metody
//    @Test
//    void givenTweetDtoWithNoUserShouldThrowException()  {
//        //given
//        TweetDTO tweetDtoNoUserId = new TweetDTO();
//
//        assertThrows(UserIdNotPresentException.class, () -> tweetService.create(tweetDtoNoUserId));
//    }
//
//    @Test
//    void givenTweetDtoWithUserIdPresentShouldThrowException()  {
//        //given
//        TweetDTO tweetDtoNoUserId = new TweetDTO();
//        tweetDtoNoUserId.setUserDTO(new UserDTO());
//
//        assertThrows(UserIdNotPresentException.class, () -> tweetService.create(tweetDtoNoUserId));
//    }

    //poniższe może być przydatne do testowania czy jest rzucany wyjątek, gdy jest empty optional
//    when(userService.findUserById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
//    when(tweetRepository.save(ArgumentMatchers.any(Tweet.class))).thenReturn(tweet1);
//
//    //when
//        tweetService.create(tweetNoUser);
//
//    //then
//        Assertions.shouldHaveThrown(ElementNotFoundException.class);


}