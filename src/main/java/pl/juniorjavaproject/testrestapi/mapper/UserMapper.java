package pl.juniorjavaproject.testrestapi.mapper;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.juniorjavaproject.testrestapi.dto.UserDTO;
import pl.juniorjavaproject.testrestapi.model.User;

@Component
public class UserMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMapper.class);

    public User from(UserDTO userDTO) {
        LOGGER.info("from{}", userDTO);
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDTO, User.class);
        LOGGER.info("from {} = {}", userDTO, user);
        return user;
    }

    public UserDTO from(User user){
        LOGGER.info("from {}", user);
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        LOGGER.info("from {} = {}", user, userDTO);
        return userDTO;
    }
}
