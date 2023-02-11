package kz.test.tz.service.impl;

import kz.test.tz.entity.Board;
import kz.test.tz.exception.RequestException;
import kz.test.tz.exception.NotFoundException;
import kz.test.tz.pogo.NewBoardRequest;
import kz.test.tz.repo.BoardRepository;
import kz.test.tz.repo.UserRepository;
import kz.test.tz.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceIml implements BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    @Transactional
    @Override
    public void addNewBoard(NewBoardRequest request, Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User: %d not found".formatted(userId)));
        var board = new Board();
        board.setName(request.getName());
        board.setMinPrice(request.getMinPrice());
        board.setDescription(request.getDescription());
        board.setUser(user);
        board.setStatus(Board.Status.ACTIVE);
        boardRepository.save(board);
        log.info("Created new board: {}", board);
    }

    @Override
    public void addImagesByAbsId(MultipartFile multipartFile, Long id) {
        try {
            var bulletinBoard = boardRepository.findByIdAndStatus(id, Board.Status.ACTIVE).orElseThrow(() ->
                    new NotFoundException("Abs Id: %d not found".formatted(id)));
            bulletinBoard.setImage(multipartFile.getBytes());
            boardRepository.save(bulletinBoard);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Board> getAllAbs() {
        log.info("Get a list of ads");
        return boardRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Board> findByNameContaining(String name) {
        return boardRepository.findByNameContaining(name);
    }

    @Transactional
    @Override
    public Board requestPurchase(Long boardId, Long userId, Long minPrice) {
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime closedDateTime = dateTime.plusDays(5);
        var bulletinBoard = boardRepository.findByIdAndStatus(boardId, Board.Status.ACTIVE).orElseThrow(() -> new NotFoundException("Bulletin board not found"));
        if (bulletinBoard.getMinPrice() >= minPrice) {
            throw new RequestException("The offered price cannot be lower or equal to the minimum price");
        }
        var board = boardRepository.findById(boardId).orElse(null);
        sendingNotificationToEmail(minPrice, board);
        bulletinBoard.setClientId(userId);
        bulletinBoard.setClosedDateTime(closedDateTime);
        bulletinBoard.setMinPrice(minPrice);
        return boardRepository.save(bulletinBoard);
    }

    private static void sendingNotificationToEmail(Long minPrice, Board board) {
        if (board != null && board.getClosedDateTime() != null) {
            log.warn("Отправка уведомления email на эл.адрес: {}", board.getUser().getEmail());
            log.warn("Уважемый клиент, ваше ценовае предлажение была перебита, на сумму: {}", minPrice);
        }
    }
}
