package br.com.bitewisebytes.simpletwittersecurity.twittter;



import br.com.bitewisebytes.simpletwittersecurity.user.UserDTO;

import java.time.Instant;

public record TweetDTO(Long id ,String tweetText, Instant creationTimestamp) {
    public TweetDTO(Tweet tweet) {
        this(tweet.getTweetId(), tweet.getContent(), tweet.getCreationTimestamp());
    }
}