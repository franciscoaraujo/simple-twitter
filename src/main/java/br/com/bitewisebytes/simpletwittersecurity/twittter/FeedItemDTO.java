package br.com.bitewisebytes.simpletwittersecurity.twittter;

import br.com.bitewisebytes.simpletwittersecurity.user.UserDTO;

import java.time.Instant;

public record FeedItemDTO(long tweetId, String content, String userName, Instant creationTimestamp) {

    public FeedItemDTO(UserDTO userDTO, TweetDTO tweetDTO) {

        this(tweetDTO.id(), tweetDTO.tweetText(), userDTO.userName(), tweetDTO.creationTimestamp());

    }
}
