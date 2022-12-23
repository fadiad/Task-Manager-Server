package TaskManager.service;

import TaskManager.entities.Item;
import TaskManager.entities.User;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class ItemService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ItemRepository itemRepository;


    public ItemDTO addNewItem(Item newItem) {
        User createUser = userRepository.findById(newItem.getCreator().getId()).orElseThrow(()->new IllegalArgumentException("User not found"));
        newItem.setCreator(createUser);
        return new ItemDTO(itemRepository.save(newItem));
    }

    public ItemDTO assignItemTo(int itemId, int userId) {
        User userToAssign = userRepository.findById( userId).orElseThrow(()->new IllegalArgumentException("User not found"));
        Item itemToAssign = itemRepository.findById(itemId).orElseThrow(()->new IllegalArgumentException("Item not found"));
        itemToAssign.setAssignTo(userToAssign);

        return new ItemDTO(itemRepository.save(itemToAssign));
    }

    public ItemDTO updateItem(int itemId, Item updatedItem) {
        Item oldItem = itemRepository.findById(itemId).orElseThrow(()->new IllegalArgumentException("Item not found"));
        oldItem.setItem(updatedItem);
        return new ItemDTO(itemRepository.save(oldItem));
    }
    @Transactional
    public void deleteItem(int itemId) {
        itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepository.deleteById(itemId);
    }

}
