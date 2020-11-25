package pl.juniorjavaproject.testrestapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;
import pl.juniorjavaproject.testrestapi.services.TweetManagerService;
import pl.juniorjavaproject.testrestapi.services.TweetService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TweetRestController Specification")
@WebMvcTest(TweetRestController.class)
class TweetRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TweetService tweetService;

    @MockBean
    TweetManagerService tweetManagerService;


    private TweetDTO tweetDTO1;
    private TweetDTO tweetDTO2;
    private List<TweetDTO> tweetDTOList;

    @BeforeEach
    public void prepareTest() {
        tweetDTO1 = new TweetDTO();
        tweetDTO1.setId(1L);
        tweetDTO1.setTweetText("test1");
        tweetDTO1.setTweetTitle("test1 title");

        tweetDTO2 = new TweetDTO();
        tweetDTO2.setId(2L);
        tweetDTO2.setTweetText("test2");
        tweetDTO2.setTweetTitle("test2 title");

        tweetDTOList = List.of(tweetDTO1, tweetDTO2);
    }

    @Test
    void loadContext() {

    }


    @DisplayName("should return list of tweetDTO")
    @Test
    public void test1() throws Exception {
        //given
        String tweetListJSON = objectMapper.writeValueAsString(tweetDTOList);
        Mockito.when(tweetManagerService.list()).thenReturn(ResponseEntity.ok(tweetDTOList));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/tweets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(tweetListJSON));
    }

    @DisplayName(" - should save new tweet and return location header")
    @Test
    public void test2() throws Exception {
        String tweet = objectMapper.writeValueAsString(tweetDTO1);

/*        Mockito.doAnswer(invocation -> {
            invocation.getArgument(0, TweetDTO.class).setId(1L);
            return 1L;
        }).when(tweetService).create(ArgumentMatchers.any());*/

        Mockito.when(tweetService.create(tweetDTO1)).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/tweets")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tweet))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/api/tweets/1"));

    }

    @DisplayName(" - should return tweet DTO")
    @Test
    public void test3() throws Exception {
        String tweet = objectMapper.writeValueAsString(tweetDTO1);
        Mockito.when(tweetManagerService.read(ArgumentMatchers.anyLong())).thenReturn(ResponseEntity.ok(tweetDTO1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/tweets/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(tweet));
    }

    @DisplayName(" when ID is not in database - should return element not found status")
    @Test
    public void test4() throws Exception {
        Mockito.when(tweetManagerService.read(ArgumentMatchers.anyLong())).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/tweets/9"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DisplayName(" - should return updated TweetDTO")
    @Test
    public void test5(){
//        Mockito.when(tweetManagerService.update)
    }

    @DisplayName(" when given ID is not in database - should throw ElementNotFoundException")
    @Test
    public void test6(){

    }


}