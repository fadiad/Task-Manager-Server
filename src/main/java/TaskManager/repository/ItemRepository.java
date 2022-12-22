package TaskManager.repository;

import TaskManager.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
   List<Item> findByBoardId(int boardId);
   Optional<Item> findById(int itemId);
   void deleteById(int id);
}
