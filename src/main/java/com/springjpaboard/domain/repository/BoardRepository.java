package com.springjpaboard.domain.repository;

import com.springjpaboard.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
