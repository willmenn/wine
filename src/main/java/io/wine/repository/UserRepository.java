package io.wine.repository;

import io.wine.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsernameAndAndPassword(String username, String password);
}
