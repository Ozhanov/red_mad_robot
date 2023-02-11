package kz.test.tz.service;

import kz.test.tz.entity.Board;
import kz.test.tz.repo.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardCorn {

    private final BoardRepository boardRepository;

    @Scheduled(cron = "0 00 8 ? * *")
    public void runCronClosedDateTimeExpiredBulletinBoards() {
        log.info("[RUN CRON]: runCronClosedDateTimeExpiredBulletinBoards");
        var bulletinBoards = boardRepository.closedDateTimeExpiredBulletinBoards(LocalDateTime.now());
        bulletinBoards.forEach(bullet -> {
            bullet.setStatus(Board.Status.REMOVED_PUBLICATION);
            boardRepository.save(bullet);
            sendingNotificationToEmail(bullet);
        });
        log.info("[END CRON]: runCronClosedDateTimeExpiredBulletinBoards");
    }

    private static void sendingNotificationToEmail(Board bullet) {
        log.warn("Отправка уведомление email на эл.адресс: {}", bullet.getUser().getEmail());
        log.warn("Уважемый продовец аукцион на ваше обявление: {} было закрыто по истечению срока", bullet.getName());
    }
}
