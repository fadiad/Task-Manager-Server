package TaskManager.repository;

import TaskManager.entities.Item;
import TaskManager.entities.entitiesUtils.ItemTypes;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
   List<Item> findByBoardId(int boardId);
   Optional<Item> findById(int itemId);
   void deleteById(int id);

   void deleteByStatusId(int statusId);

   List<Item> findByBoardIdAndItemType(int boardId, ItemTypes type);

   List<Item> findAll(Specification<Item> specification);
}
