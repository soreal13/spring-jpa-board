package com.springjpaboard.service;

import com.springjpaboard.domain.entity.Board;
import com.springjpaboard.domain.repository.BoardRepository;
import com.springjpaboard.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BoardService {

    private BoardRepository boardRepository;

    private static final int BLOCK_PAGE_NUM_COUNT = 3; // 한 블럭에 존재하는 페이지 수
    private static final int PAGE_POST_COUNT = 5; // 한 페이지에 존재하는 게시글 수

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

//    @Transactional
//    public List<BoardDto> getBoardList(Integer pageNum) {
//        Page<Board> page = boardRepository
//                .findAll(PageRequest
//                        .of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate")));
//
//        List<Board> boards = page.getContent();
//        List<BoardDto> boardDtoList = new ArrayList<>();
//
//        for (Board board : boards) {
//            boardDtoList.add(this.convertEntityToDto(board));
//        }
//        return boardDtoList;
//    }

    @Transactional
    public Page<Board> getBoardList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 5);

        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Integer[] getPageList(Integer currentPageNum) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        // 총 게시글 수
        Double postsTotalCount = Double.valueOf(this.getBoardCount());

        // 총 게시글 수를 기준으로 계산한 마지막 페이지 번호 계산
        Integer totalLastPageNum = (int)(Math.ceil(postsTotalCount/PAGE_POST_COUNT)); // 올림 연산

        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > currentPageNum + BLOCK_PAGE_NUM_COUNT)
                ? currentPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;

        // 페이지 시작 번호 조정
        currentPageNum = (currentPageNum <= 3) ? 1 : currentPageNum - 2;

        // 페이지 번호 할당
        for (int val = currentPageNum, i = 0; val <= blockLastPageNum; val++, i++) {
            pageList[i] = val;
        }
        return pageList;
    }

    @Transactional
    public Long getBoardCount() {
        return boardRepository.count();
    }

    @Transactional
    public BoardDto getPost(Long id) {
        Optional<Board> boardWrapper = boardRepository.findById(id);
        Board board = boardWrapper.get();

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createdDate(board.getCreatedDate())
                .build();

        return boardDto;
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public List<BoardDto> searchPosts(String keyword) {
        List<Board> boards = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if (boards.isEmpty()) {
            return boardDtoList;
        }

        for (Board board : boards) {
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;

    }

    private BoardDto convertEntityToDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createdDate(board.getCreatedDate())
                .build();
    }

}
