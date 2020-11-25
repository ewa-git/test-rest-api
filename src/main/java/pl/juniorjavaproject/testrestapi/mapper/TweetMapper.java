package pl.juniorjavaproject.testrestapi.mapper;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.model.Tweet;
import pl.juniorjavaproject.testrestapi.model.User;

@Component
public class TweetMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TweetMapper.class);

    public TweetDTO from(Tweet tweet) {
        LOGGER.info("from({})", tweet);
        ModelMapper modelMapper = new ModelMapper();
        TweetDTO tweetDTO = modelMapper.map(tweet, TweetDTO.class);
        User userToMap = tweet.getUser();
        UserDTO userDTO = modelMapper.map(userToMap, UserDTO.class);
        tweetDTO.setUserDTO(userDTO);
        LOGGER.info("from({}) = {}", tweet, tweetDTO);
        return tweetDTO;
    }

    public Tweet from(TweetDTO tweetDTO) {
        LOGGER.info("from({})", tweetDTO);
        ModelMapper modelMapper = new ModelMapper();
        Tweet tweet = modelMapper.map(tweetDTO, Tweet.class);
        LOGGER.info("from({}) = {}", tweetDTO, tweet);
        return tweet;
    }
}
