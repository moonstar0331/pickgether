package com.capstone.pick.repository;

import com.capstone.pick.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
