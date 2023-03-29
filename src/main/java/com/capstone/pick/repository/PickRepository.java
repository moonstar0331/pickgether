package com.capstone.pick.repository;

import com.capstone.pick.domain.Pick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickRepository extends JpaRepository<Pick, Long> {
}
