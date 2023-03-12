package com.capstone.pick.repository;

import com.capstone.pick.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    List<Hashtag> findByContentContaining(String content);
}
