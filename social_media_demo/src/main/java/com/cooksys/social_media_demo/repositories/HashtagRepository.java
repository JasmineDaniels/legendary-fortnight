package com.cooksys.social_media_demo.repositories;

import com.cooksys.social_media_demo.entities.Hashtag;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

	@NonNull List<Hashtag> findAll();

    @Query("select h from Hashtag h where h.label = ?1")
    Optional<Hashtag> findByLabel(String label);

}
