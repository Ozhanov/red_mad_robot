package kz.test.tz.repo;

import kz.test.tz.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT bb FROM Board bb WHERE bb.name LIKE %:name%")
    List<Board> findByNameContaining(@Param("name") String name);

    @Query("SELECT bb FROM Board bb WHERE bb.name = :name")
    List<Board> findByName(@Param("name") String name);

    @Query("SELECT b FROM Board b WHERE b.closedDateTime < :currentDateTime AND b.status='ACTIVE'")
    List<Board> closedDateTimeExpiredBulletinBoards(LocalDateTime currentDateTime);

    Optional<Board> findByIdAndStatus(Long id, Board.Status status);
}
