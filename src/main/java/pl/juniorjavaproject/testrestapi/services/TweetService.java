package pl.juniorjavaproject.testrestapi.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.exceptions.UserIdNotPresentException;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;
import pl.juniorjavaproject.testrestapi.mapper.TweetMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@Slf4j
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserService userService;
    private final TweetMapper tweetMapper;

    public TweetService(TweetRepository tweetRepository, UserService userService, TweetMapper tweetMapper) {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
        this.tweetMapper = tweetMapper;
    }

    public List<TweetDTO> list() {
        log.info("Requested for TweetDTO list");
        List<Tweet> tweetList = tweetRepository.findAll();
        List<TweetDTO> tweetDTOSList = new ArrayList<>();
        if (!tweetList.isEmpty()) {
            for (Tweet tweet : tweetList) {
                TweetDTO tweetDTO = tweetMapper.from(tweet);
                tweetDTOSList.add(tweetDTO);
            }
        }
        log.info("Returned list of {}", tweetDTOSList);
        return tweetDTOSList;
    }

    public Long create(TweetDTO tweetDTO) throws UserIdNotPresentException, ElementNotFoundException {
        log.info("Given {} should create and return long id", tweetDTO);
        if (tweetDTO.getUser() == null || tweetDTO.getUser().getId() == null)
            throw new UserIdNotPresentException("Brak podanego id użytkownika.");
        Long id = tweetDTO.getUser().getId();
        Tweet tweet = tweetMapper.from(tweetDTO);
        userService.findUserById(id);
        Tweet savedTweet = tweetRepository.save(tweet);
        log.info("Saved Tweet with id: {}", savedTweet.getId());
        return savedTweet.getId();
    }

    public TweetDTO read(long id) throws ElementNotFoundException {
        log.info("Given id {} should return TweetDTO", id);
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        Tweet tweet = tweetOptional.orElseThrow(() -> new ElementNotFoundException("Nie ma elementu o podanym ID."));
        log.info("Returned DTO for Tweet: {}", tweet);
        return tweetMapper.from(tweet);
    }

    public TweetDTO update(Long id, TweetDTO tweetDTO) throws ElementNotFoundException {
        log.info("Given id: {} and TweetDTO: {}, should update and return TweetDTO", id, tweetDTO);
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        tweetOptional.orElseThrow(() -> new ElementNotFoundException("Nie ma elementu o podanym ID."));
        Tweet tweet = tweetMapper.from(tweetDTO);
        Tweet savedTweet = tweetRepository.save(tweet);
        log.info("Returned DTO for Tweet: {}", savedTweet);
        return tweetMapper.from(savedTweet);
    }

    public void delete(Long id) throws ElementNotFoundException {
        log.info("Given id: {} should delete", id);
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        Tweet tweet = tweetOptional.orElseThrow(
                () -> new ElementNotFoundException("Nie ma elementu o podanym ID."));
        tweetRepository.delete(tweet);
        log.info("Deleted Tweet: {}", tweet);
    }
}
