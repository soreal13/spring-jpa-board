package com.springjpaboard.controller;

import com.springjpaboard.domain.entity.Board;
import com.springjpaboard.dto.BoardDto;
import com.springjpaboard.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BoardController {

    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

//    @GetMapping("/")
//    public String list(Model model, @RequestParam(value= "page", defaultValue = "1") Integer pageNum) {
//        List<BoardDto> boardDtoList = boardService.getBoardList(pageNum);
//        Integer[] pageList = boardService.getPageList(pageNum);
//
//        model.addAttribute("boardList", boardDtoList);
//        model.addAttribute("pageList", pageList);
//
//        return "board/list.html";
//    }

    @GetMapping("/")
    public String list(@PageableDefault Pageable pageable, Model model) {
        Page<Board> boardList = boardService.getBoardList(pageable);

        model.addAttribute("boardList", boardList);

        return "board/list.html";
    }

    @GetMapping("/post")
    public String write() {
        return "board/write.html";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);

        model.addAttribute("boardDto", boardDto);
        return "board/detail.html";
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);

        model.addAttribute("boardDto", boardDto);
        return "board/update.html";
    }

    @PostMapping("/post/edit/{id}")
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @PostMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);

        return "redirect:/";
    }

    @GetMapping("/board/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);
        model.addAttribute("boardList", boardDtoList);

        return "board/list.html";
    }



}