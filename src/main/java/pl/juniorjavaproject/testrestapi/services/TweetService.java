package pl.juniorjavaproject.testrestapi.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.mapper.TweetMapper;
import pl.juniorjavaproject.testrestapi.mapper.UserMapper;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.model.User;
import pl.juniorjavaproject.testrestapi.repositories.TweetRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class TweetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TweetService.class);

    private final TweetRepository tweetRepository;
    private final UserService userService;
    private final TweetMapper tweetMapper;


    public TweetService(TweetRepository tweetRepository, UserService userService,
                        TweetMapper tweetMapper) {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
        this.tweetMapper = tweetMapper;

    }

    public List<TweetDTO> list() {
        List<Tweet> tweetList = tweetRepository.findAll();
        List<TweetDTO> tweetDTOSList = new ArrayList<>();
        if (!tweetList.isEmpty()) {
            for (Tweet tweet : tweetList) {
                LOGGER.info("Tweet from DB {}", tweet);
                TweetDTO tweetDTO = tweetMapper.from(tweet);
                LOGGER.info("Tweet {} mapped for TweetDTO{}", tweet, tweetDTO);
                tweetDTOSList.add(tweetDTO);
            }
        }
        return tweetDTOSList;
    }

    public Long create(TweetDTO tweetDTO) throws ElementNotFoundException {
        LOGGER.info("received TweetDTO to be saved in DB{}", tweetDTO);
        Optional<User> user = userService.findUserById(tweetDTO.getUser().getId());
        user.orElseThrow(() -> new ElementNotFoundException("Użytkownik o podanym id nie istnieje"));
        Tweet tweet = tweetMapper.from(tweetDTO);
        LOGGER.info("saved in DB tweet{} from {}", tweet, tweetDTO);
        tweetRepository.save(tweet);
        return tweet.getId();
    }

    public TweetDTO read(long id) throws ElementNotFoundException {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        Tweet tweet = tweetOptional.orElseThrow(() -> new ElementNotFoundException("Nie ma elementu o podanym ID"));
        LOGGER.info("Tweet from DB {}", tweet);
        TweetDTO tweetDTO = tweetMapper.from(tweet);
        LOGGER.info("TweetDTO {} mapped from Tweet {}", tweetDTO, tweet);
        return tweetDTO;
    }


    public TweetDTO update(Long id, TweetDTO tweetDTO) throws ElementNotFoundException {
        LOGGER.info("received TweetDTO to be updated {} and id {}", tweetDTO, id);
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        tweetOptional.orElseThrow(() -> new ElementNotFoundException("Nie ma elementu o podanym ID"));
        Tweet tweet = tweetMapper.from(tweetDTO);
        Tweet savedTweet = tweetRepository.save(tweet);
        LOGGER.info("updated in DB tweet{} from {}", savedTweet, tweetDTO);
        return tweetMapper.from(savedTweet);
    }

    public void delete(Long id) throws ElementNotFoundException {
        LOGGER.info("received id of tweet to be deleted {}", id);
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        Tweet tweet = tweetOptional.orElseThrow(
                () -> new ElementNotFoundException("Nie ma elementu o podanym ID"));
        LOGGER.info("Tweet from DB to be deleted{}", tweet);
        tweetRepository.delete(tweet);
    }

    public TweetDTO saveTweet(TweetDTO tweetDTO) {
        Tweet tweet = tweetMapper.from(tweetDTO);
//        tweet.setUser(userService.findUserById(tweetDTO.getUserDTO().getId()));
        Tweet savedTweet = tweetRepository.save(tweet);
        return tweetMapper.from(savedTweet);
    }

}
