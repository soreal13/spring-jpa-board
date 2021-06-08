package com.springjpaboard.service;

import com.springjpaboard.domain.repository.BoardRepository;
import com.springjpaboard.dto.BoardDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class BoardService {

    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }


}
