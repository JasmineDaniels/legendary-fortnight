package com.cooksys.social_media_demo.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.social_media_demo.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCredentials_Username(String username);

    Optional<User> findByProfile_Email(String email);

}
