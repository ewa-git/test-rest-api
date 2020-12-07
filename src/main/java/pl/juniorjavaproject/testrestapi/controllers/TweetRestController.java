package pl.juniorjavaproject.testrestapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.juniorjavaproject.testrestapi.dto.TweetDTO;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.exceptions.UserIdNotPresentException;
import pl.juniorjavaproject.testrestapi.services.TweetService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tweets")
public class TweetRestController {

    private final TweetService tweetService;

    public TweetRestController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping
    public ResponseEntity<List<TweetDTO>> list() {
        List<TweetDTO> tweetDTOList = tweetService.list();
        return !tweetDTOList.isEmpty() ? ResponseEntity.ok(tweetDTOList) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TweetDTO> create(@Valid @RequestBody TweetDTO tweetDTO)
            throws UserIdNotPresentException, ElementNotFoundException {
        return ResponseEntity.created(URI.create("/api/tweets/" + tweetService.create(tweetDTO))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetDTO> read(@PathVariable Long id) throws ElementNotFoundException {
        TweetDTO tweetDTO = tweetService.read(id);
        return ResponseEntity.ok(tweetDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetDTO> update(@PathVariable Long id, @Valid @RequestBody TweetDTO tweetDTO)
            throws ElementNotFoundException {
        TweetDTO updatedTweetDTO = tweetService.update(id, tweetDTO);
        return ResponseEntity.ok(updatedTweetDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws ElementNotFoundException {
        tweetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
