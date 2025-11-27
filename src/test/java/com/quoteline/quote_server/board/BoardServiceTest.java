package com.quoteline.quote_server.board;

import com.quoteline.quote_server.board.domain.Board;
import com.quoteline.quote_server.board.dto.BoardRequest;
import com.quoteline.quote_server.board.repository.BoardRepository;
import com.quoteline.quote_server.board.service.BoardService;
import com.quoteline.quote_server.exception.ErrorCode;
import com.quoteline.quote_server.exception.QuoteLineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    private final BoardRequest mockRequest = new BoardRequest("테스트 제목", "테스트 내용", "테스터");
    private final Board mockBoard = Board.builder()
            .id(1L).title("테스트 제목").content("테스트 내용").author("테스터").build();

    // 1. 게시글 생성
    @Test
    @DisplayName("게시글 생성에 성공한다")
    void 게시글_생성_성공() {
        when(boardRepository.save(any(Board.class))).thenReturn(mockBoard);
        Board createdBoard = boardService.createBoard(mockRequest);
        assertThat(createdBoard.getTitle()).isEqualTo("테스트 제목");
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    // 2. 단일 게시글 조회
    @Test
    @DisplayName("유효한 ID로 게시글 조회에 성공한다")
    void 단일게시글_조회_성공() {
        when(boardRepository.findById(1L)).thenReturn(Optional.of(mockBoard));
        Board foundBoard = boardService.getBoard(1L);
        assertThat(foundBoard.getId()).isEqualTo(1L);
        assertThat(foundBoard.getTitle()).isEqualTo("테스트 제목");
    }

    // 3. 단일 게시글 조회 - 실패 (404)
    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 BOARD_NOT_FOUND 예외가 발생한다")
    void 단일게시글_조회_실패() {
        when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());
        QuoteLineException exception = assertThrows(QuoteLineException.class, () -> boardService.getBoard(999L));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.BOARD_NOT_FOUND);
    }

    // 4. 전체 게시글 조회
    @Test
    @DisplayName("전체 게시글 목록 조회에 성공한다")
    void 전체게시글_조회_성공() {
        List<Board> boards = Arrays.asList(mockBoard, Board.builder().id(2L).title("2번글").build());
        when(boardRepository.findAll()).thenReturn(boards);
        List<Board> foundBoards = boardService.getAllBoards();
        assertThat(foundBoards).hasSize(2);
    }

    // 5. 게시글 수정
    @Test
    @DisplayName("게시글 수정에 성공한다")
    void 게시글_수정_성공() {
        BoardRequest updateRequest = new BoardRequest("수정된 제목", "수정된 내용", "수정자");
        when(boardRepository.findById(1L)).thenReturn(Optional.of(mockBoard));
        when(boardRepository.save(any(Board.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Board updateBoard = boardService.updateBoard(1L, updateRequest);
        assertThat(updateBoard.getTitle()).isEqualTo("수정된 제목");
    }

    // 6. 게시글 삭제
    @Test
    @DisplayName("게시글 삭제에 성공한다")
    void 게시글_삭제_성공() {
        Long idToDelete = 1L;
        boardService.deleteBoard(idToDelete);
        verify(boardRepository, times(1)).deleteById(idToDelete);
    }
}