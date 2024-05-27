package org.example.mutsaminiproject.service;

import lombok.RequiredArgsConstructor;
import org.example.mutsaminiproject.domain.Board;
import org.example.mutsaminiproject.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    //페이징처리된 게시글 목록
    public Page<Board> findAllBoardsWithPagination (Pageable pageable){
        Pageable sortedByDescCreated = PageRequest.of(pageable.getPageNumber() , pageable.getPageSize() , Sort.by(Sort.Direction.DESC,"createdAt" , "id"));
        return boardRepository.findAll(sortedByDescCreated);
    }

    @Transactional
    public boolean insertBoard(Board board) {

        board.setCreatedAt(LocalDateTime.now());
        Board brd = boardRepository.save(board);
        if(brd.getId()!=null){
            return true;
        }else{
            return false;
        }
    }
    @Transactional(readOnly = true)
    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }
    @Transactional
    public boolean removeBoard(Board board) {
        Board brd=boardRepository.findById(board.getId()).orElse(null);
        if(brd.getPassword()==null) return false;
        if(brd.getPassword().equals(board.getPassword())){
            boardRepository.deleteById(board.getId());
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public boolean modBoard(Board board) {
        String beforePassWord= boardRepository.findById(board.getId()).orElse(null).getPassword();
        if(beforePassWord.equals(board.getPassword())){
            board.setUpdatedAt(LocalDateTime.now());
            boardRepository.save(board);
            return true;
        }else{
            return false;
        }
    }
}
