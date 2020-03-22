package ua.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.training.entity.BankAccount;

import java.util.Optional;


@Repository
public interface BankRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findBankAccountById(Long id);

}