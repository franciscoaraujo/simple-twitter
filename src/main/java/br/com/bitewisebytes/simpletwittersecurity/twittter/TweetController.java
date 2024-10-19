package br.com.bitewisebytes.simpletwittersecurity.twittter;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDTO> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return  tweetService.feed(page, pageSize);
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> newTweet(@RequestBody TweetDTO tweetDTO, JwtAuthenticationToken token) {
        return tweetService.newTweet(tweetDTO, token);
    }

    @PostMapping("/tweets/delete/{tweetId}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long tweetId, JwtAuthenticationToken token) {
        return tweetService.deleteTweet(tweetId, token);
    }

    @GetMapping("/tweets/list")
    public ResponseEntity listTweet(JwtAuthenticationToken token) {
        return tweetService.listTweet(token);
    }
}
