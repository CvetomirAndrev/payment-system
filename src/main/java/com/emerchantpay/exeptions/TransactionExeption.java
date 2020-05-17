package com.emerchantpay.exeptions;


public class TransactionExeption extends RuntimeException {

    public TransactionExeption(String message) {
        super(message);
    }

}
