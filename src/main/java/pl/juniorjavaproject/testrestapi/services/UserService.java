package pl.juniorjavaproject.testrestapi.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.juniorjavaproject.testrestapi.exceptions.ElementNotFoundException;
import pl.juniorjavaproject.testrestapi.model.User;
import pl.juniorjavaproject.testrestapi.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findUserById(long id) throws ElementNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.orElseThrow(() -> new ElementNotFoundException("Nie znaleziono użytkownika o podanym ID."));
    }
}
