package com.capstone.pick.repository;

import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.constant.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    void deleteByIdAndUser_UserId(Long voteId, String userId);
    List<Vote> findByCategory(Category category, Sort sort);
    @Query("SELECT v FROM Vote v LEFT JOIN VoteOption vo ON v.id = vo.vote.id LEFT JOIN Pick p on vo.id = p.voteOption.id GROUP BY v.id ORDER BY COUNT(p.id) DESC")
    List<Vote> findAllOrderByPopular();

    List<Vote> findByTitleContaining(String title);

    List<Vote> findByContentContaining(String content);

    List<Vote> findByUser_NicknameContaining(String nickname);
}
