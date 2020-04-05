package com.postman.calendar.repositories;

import com.postman.calendar.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmailId(String email);
    User findByUserId(String userId);
}
