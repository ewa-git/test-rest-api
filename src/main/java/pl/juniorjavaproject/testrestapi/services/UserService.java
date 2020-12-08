package pl.juniorjavaproject.testrestapi.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.model.User;
import pl.juniorjavaproject.testrestapi.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(long id) throws ElementNotFoundException {
        log.info("Given id {} should find User", id);
        Optional<User> optionalUser = userRepository.findById(id);

        optionalUser.ifPresent(user -> log.info("Found user {}", user));
        return optionalUser.orElseThrow(() -> new ElementNotFoundException("Nie znaleziono użytkownika o podanym ID."));
    }
}
