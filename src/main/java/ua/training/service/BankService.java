package ua.training.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.BankTransactionException;
import ua.training.entity.BankAccount;
import ua.training.entity.user.User;
import ua.training.repository.BankRepository;
import ua.training.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class BankService {
    private BankRepository bankRepository;
    private UserRepository userRepository;


    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public Optional<BankAccount> getBankAccountById(@NotNull Long id) {

        return bankRepository.findById(id);
    }


    // MANDATORY: Transaction must be created before.

//    public void addMoney(User user, BigDecimal money) {
//        try {
//            User u = userRepository.getOne(user.getId());
//            BigDecimal current = u.getBalance();
//            u.setBalance(current.add(money));
//            userRepository.save(u);
//        } catch (NullPointerException e){
//            log.error("null pointer");
//        }
//    }

}
