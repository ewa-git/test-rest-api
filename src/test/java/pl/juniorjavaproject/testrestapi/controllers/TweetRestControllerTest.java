package pl.juniorjavaproject.testrestapi.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.services.TweetManagerService;
import pl.juniorjavaproject.testrestapi.services.TweetService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TweetRestController Specification")
//@WebMvcTest(TweetRestController.class)
@AutoConfigureMockMvc
@SpringBootTest
class TweetRestControllerTest {
    private static final String TWEET_TITLE = "test title";
    private static final String TWEET_TEXT = "test tweet text";
    private static final Long TWEET_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final String USER_FIRSTNAME = "test user firstname";
    private static final String USER_LASTNAME = "test user lastname";
    private static final String BASE_URI = "/api/tweets/";


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TweetService tweetService;

    @MockBean
    TweetManagerService tweetManagerService;


    @Test
    @DisplayName("load context")
    void loadContext() {

    }


    @DisplayName("get method - should return status OK")
    @Test
    public void test1() throws Exception {
        //given

        List<TweetDTO> tweetDTOList = new ArrayList<>();

        Mockito.when(tweetManagerService.list()).thenReturn(ResponseEntity.ok(tweetDTOList));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URI))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("get method - should return list of tweetDTO")
    @Test
    public void test1a() throws Exception {
        //given
        UserDTO userDTO = UserDTO.builder()
                .id(USER_ID)
                .firstName(USER_FIRSTNAME)
                .lastName(USER_LASTNAME)
                .build();

        TweetDTO tweetDTO = TweetDTO.builder()
                .id(TWEET_ID)
                .tweetTitle(TWEET_TITLE)
                .tweetText(TWEET_TEXT)
                .user(userDTO)
                .build();
        List<TweetDTO> tweetDTOList = List.of(tweetDTO);
        Mockito.when(tweetManagerService.list()).thenReturn(ResponseEntity.ok(tweetDTOList));

        //when
        //then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();
        List<TweetDTO> tweetDTOS = objectMapper.readValue(contentAsString, new TypeReference<List<TweetDTO>>() {
        });

        assertAll(
                () -> assertEquals(tweetDTOList.size(), tweetDTOS.size()),
                () -> assertEquals(tweetDTOList.get(0).getId(), tweetDTOS.get(0).getId()),
                () -> assertEquals(tweetDTOList.get(0).getUser(), tweetDTOS.get(0).getUser()),
                () -> assertEquals(tweetDTOList.get(0).getTweetTitle(), tweetDTOS.get(0).getTweetTitle()),
                () -> assertEquals(tweetDTOList.get(0).getTweetText(), tweetDTOS.get(0).getTweetText()));


    }

    @DisplayName(" post method - should save new tweet and return location header")
    @Test
    public void test2() throws Exception {
        //given
        UserDTO userDTO = UserDTO.builder()
                .id(USER_ID)
                .firstName(USER_FIRSTNAME)
                .lastName(USER_LASTNAME)
                .build();

        TweetDTO tweetDTO = TweetDTO.builder()
                .id(TWEET_ID)
                .tweetTitle(TWEET_TITLE)
                .tweetText(TWEET_TEXT)
                .user(userDTO)
                .build();
        String tweet = objectMapper.writeValueAsString(tweetDTO);

        Mockito.when(tweetService.create(tweetDTO)).thenReturn(1L);

/*        Mockito.doAnswer(invocation -> {
            invocation.getArgument(0, TweetDTO.class).setId(1L);
            return 1L;
        }).when(tweetService).create(ArgumentMatchers.any());*/

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
                .post(BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tweet))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, BASE_URI + TWEET_ID));
    }

    @DisplayName(" get method with uri id- should return status OK")
    @Test
    public void test3() throws Exception {
        //given
        Mockito.when(tweetManagerService.read(ArgumentMatchers.anyLong())).thenReturn(ResponseEntity.ok(ArgumentMatchers.any(TweetDTO.class)));

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URI + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @DisplayName(" get method with uri id- should return tweet DTO")
    @Test
    public void test3a() throws Exception {
        //given
        UserDTO userDTO = UserDTO.builder()
                .id(USER_ID)
                .firstName(USER_FIRSTNAME)
                .lastName(USER_LASTNAME)
                .build();

        TweetDTO tweetDTO = TweetDTO.builder()
                .id(TWEET_ID)
                .tweetTitle(TWEET_TITLE)
                .tweetText(TWEET_TEXT)
                .user(userDTO)
                .build();

        Mockito.when(tweetManagerService.read(ArgumentMatchers.anyLong())).thenReturn(ResponseEntity.ok(tweetDTO));

        //when
        //then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URI + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();
        TweetDTO tweetDTOResult = objectMapper.readValue(contentAsString, TweetDTO.class);

        assertAll(() -> assertEquals(tweetDTO.getId(), tweetDTOResult.getId()),
                () -> assertEquals(tweetDTO.getTweetText(), tweetDTOResult.getTweetText()),
                () -> assertEquals(tweetDTO.getTweetTitle(), tweetDTOResult.getTweetTitle()),
                () -> assertEquals(tweetDTO.getUser(), tweetDTOResult.getUser()));
    }

    @DisplayName("get method when ID is not in database - should return element not found status")
    @Test
    public void test4() throws Exception {
        //given
        Mockito.when(tweetManagerService.read(ArgumentMatchers.anyLong())).thenReturn(ResponseEntity.notFound().build());

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URI + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName(" put method - should return status OK")
    @Test
    public void test5() throws Exception {
        //given
        UserDTO userDTO = UserDTO.builder()
                .id(USER_ID)
                .firstName(USER_FIRSTNAME)
                .lastName(USER_LASTNAME)
                .build();

        TweetDTO tweetDTO = TweetDTO.builder()
                .id(TWEET_ID)
                .tweetTitle(TWEET_TITLE)
                .tweetText(TWEET_TEXT)
                .user(userDTO)
                .build();
        String tweet = objectMapper.writeValueAsString(tweetDTO);

        Mockito.when(tweetService.update(1L, tweetDTO)).thenReturn(tweetDTO);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
                .put(BASE_URI + TWEET_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tweet))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName(" put method - should return updated TweetDTO")
    @Test
    public void test5a() throws Exception {
        //given
        UserDTO userDTO = UserDTO.builder()
                .id(USER_ID)
                .firstName(USER_FIRSTNAME)
                .lastName(USER_LASTNAME)
                .build();

        TweetDTO tweetDTO = TweetDTO.builder()
                .id(TWEET_ID)
                .tweetTitle(TWEET_TITLE)
                .tweetText(TWEET_TEXT)
                .user(userDTO)
                .build();
        String tweet = objectMapper.writeValueAsString(tweetDTO);

        Mockito.when(tweetService.update(1L, tweetDTO)).thenReturn(tweetDTO);
        /*        Mockito.doAnswer(invocation -> {
            invocation.getArgument(0, TweetDTO.class).setTweetText("updated");
            return invocation.getArgument(0);
        }).when(tweetService).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any());*/

        //when
        //then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(BASE_URI + TWEET_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tweet))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();
        TweetDTO tweetDTOResult = objectMapper.readValue(contentAsString, TweetDTO.class);

        assertAll(() -> assertEquals(tweetDTO.getId(), tweetDTOResult.getId()),
                () -> assertEquals(tweetDTO.getTweetText(), tweetDTOResult.getTweetText()),
                () -> assertEquals(tweetDTO.getTweetTitle(), tweetDTOResult.getTweetTitle()),
                () -> assertEquals(tweetDTO.getUser(), tweetDTOResult.getUser()));
    }

    @DisplayName(" put method - when given id is not in database - should return not found status ")
    @Test
    public void test6() throws Exception {
        //given
        UserDTO userDTO = UserDTO.builder()
                .id(USER_ID)
                .firstName(USER_FIRSTNAME)
                .lastName(USER_LASTNAME)
                .build();

        TweetDTO tweetDTO = TweetDTO.builder()
                .id(TWEET_ID)
                .tweetTitle(TWEET_TITLE)
                .tweetText(TWEET_TEXT)
                .user(userDTO)
                .build();
        String tweet = objectMapper.writeValueAsString(tweetDTO);

        Mockito.when(tweetService.update(1L, tweetDTO)).thenThrow(ElementNotFoundException.class);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
                .put(BASE_URI + TWEET_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tweet))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName(" put method - when given given TweetDTO has null user - should return bad request ")
    @Test
    public void test9() throws Exception {
        //given

        TweetDTO tweetDTO = TweetDTO.builder()
                .id(TWEET_ID)
                .tweetTitle(TWEET_TITLE)
                .tweetText(TWEET_TEXT)
                .build();
        String tweet = objectMapper.writeValueAsString(tweetDTO);

        Mockito.when(tweetService.update(1L, tweetDTO)).thenReturn(tweetDTO);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
                .put(BASE_URI + TWEET_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tweet))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @DisplayName("delete method - should return no content")
    @Test
    public void test7() throws Exception {
        //given
        Mockito.doNothing().when(tweetService).delete(ArgumentMatchers.anyLong());

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @DisplayName("delete method - when id is not in database - should return not found status")
    @Test
    public void test8() throws Exception {
        Mockito.doThrow(ElementNotFoundException.class).when(tweetService).delete(ArgumentMatchers.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}