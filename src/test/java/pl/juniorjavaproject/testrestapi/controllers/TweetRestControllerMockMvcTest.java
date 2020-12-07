package pl.juniorjavaproject.testrestapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.mapper.TweetMapper;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.model.User;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;
import pl.juniorjavaproject.testrestapi.repositories.UserRepository;
import pl.juniorjavaproject.testrestapi.services.TweetService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// testing only web layer wo Spring Context
// web layer for particular controller

@WebMvcTest(TweetRestController.class)
public class TweetRestControllerMockMvcTest {

    private static final String APP_URL = "/api/tweets";
    private static final String APP_URL_WITH_ID_1 = "/api/tweets/1";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private TweetMapper tweetMapper;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TweetRepository tweetRepository;
    @MockBean
    private TweetService tweetService;

    private UserDTO userDTO;

    @BeforeEach
    void init() {
        User user = new User();
        user.setEmail("test@email.pl");
        user.setFirstName("Damian");
        user.setLastName("Rowiński");
        user.setPassword("TEST_PASSWORD");
        user.setId(1L);
        userDTO = new UserDTO();
        userDTO.setId(user.getId());

        when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));

        tweetMapper = new TweetMapper();
    }

    @Test
    void shouldReturnNotFoundStatusWhenListIsEmpty() throws Exception {
        mockMvc.perform(get(APP_URL))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnListWithOneTweet() throws Exception {
        TweetDTO tweet = new TweetDTO();
        tweet.setTweetText("TEST TEXT");
        tweet.setTweetTitle("TITLE TEST");
        tweet.setUser(userDTO);

        List<TweetDTO> tweetList = List.of(tweet);
        String tweetListJson = objectMapper.writeValueAsString(tweetList);

        when(tweetService.list()).thenReturn(tweetList);

        mockMvc.perform(get(APP_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(tweetListJson));
    }

    @Test
    void givenTweetDtoShouldReturnSavedTweetId() throws Exception {
        long id = 1L;
        TweetDTO tweetDTO = new TweetDTO();
        tweetDTO.setTweetText("TEST TEXT");
        tweetDTO.setTweetTitle("TITLE TEST");
        tweetDTO.setUser(userDTO);
        Tweet tweet = tweetMapper.from(tweetDTO);
        tweet.setId(id);

        String tweetJson = objectMapper.writeValueAsString(tweetDTO);

        when(tweetService.create(tweetDTO)).thenReturn(id);
        when(tweetRepository.save(tweet)).thenReturn(tweet);

        mockMvc.perform(post(APP_URL)
                .content(tweetJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", APP_URL + "/" + id));
    }

    @Test
    void givenIdShouldReturnTweetDTO() throws Exception {
        long id = 1L;
        TweetDTO tweetDTO = new TweetDTO();
        tweetDTO.setTweetText("TEST TEXT");
        tweetDTO.setTweetTitle("TITLE TEST");
        tweetDTO.setUser(userDTO);

        String tweetJson = objectMapper.writeValueAsString(tweetDTO);

        when(tweetService.read(id)).thenReturn(tweetDTO);

        mockMvc.perform(get(APP_URL_WITH_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(tweetJson));
    }

    @Test
    void givenIdNotPresentShouldReturn404() throws Exception {
        long id = 1L;
        when(tweetService.read(id)).thenThrow(new ElementNotFoundException("not found tweet"));
        mockMvc.perform(get(APP_URL_WITH_ID_1))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenIdTweetDtoShouldReturnUpdatedTweetDTO() throws Exception {
        long id = 1L;
        TweetDTO tweetDTO = new TweetDTO();
        tweetDTO.setTweetText("TEST TEXT");
        tweetDTO.setTweetTitle("TITLE TEST");
        tweetDTO.setUser(userDTO);
        tweetDTO.setId(id);

        String tweetJson = objectMapper.writeValueAsString(tweetDTO);

        when(tweetService.update(id, tweetDTO)).thenReturn(tweetDTO);

        mockMvc.perform(put(APP_URL_WITH_ID_1)
                .content(tweetJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(tweetJson));
    }

    @Test
    void givenIdShouldDeleteAndReturnNoContent() throws Exception {
        mockMvc.perform(delete(APP_URL_WITH_ID_1))
                .andExpect(status().isNoContent());
    }

}
