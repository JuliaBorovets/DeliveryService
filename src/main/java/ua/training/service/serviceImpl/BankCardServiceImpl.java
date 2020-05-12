package ua.training.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.controller.exception.BankException;
import ua.training.entity.user.BankCard;
import ua.training.entity.user.User;
import ua.training.repository.BankCardRepository;
import ua.training.service.BankCardService;

import java.math.BigDecimal;

@Slf4j
@Service
public class BankCardServiceImpl implements BankCardService {
    private final BankCardRepository bankCardRepository;

    public BankCardServiceImpl(BankCardRepository bankCardRepository) {
        this.bankCardRepository = bankCardRepository;
    }

    private BankCard findById(Long id) throws BankException {
        return bankCardRepository
                .findById(id)
                .orElseThrow(() -> new BankException("can not find bank card with id = " + id));
    }
    public BankCard addBankCard(Long bankId, User user) throws BankException {
        BankCard bankCard = findById(bankId);
        user.addBankCard(bankCard);
        return bankCard;
    }

    public void deleteBankCard(Long bankId) throws BankException {
        BankCard bankCard = findById(bankId);
        bankCard.deleteBankCard();
    }


    public void replenishBankCard(Long bankId, BigDecimal balance) throws BankException {
        BankCard bankCard = findById(bankId);
        bankCard.replenish(balance);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    public void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws BankException {
        replenishBankCard(fromAccountId, amount.negate());
        replenishBankCard(toAccountId, amount);

    }
}
