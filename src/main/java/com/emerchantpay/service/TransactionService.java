package com.emerchantpay.service;

import com.emerchantpay.model.dto.request.TransactionRequestDto;
import com.emerchantpay.model.entities.Merchant;
import com.emerchantpay.model.entities.Transaction;
import com.emerchantpay.repository.TransactionRepository;
import com.emerchantpay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository<Merchant> userRepository;

    //TODO: Integration test
    @Transactional
    public Transaction createTransaction(TransactionRequestDto transactionDto) {
        Merchant merchant = userRepository.findById(transactionDto.getMerchantId()).orElse(null);
        if (merchant == null || !merchant.isActive()) {
            return null;
        }
        Transaction relatedTransaction =
                transactionDto.getBelongTo() != null ? transactionRepository.findById(transactionDto.getBelongTo()).orElse(null) : null;
        Transaction result = transactionDto.getTransactionType().createTransaction(transactionDto, relatedTransaction);
        if (relatedTransaction != null) {
            transactionRepository.save(relatedTransaction);
        }
        result.setReferenceId(merchant);
        transactionRepository.save(result);
        return result;
    }


}
