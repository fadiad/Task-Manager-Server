package TaskManager.repository;

import TaskManager.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findById(int id);
}
