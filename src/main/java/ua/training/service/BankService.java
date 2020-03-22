package ua.training.service;

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
    @Transactional(propagation = Propagation.MANDATORY)
    public void addAmount(Long id, BigDecimal amount) throws BankTransactionException {
        Optional<BankAccount> account = getBankAccountById(id);
        if (!account.isPresent()) {
            throw new BankTransactionException("Account not found " + id);
        }
        BigDecimal newBalance = account.get().getBalance().add(amount);
        if (newBalance.doubleValue() < 0) {
            throw new BankTransactionException(
                    "The money in the account '" + id + "' is not enough (" + account.get().getBalance() + ")");
        }
        account.get().setBalance(newBalance);
    }

    // Do not catch BankTransactionException in this method.
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankTransactionException.class)
    public void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount)
            throws BankTransactionException {

        addAmount(toAccountId, amount);
        addAmount(fromAccountId, amount.negate());
    }

    public void addMoney(Long id, BigDecimal money) throws BankTransactionException {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {

            throw new BankTransactionException("User not found " + id);
        }
        if (money.compareTo(BigDecimal.ZERO) > 0) {
            user.get().setBalance(user.get().getBalance().add(money));
        }
    }

}
