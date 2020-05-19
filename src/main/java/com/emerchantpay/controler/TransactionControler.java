package com.emerchantpay.controler;

import com.emerchantpay.model.dto.request.TransactionRequestDto;
import com.emerchantpay.model.dto.response.TransactionResponseDto;
import com.emerchantpay.model.entities.Transaction;
import com.emerchantpay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionControler {

    @Autowired
    private TransactionService transactionService;

    //TODO
    @GetMapping
    private List<TransactionResponseDto> getTransactionByMerchant(@RequestHeader(name = "Authorization") String token) {
        return null;
    }


    @PostMapping
    public Transaction createTransaction(@RequestBody TransactionRequestDto transactionDto) {
        return transactionService.createTransaction(transactionDto);
    }
}
