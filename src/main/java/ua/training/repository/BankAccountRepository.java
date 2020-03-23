package ua.training.repository;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.BankTransactionException;

import ua.training.entity.order.Order;
import ua.training.entity.user.User;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

//@Repository
//@NoArgsConstructor
//public interface BankAccountRepository extends JpaRepository<Order, Long> {

//    // MANDATORY: Transaction must be created before.
//    @Transactional
//    public void addAmount(Long id, BigDecimal amount) throws BankTransactionException {
//        User account = this.findById(id);
//        if (account == null) {
//            throw new BankTransactionException("Account not found " + id);
//        }
//        BigDecimal newBalance = account.getBalance().add(amount);
//        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
//            throw new BankTransactionException(
//                    "The money in the account '" + id + "' is not enough (" + account.getBalance() + ")");
//        }
//        account.setBalance(newBalance);
//    }
//
//    // Do not catch BankTransactionException in this method.
//    @Transactional(propagation = Propagation.REQUIRES_NEW,
//            rollbackFor = BankTransactionException.class)
//    public void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws BankTransactionException {
//        addAmount(toAccountId, amount);
//        addAmount(fromAccountId, amount.negate());
//    }

//
//}
