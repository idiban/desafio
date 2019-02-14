package main.java.com.idiban.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.java.com.idiban.domain.User;


@Repository
public interface UserService extends JpaRepository<User, Long> {

}
