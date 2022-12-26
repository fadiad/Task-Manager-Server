package TaskManager.utils.filter;

import TaskManager.entities.Item;
import TaskManager.entities.entitiesUtils.FilterItem;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class QueryBuilder implements Specification<Item> {
    FilterItem filter;

    @Override
    public Predicate toPredicate(Root<Item> root, @NotNull CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("boardId"), filter.getBoardId()));

        if (this.filter.getStatusId()!= null) {
            predicates.add(criteriaBuilder.equal(root.get("statusId"), this.filter.getStatusId()));
        }

        if (this.filter.getDueDate() != null) {
            predicates.add(criteriaBuilder.equal(root.get("dueDate"), this.filter.getDueDate()));
        }

        if (this.filter.getItemType() != null) {
            predicates.add(criteriaBuilder.equal(root.get("itemType"), this.filter.getItemType()));
        }

        if (this.filter.getImportance() != null) {
            predicates.add(criteriaBuilder.equal(root.get("importance"), this.filter.getImportance()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


}

