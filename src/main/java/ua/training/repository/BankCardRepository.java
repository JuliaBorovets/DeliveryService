package ua.training.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankCardRepository extends CrudRepository<BankCard, Long> {

    List<BankCard> findBankCardByUsers(User user);

    Optional<BankCard> findBankCardByIdAndExpMonthAndExpYearAndCcv(Long id, Long expMonth, Long expYear, Long cvv);


}
