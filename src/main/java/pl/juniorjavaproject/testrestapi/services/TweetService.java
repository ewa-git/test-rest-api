package pl.juniorjavaproject.testrestapi.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public TweetService(TweetRepository tweetRepository, UserService userService, ModelMapper modelMapper) {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<TweetDTO> getAll() {
        List<Tweet> tweetList = tweetRepository.findAll();
        List<TweetDTO> tweetDTOSList = new ArrayList<>();
        if (!tweetList.isEmpty()) {
            for (Tweet tweet : tweetList) {
//                UserDTO userDTO = modelMapper.map(tweet.getUser(), UserDTO.class);
                TweetDTO tweetDTO = modelMapper.map(tweet, TweetDTO.class);
//                tweetDTO.setUserDTO(userDTO);
                tweetDTOSList.add(tweetDTO);
            }
        }
        return tweetDTOSList;
    }

    public TweetDTO saveTweet(TweetDTO tweetDTO) {
        Tweet tweet = modelMapper.map(tweetDTO, Tweet.class);
//        tweet.setUser(userService.findUserById(tweetDTO.getUserDTO().getId()));
        Tweet savedTweet = tweetRepository.save(tweet);
        return modelMapper.map(savedTweet, TweetDTO.class);
    }

    public TweetDTO findTweetById(long id) {
        Tweet tweet = tweetRepository.findTweetById(id);
        if (tweet != null) {
            TweetDTO tweetDTO = modelMapper.map(tweet, TweetDTO.class);
            tweetDTO.setUserDTO(modelMapper.map(tweet.getUser(), UserDTO.class));
            return tweetDTO;
        } else {
            return null;
        }
    }

    public void editTweet(TweetDTO tweetDTO) {
        Tweet tweet = tweetRepository.findTweetById(tweetDTO.getId());
        if (tweet == null) {
            throw new ElementNotFoundException("Nie ma elementu o podanym ID");
        }
        modelMapper.map(tweetDTO, tweet);
        tweetRepository.save(tweet);
    }

    public void deleteTweet(long id) {
        Tweet tweet = tweetRepository.findTweetById(id);
        if (tweet == null) {
            throw new ElementNotFoundException("Nie ma elementu o podanym ID");
        }
        tweetRepository.delete(tweet);
    }
}
