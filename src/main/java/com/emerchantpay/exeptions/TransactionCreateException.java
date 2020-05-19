package com.emerchantpay.exeptions;


public class TransactionCreateException extends RuntimeException {

    public TransactionCreateException(String message) {
        super(message);
    }

}
