package pl.juniorjavaproject.testrestapi.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.mapper.TweetMapper;
import pl.juniorjavaproject.testrestapi.mapper.UserMapper;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserService userService;
    private final TweetMapper tweetMapper;
    private final UserMapper userMapper;

    public TweetService(TweetRepository tweetRepository, UserService userService,
                        TweetMapper tweetMapper, UserMapper userMapper) {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
        this.tweetMapper = tweetMapper;
        this.userMapper = userMapper;
    }

    public List<TweetDTO> list() {
        List<Tweet> tweetList = tweetRepository.findAll();
        List<TweetDTO> tweetDTOSList = new ArrayList<>();
        if (!tweetList.isEmpty()) {
            for (Tweet tweet : tweetList) {
                UserDTO userDTO = userMapper.from(tweet.getUser());
                TweetDTO tweetDTO = tweetMapper.from(tweet);
                tweetDTO.setUserDTO(userDTO);
                tweetDTOSList.add(tweetDTO);
            }
        }
        return tweetDTOSList;
    }

    public Long create(TweetDTO tweetDTO) {
        Tweet tweet = tweetMapper.from(tweetDTO);
        tweet.setUser(userService.findUserById(tweetDTO.getUserDTO().getId()));
        tweetRepository.save(tweet);
        return tweet.getId();
    }

    public TweetDTO read(long id) throws ElementNotFoundException {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        tweetOptional.orElseThrow(() -> new ElementNotFoundException("Nie ma elementu o podanym ID"));
        TweetDTO tweetDTO = tweetMapper.from(tweetOptional.get());
        UserDTO userDTO = userMapper.from(tweetOptional.get().getUser());
        tweetDTO.setUserDTO(userDTO);
        return tweetDTO;
    }

    
    public TweetDTO update(Long id, TweetDTO tweetDTO) throws ElementNotFoundException {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        tweetOptional.orElseThrow(() -> new ElementNotFoundException("Nie ma elementu o podanym ID"));
        Tweet tweet = tweetMapper.from(tweetDTO);
        Tweet savedTweet = tweetRepository.save(tweet);
        return tweetMapper.from(savedTweet);
    }

    public void delete(Long id) throws ElementNotFoundException {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        Tweet tweet = tweetOptional.orElseThrow(
                () -> new ElementNotFoundException("Nie ma elementu o podanym ID"));
        tweetRepository.delete(tweet);
    }
}

