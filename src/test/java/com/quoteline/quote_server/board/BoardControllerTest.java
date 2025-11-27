package com.quoteline.quote_server.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quoteline.quote_server.board.controller.BoardController;
import com.quoteline.quote_server.board.domain.Board;
import com.quoteline.quote_server.board.dto.BoardRequest;
import com.quoteline.quote_server.board.service.BoardService;
import com.quoteline.quote_server.exception.ErrorCode;
import com.quoteline.quote_server.exception.QuoteLineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    private final String BASE_URL = "/boards";
    private final BoardRequest validRequest = new BoardRequest("API 테스트 제목", "내용", "작가");
    private final Board mockBoard = Board.builder()
            .id(1L).title("API 테스트 제목").content("내용").author("작가").build();

    // 1. 게시글 생성 (POST) - 성공
    @Test
    @DisplayName("POST /boards 요청 시 201 Created 응답 및 Location 헤더 반환")
    void createBoard_success() throws Exception {
        // Given: Service가 Board 객체를 반환하도록 설정
        given(boardService.createBoard(any(BoardRequest.class))).willReturn(mockBoard);

        // When & Then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated()) // HTTP 201 응답 검증
                .andExpect(header().string("Location", BASE_URL + "/1")) // Location 헤더 검증
                .andExpect(jsonPath("$.title").value("API 테스트 제목")); // 응답 본문 검증
    }

    // 2. 게시글 생성 (POST) - 실패 (유효성 검사)
    @Test
    @DisplayName("POST /boards 요청 시 필수 필드 누락(Blank) 시 400 Bad Request 응답")
    void createBoard_fail_validation() throws Exception {
        // Given: @NotBlank에 걸리도록 제목을 빈 문자열로 설정
        BoardRequest invalidRequest = new BoardRequest("", "내용", "작가");

        // When & Then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest()); // HTTP 400 응답 검증
    }

    // 3. 단일 게시글 조회 (GET by ID) - 성공
    @Test
    @DisplayName("GET /boards/{id} 요청 시 200 OK 응답")
    void getBoard_success() throws Exception {
        // Given
        given(boardService.getBoard(1L)).willReturn(mockBoard);

        // When & Then
        mockMvc.perform(get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk()) // HTTP 200 응답 검증
                .andExpect(jsonPath("$.id").value(1L));
    }

    // 4. 단일 게시글 조회 (GET by ID) - 실패 (404 Not Found)
    @Test
    @DisplayName("GET /boards/{id} 요청 시 게시글이 없으면 404 Not Found 응답")
    void getBoard_fail_notFound() throws Exception {
        // Given: Service가 BOARD_NOT_FOUND 예외를 던지도록 설정
        given(boardService.getBoard(anyLong())).willThrow(
                new QuoteLineException(ErrorCode.BOARD_NOT_FOUND, "게시물 ID: 999")
        );

        // When & Then
        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound()) // HTTP 404 응답 검증
                .andExpect(jsonPath("$.message").value(ErrorCode.BOARD_NOT_FOUND.getMessage()));
    }

    // 5. 게시글 수정 (PUT)
    @Test
    @DisplayName("PUT /boards/{id} 요청 시 200 OK 응답")
    void updateBoard_success() throws Exception {
        // Given
        BoardRequest updateRequest = new BoardRequest("수정된 제목", "수정된 내용", "수정자");
        Board updatedBoard = Board.builder().id(1L).title("수정된 제목").content("수정된 내용").author("수정자").build();
        given(boardService.updateBoard(anyLong(), any(BoardRequest.class))).willReturn(updatedBoard);

        // When & Then
        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk()) // HTTP 200 응답 검증
                .andExpect(jsonPath("$.title").value("수정된 제목"));
    }

    // 6. 게시글 삭제 (DELETE)
    @Test
    @DisplayName("DELETE /boards/{id} 요청 시 204 No Content 응답")
    void deleteBoard_success() throws Exception {
        // Given: Service의 deleteBoard는 void이므로 doNothing() 설정
        doNothing().when(boardService).deleteBoard(anyLong());

        // When & Then
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent()); // HTTP 204 응답 검증
    }

}
