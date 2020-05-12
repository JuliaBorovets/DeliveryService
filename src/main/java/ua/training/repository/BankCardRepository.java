package ua.training.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.training.entity.user.BankCard;

@Repository
public interface BankCardRepository extends CrudRepository<BankCard, Long> {
}
