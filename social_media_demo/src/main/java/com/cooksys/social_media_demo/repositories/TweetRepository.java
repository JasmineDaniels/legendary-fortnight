package com.cooksys.social_media_demo.repositories;


import java.util.Optional;


import com.cooksys.social_media_demo.entities.Tweet;
import com.cooksys.social_media_demo.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TweetRepository extends JpaRepository<Tweet, Long> {


	Optional<Tweet> findById(Long id);

    List<Tweet> findAllByAuthorAndDeletedFalseOrderByPostedDesc(User author);

    Optional<List<Tweet>> findAllTweetsByHashtagsLabel(String label);


}