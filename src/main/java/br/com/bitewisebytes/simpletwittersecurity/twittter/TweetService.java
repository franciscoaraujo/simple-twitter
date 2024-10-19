package br.com.bitewisebytes.simpletwittersecurity.twittter;

import br.com.bitewisebytes.simpletwittersecurity.role.Role;
import br.com.bitewisebytes.simpletwittersecurity.user.UserDTO;
import br.com.bitewisebytes.simpletwittersecurity.user.UserReppository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserReppository userReppository;

    public TweetService(TweetRepository tweetRepository, UserReppository userReppository) {
        this.tweetRepository = tweetRepository;
        this.userReppository = userReppository;
    }

    public ResponseEntity<FeedDTO> feed(int page, int pageSize) {

      var tweets = tweetRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"));
       tweets.stream().collect(Collectors.toList());
        var feedItemList = tweets.stream()
                .map(tweet -> {
                    var user = tweet.getUser();
                    return new FeedItemDTO(
                            new UserDTO(user.getUserId(),user.getUsername(), user.getEmail()),
                            new TweetDTO(tweet)
                    );
                })
                .toList();
      return ResponseEntity.ok(new FeedDTO(feedItemList, page, pageSize,  tweets.getTotalPages(), tweets.getTotalElements()));
    }

    @Transactional
    public ResponseEntity<Void> newTweet(TweetDTO tweetDTO, JwtAuthenticationToken token) {

        var user = userReppository
                .findById(UUID.fromString(token.getName()))
                .orElseThrow();

        var tweet = new Tweet();
        tweet.setUser(user);
        tweet.setContent(tweetDTO.tweetText());
        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteTweet(Long tweetId, JwtAuthenticationToken token) {

        var tweet = tweetRepository
                .findById(tweetId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var user = userReppository
                .findById(UUID.fromString(token.getName()));

        var isAdmin = user.get().getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if(isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
            tweetRepository.deleteById(tweetId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<List<TweetDTO>> listTweet(JwtAuthenticationToken token) {
        var user = userReppository
                .findById(UUID.fromString(token.getName()))
                .orElseThrow();
        var tweets = tweetRepository.findAllByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var tweetDTOs = tweets.stream()
                .map(tweet -> new TweetDTO(tweet))
                .toList();
        return ResponseEntity.ok(tweetDTOs);
    }
}
