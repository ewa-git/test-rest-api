package pl.juniorjavaproject.testrestapi.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.juniorjavaproject.testrestapi.dto.CreateUserDTO;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.model.User;
import pl.juniorjavaproject.testrestapi.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findUserById(long id) {
        return userRepository.findById(id);
    }

    public void create(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword());

        userRepository.save(user);
    }

}
