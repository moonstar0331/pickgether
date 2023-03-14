package com.capstone.pick.repository;

import com.capstone.pick.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByUserIdContaining(String userId);
}
