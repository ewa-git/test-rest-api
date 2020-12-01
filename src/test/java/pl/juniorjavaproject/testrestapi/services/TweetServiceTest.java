package pl.juniorjavaproject.testrestapi.services;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
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
import static org.mockito.Mockito.*;

class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private UserService userService;
    @Mock
    private TweetMapper mockTweetMapper;

    private long id1;
    private Tweet tweet1;
    private Tweet tweet2;
    private TweetDTO tweetDTO1;
    private TweetDTO tweetDTO2;
    private List<Tweet> tweetList;
    private List<TweetDTO> tweetDTOList;
    private TweetService tweetService;
    private User user1;
    private UserDTO userDTO1;

    private TweetService tweetServiceMockTweetMapper;

    private ModelMapper modelMapper;
    private TweetMapper tweetMapper;

    {
        modelMapper = new ModelMapper();
        tweetMapper = new TweetMapper();
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

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
        userDTO1 = modelMapper.map(user1, UserDTO.class);
        tweetDTO1.setUserDTO(userDTO1);

        tweetDTO2 = modelMapper.map(tweet2, TweetDTO.class);
        UserDTO userDTO2 = modelMapper.map(user2, UserDTO.class);
        tweetDTO2.setUserDTO(userDTO2);

        tweetList = List.of(tweet1, tweet2);
        tweetDTOList = List.of(tweetDTO1, tweetDTO2);

        tweetService = new TweetService(tweetRepository, userService, modelMapper, tweetMapper);
        tweetServiceMockTweetMapper = new TweetService(tweetRepository, userService, modelMapper, mockTweetMapper);
    }

    @Nested
    class ListMethodTest {
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
        void shouldUseTweetMapper() {
            //given
            when(tweetRepository.findAll()).thenReturn(tweetList);

            //when
            tweetServiceMockTweetMapper.list();

            //then
            verify(mockTweetMapper, Mockito.times(tweetList.size())).from(ArgumentMatchers.any(Tweet.class));
        }
    }

    @Nested
    class CreateMethodTests {
        @Test
        void shouldThrowElementNotFoundExceptionWhenUserNotFound() {
            //given
            when(userService.findUserById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
            when(tweetRepository.save(ArgumentMatchers.any(Tweet.class))).thenReturn(tweet1);

            //when && then
            assertThrows(ElementNotFoundException.class, () -> tweetService.create(tweetDTO1));
        }

        @Test
        void givenTweetDtoShouldReturnSavedTweetId() throws UserIdNotPresentException, ElementNotFoundException {
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
        void givenTweetDtoShouldSaveTweetWithTheSameFields() throws UserIdNotPresentException, ElementNotFoundException {
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
    }

    @ParameterizedTest
    @MethodSource("dataForUserNotPresentExceptions")
    void createShouldThrowExceptionWhenUserDataNotPresent(TweetDTO tweetDTO) {
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
//    @Test
//    void givenTweetDtoWithUserIdPresentShouldThrowException()  {
//        //given
//        TweetDTO tweetDtoNoUserId = new TweetDTO();
//        tweetDtoNoUserId.setUserDTO(new UserDTO());
//
//        assertThrows(UserIdNotPresentException.class, () -> tweetService.create(tweetDtoNoUserId));
//    }

    @Nested
    class ReadMethodTests {
        @Test
        void shouldThrowElementNotFoundException() {
            //given
            when(tweetRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

            //when && then
            assertThrows(ElementNotFoundException.class, () -> tweetService.read(ArgumentMatchers.anyLong()));
        }

        @Test
        void shouldUseTweetMapper() throws ElementNotFoundException {
            //given
            when(tweetRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(tweet1));

            //when
            tweetServiceMockTweetMapper.read(ArgumentMatchers.anyLong());

            //then
            Mockito.verify(mockTweetMapper, Mockito.times(1))
                    .from(ArgumentMatchers.any(Tweet.class));
        }

        @Test
        void givenIdShouldReturnTweetDTO() throws ElementNotFoundException {
            //given
            when(tweetRepository.findById(id1)).thenReturn(Optional.of(tweet1));

            //when
            TweetDTO returnedTweetDTO = tweetService.read(id1);

            //then
            assertAll(
                    () -> assertThat(returnedTweetDTO.getId()).isEqualTo(tweetDTO1.getId()),
                    () -> assertThat(returnedTweetDTO.getTweetText()).isEqualTo(tweetDTO1.getTweetText()),
                    () -> assertThat(returnedTweetDTO.getTweetTitle()).isEqualTo(tweetDTO1.getTweetTitle()),
                    () -> assertThat(returnedTweetDTO.getUserDTO()).isEqualTo(tweetDTO1.getUserDTO())
            );
        }
    }

    @Nested
    class UpdateMethodTests {
        @Test
        void shouldThrowElementNotFoundExceptionWhenIdNotFound() {
            //given
            when(tweetRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

            //
            assertThrows(ElementNotFoundException.class, () -> tweetService.update(id1, tweetDTO1));
        }

        @Test
        void shouldUseTweetMapper() throws ElementNotFoundException {
            //given
            when(tweetRepository.findById(id1)).thenReturn(Optional.of(tweet1));

            //when
            tweetServiceMockTweetMapper.update(id1, tweetDTO1);

            //then
            verify(mockTweetMapper, times(1)).from(tweetDTO1);
        }

//        @Test
//        void givenTweetShouldHaveSameFieldsAsSavedTweet() throws ElementNotFoundException {
//            //given
//            when(tweetRepository.findById(id1)).thenReturn(Optional.of(tweet1));
//            when(tweetRepository.save(ArgumentMatchers.any(Tweet.class))).thenReturn(tweet2);
//            User user2 = modelMapper.map(tweetDTO2.getUserDTO(), User.class);
//
//            //when
//            tweetService.update(id1, tweetDTO2);
//            ArgumentCaptor<Tweet> argumentCaptor = ArgumentCaptor.forClass(Tweet.class);
//
//            //then
//            verify(tweetRepository).save(argumentCaptor.capture());
//            Tweet capturedTweet = argumentCaptor.capture();
//
////            to nie działa
//
//            assertAll(
//                    () -> assertThat(capturedTweet.getId()).isEqualTo(id1),
//                    () -> assertThat(capturedTweet.getTweetText()).isEqualTo(tweetDTO2.getTweetText()),
//                    () -> assertThat(capturedTweet.getTweetTitle()).isEqualTo(tweetDTO2.getTweetTitle()),
//                    () -> assertThat(capturedTweet.getUser()).isEqualTo(user2)
//            );
//
//        }
    }
}