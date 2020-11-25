package pl.juniorjavaproject.testrestapi.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;

import java.util.List;

@Service
public class TweetManagerService {

    private final TweetService tweetService;


    public TweetManagerService(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    public ResponseEntity<TweetDTO> read(@PathVariable Long id) throws ElementNotFoundException {
        TweetDTO tweetDTO = tweetService.read(id);
        if (tweetDTO != null) {
            return ResponseEntity.ok(tweetDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<TweetDTO>> list() throws ElementNotFoundException {
        List<TweetDTO> tweetDTOList = tweetService.list();
        if (!tweetDTOList.isEmpty()) {
            return ResponseEntity.ok(tweetDTOList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
