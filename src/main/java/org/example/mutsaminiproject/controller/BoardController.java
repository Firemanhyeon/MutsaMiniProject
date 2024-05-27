package org.example.mutsaminiproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.mutsaminiproject.domain.Board;
import org.example.mutsaminiproject.service.BoardService;

import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer;

    //리스트페이징
    @GetMapping
    public String boardList(Model model , @RequestParam(defaultValue = "1") int page , @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page -1 , size);
        Page<Board> list = boardService.findAllBoardsWithPagination(pageable);
        model.addAttribute("boardList" , list);
        model.addAttribute("currentPage" , page);
        return "board/list";
    }

    //상세페이지이동
    @GetMapping("/detail")
    public String getBoard(@RequestParam Long id ,@RequestParam int page, Model model){
        model.addAttribute("page" , page);
        model.addAttribute("board", boardService.getBoardById(id));
        return "board/detail";
    }

    //삭제페이지이동
    @GetMapping("/delete/{id}")
    public String deleteBoard(@PathVariable Long id , @RequestParam int page ,Model model){
        Board brd  = new Board();
        brd.setId(id);
        model.addAttribute("page" ,page);
        model.addAttribute("board" ,brd);
        return "board/delete";
    }

    //삭제
    @PostMapping("/remove")
    public String removeBoard(@ModelAttribute Board board , @RequestParam int page ,Model model , RedirectAttributes redirectAttributes){

        if(boardService.removeBoard(board)){
            return "redirect:/board?page="+page;
        }else{
            redirectAttributes.addFlashAttribute("message" , "비밀번호가 틀렸습니다. 다시입력하세요");
            return "redirect:/board/delete/"+board.getId()+"?page="+page;
        }

    }

    //수정페이지이동
    @GetMapping("/update/{id}")
    public String updateBoard(@PathVariable Long id , @RequestParam int page , Model model){
        Board brd = boardService.getBoardById(id);
        //brd.setPassword("");
        model.addAttribute("board" , brd);
        model.addAttribute("page" , page);
        return "board/update";
    }

    //수정
    @PostMapping("/modboard")
    public String modBoard(@ModelAttribute Board board ,@RequestParam int page ,RedirectAttributes redirectAttributes){
        if(boardService.modBoard(board)){
            return "redirect:/board/detail?id="+board.getId()+"&page="+page;
        }else{
            redirectAttributes.addFlashAttribute("message" , "비밀번호가 틀렸습니다. 다시입력하세요");
            return "redirect:/board/update/"+board.getId()+"?page="+page;
        }
    }
    //등록페이지이동
    @GetMapping("/addBoard")
    public String boardAddForm(Model model){
        model.addAttribute("board" , new Board());
        return "board/addForm";
    }
    //등록
    @PostMapping("/addBoard")
    public String boardAdd(@ModelAttribute Board board){

        if(boardService.insertBoard(board)){
            return "redirect:/board";
        }else{
            return "redirect:board/addForm";
        }

    }

}
