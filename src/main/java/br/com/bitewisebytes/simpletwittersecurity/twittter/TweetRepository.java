package br.com.bitewisebytes.simpletwittersecurity.twittter;

import br.com.bitewisebytes.simpletwittersecurity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    Optional<List<Tweet>> findAllByUser(User user);
}
