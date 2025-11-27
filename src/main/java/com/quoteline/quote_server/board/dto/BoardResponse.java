package com.quoteline.quote_server.board.dto;

import com.quoteline.quote_server.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private String author;

    public static BoardResponse from(Board board) {
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getAuthor()
        );
    }

    public static List<BoardResponse> from(List<Board> boards) {
        return boards.stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
    }
}