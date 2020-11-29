package pl.juniorjavaproject.testrestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.juniorjavaproject.testrestapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {


}
