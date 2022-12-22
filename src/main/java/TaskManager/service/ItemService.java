package TaskManager.service;

import TaskManager.entities.Item;
import TaskManager.entities.User;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ItemService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ItemRepository itemRepository;

    public Item addNewItem(Item newItem) {
        User createUser = userRepository.findById(newItem.getCreator().getId()).orElseThrow(()->new IllegalArgumentException("User not found"));
        newItem.setCreator(createUser);
        return itemRepository.save(newItem);
    }

    public ItemDTO aassignItemTo(int itemId, int userId) {
        User userToAssign = userRepository.findById( userId).orElseThrow(()->new IllegalArgumentException("User not found"));
        Item itemToAssign = itemRepository.findById(itemId).orElseThrow(()->new IllegalArgumentException("Item not found"));
        itemToAssign.setAssignTo(userToAssign);

        return new ItemDTO(itemRepository.save(itemToAssign));
    }
    public void deleteItem(int itemId) {
        itemRepository.deleteById(itemId);
    }
}
