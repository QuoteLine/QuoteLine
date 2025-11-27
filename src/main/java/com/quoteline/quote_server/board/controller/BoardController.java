package com.quoteline.quote_server.board.controller;

import com.quoteline.quote_server.board.domain.Board;
import com.quoteline.quote_server.board.dto.BoardRequest;
import com.quoteline.quote_server.board.dto.BoardResponse;
import com.quoteline.quote_server.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody BoardRequest request) {
        Board board = boardService.createBoard(request);
        URI location = URI.create("/boards/" + board.getId());
        return ResponseEntity.created(location).body(BoardResponse.from(board));
    }

    // 단일 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long id) {
        Board board = boardService.getBoard(id);
        return ResponseEntity.ok(BoardResponse.from(board));
    }

    // 전체 게시글 조회
    @GetMapping
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        List<Board> boards = boardService.getAllBoards();
        return ResponseEntity.ok(BoardResponse.from(boards));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> updateBoard(
            @PathVariable Long id, @Valid @RequestBody BoardRequest request
    ) {
        Board updateBoard = boardService.updateBoard(id, request);
        return ResponseEntity.ok(BoardResponse.from(updateBoard));
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
