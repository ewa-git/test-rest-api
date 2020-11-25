package pl.juniorjavaproject.testrestapi.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.model.User;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserMapper specification")
class UserMapperTest {

    UserMapper userMapper;

    @BeforeEach
    public void prepareTest() {
        userMapper = new UserMapper();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Jan");
        userDTO.setLastName("Kowalski");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setPassword("123456");
    }

}