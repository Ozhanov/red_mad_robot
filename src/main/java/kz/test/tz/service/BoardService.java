package kz.test.tz.service;

import kz.test.tz.entity.Board;
import kz.test.tz.pogo.NewBoardRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    void addNewBoard(NewBoardRequest request, Long userId);
    void addImagesByAbsId(MultipartFile multipartFile, Long id);

    List<Board> getAllAbs();

    List<Board> findByNameContaining(String name);

    Board requestPurchase(Long boardId, Long userId, Long minPrice);
}