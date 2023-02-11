package kz.test.tz.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.test.tz.dto.BulletinBoardDTO;
import kz.test.tz.pogo.NewBoardRequest;
import kz.test.tz.security.jwt.SecurityUser;
import kz.test.tz.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "API доска объявлений", description = "api для доски объявлений")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/ads")
public class BoardController {

    private final BoardService boardService;

    @ApiOperation("Создания объявления и размещение его на доске объявлений")
    @PostMapping
    public ResponseEntity<String> addNewBoard(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestBody NewBoardRequest request
    ) {
        boardService.addNewBoard(request, securityUser.getId());
        return ResponseEntity.ok("Ads successfully created");
    }

    @ApiOperation("Добавить изображение по ID обявления")
    @PutMapping("/image/{id}")
    public ResponseEntity<String> addImagesByAbsId(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile multipartFile
    ) {
        boardService.addImagesByAbsId(multipartFile, id);
        return ResponseEntity.ok("Image successfully added");
    }

    @ApiOperation("Получить весь список обявлений")
    @GetMapping
    public ResponseEntity<List<BulletinBoardDTO>> getAllAbs() {
        var boardDTOS = boardService.getAllAbs()
                .stream().map(BulletinBoardDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(boardDTOS);
    }

    @ApiOperation("Фильтр по названию обявлений")
    @GetMapping("/filer")
    public ResponseEntity<List<BulletinBoardDTO>> findByNameContaining(
            @RequestParam("name") String name
    ) {
        var boardDTOS = boardService.findByNameContaining(name)
                .stream().map(BulletinBoardDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(boardDTOS);
    }

    @ApiOperation("Запрос на покупку товара, предложение цены, участие в аукционе")
    @PutMapping("/request")
    public ResponseEntity<BulletinBoardDTO> requestPurchase(
            @RequestParam Long boardId,
            @RequestParam Long minPrice,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        var bulletinBoard = boardService.requestPurchase(boardId, securityUser.getId(), minPrice);
        return ResponseEntity.ok(BulletinBoardDTO.from(bulletinBoard));
    }

}