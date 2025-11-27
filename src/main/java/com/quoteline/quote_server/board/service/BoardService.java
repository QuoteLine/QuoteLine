package com.quoteline.quote_server.board.service;

import com.quoteline.quote_server.board.domain.Board;
import com.quoteline.quote_server.board.dto.BoardRequest;
import com.quoteline.quote_server.board.repository.BoardRepository;
import com.quoteline.quote_server.exception.ErrorCode;
import com.quoteline.quote_server.exception.QuoteLineException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board createBoard(BoardRequest request) {
        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .build();
        return boardRepository.save(board);
    }

    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new QuoteLineException(ErrorCode.BOARD_NOT_FOUND, String.format("게시물 ID: %d", id)));
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Board updateBoard(Long id, BoardRequest request) {
        Board board = getBoard(id);
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setAuthor(request.getAuthor());
        return boardRepository.save(board);
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}
