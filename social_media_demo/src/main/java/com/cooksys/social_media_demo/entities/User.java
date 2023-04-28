package com.cooksys.social_media_demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;


@Entity
@NoArgsConstructor
@Data
@Table(name="user_table")
public class User {

	@Id
	@GeneratedValue
	private Long id;
	
	@CreationTimestamp
	@Column(nullable=false)
	private Timestamp joined;
	
	@Column(nullable=false)
	private boolean deleted = false;
	
	@Embedded
	private Profile profile;
	
	@Embedded
	private Credentials credentials;
		
	@OneToMany(mappedBy = "author")
	private List<Tweet> tweets;
	
	@ManyToMany
	@JoinTable(  
    		name="user_likes",
    		joinColumns = @JoinColumn(name="user_id"),
    		inverseJoinColumns=@JoinColumn(name="tweet_id")
    		)
	private List<Tweet> likedTweets;
	
	@ManyToMany(mappedBy = "followers")
	private List<User> following;
	
	@ManyToMany
	@JoinTable(name = "followers_following")
	private List<User> followers;
	
	@ManyToMany(mappedBy = "mentionedUsers")
	private List<Tweet> mentionedTweets;
	
}
