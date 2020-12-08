package pl.juniorjavaproject.testrestapi.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.model.Tweet;

@Component
@Slf4j
public class TweetMapper {

    public TweetDTO from(Tweet tweet) {
        log.info("from({})", tweet);
        ModelMapper modelMapper = new ModelMapper();
        TweetDTO tweetDTO = modelMapper.map(tweet, TweetDTO.class);
        log.info("from({}) = {}", tweet, tweetDTO);
        return tweetDTO;
    }

    public Tweet from(TweetDTO tweetDTO) {
        log.info("from({})", tweetDTO);
        ModelMapper modelMapper = new ModelMapper();
        Tweet tweet = modelMapper.map(tweetDTO, Tweet.class);
        log.info("from({}) = {}", tweetDTO, tweet);
        return tweet;
    }
}
