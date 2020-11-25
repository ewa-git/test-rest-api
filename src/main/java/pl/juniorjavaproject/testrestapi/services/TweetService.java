package pl.juniorjavaproject.testrestapi.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.model.User;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.exceptions.UserIdNotPresentException;
import pl.juniorjavaproject.testrestapi.mapper.TweetMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TweetMapper tweetMapper;

    public TweetService(TweetRepository tweetRepository, UserService userService, ModelMapper modelMapper,
                        TweetMapper tweetMapper) {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.tweetMapper = tweetMapper;
    }

    public List<TweetDTO> list() {
        List<Tweet> tweetList = tweetRepository.findAll();
        List<TweetDTO> tweetDTOSList = new ArrayList<>();
        if (!tweetList.isEmpty()) {
            for (Tweet tweet : tweetList) {
                TweetDTO tweetDTO = tweetMapper.from(tweet);
                tweetDTOSList.add(tweetDTO);
            }
        }
        return tweetDTOSList;
    }

    public Long create(TweetDTO tweetDTO) throws UserIdNotPresentException, ElementNotFoundException {
        UserDTO userDTO = tweetDTO.getUserDTO();
        if (userDTO == null || userDTO.getId() == null)
            throw new UserIdNotPresentException("Brak podanego id użytkownika.");
        Optional<User> optionalUser = userService.findUserById(userDTO.getId());
        optionalUser.orElseThrow(() -> new ElementNotFoundException("Brak użytkownika o podanym ID"));
        Tweet tweet = modelMapper.map(tweetDTO, Tweet.class);
        Tweet savedTweet = tweetRepository.save(tweet);
        return savedTweet.getId();
    }

    public TweetDTO read(long id) throws ElementNotFoundException {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        Tweet tweet = optionalTweet
                .orElseThrow(() -> new ElementNotFoundException("Brak tweetu o podanym ID"));
        return tweetMapper.from(tweet);
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
