package com.emerchantpay.service;

import com.emerchantpay.exeptions.TransactionCreateException;
import com.emerchantpay.model.dto.request.TransactionRequestDto;
import com.emerchantpay.model.entities.Transaction;
import com.emerchantpay.model.enums.TransactionStatus;
import com.emerchantpay.model.enums.TransactionType;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static com.emerchantpay.model.enums.TransactionStatus.*;
import static com.emerchantpay.model.enums.TransactionType.*;

public class TransactionStateTest {

    private static final String CUSTOMER_EMAIL = "cvetomir.andrev@gmail.com";
    private static final String CUSTOMER_PHONE = "0883476912";

// AUTHORIZE_TRANSACTION TEST

    @Test
    public void testGivenAuthorizeTransactionRequestThenCreateAuthorizeTransaction() {
        validateTransactionState(
                createTransactionRequestDo(200L, AUTHORIZE_TRANSACTION),
                null,
                createTransaction(200L, AUTHORIZE_TRANSACTION, APPROVED),
                null);
    }

    @Test
    public void testGivenAuthorizeTransactionWithRelatedTransactionThenException() {
        validateTransactionState(
                createTransactionRequestDo(200L, AUTHORIZE_TRANSACTION),
                new Transaction());
    }


// CHARGE TRANSACTION TEST

    @Test
    public void testGivenChargeRequestThenCreateChargeTransaction() {
        Transaction expectedTransaction = createTransaction(200L, CHARGE, APPROVED);
        expectedTransaction.setInitialTransaction(createTransaction(100L, AUTHORIZE_TRANSACTION, APPROVED));
        validateTransactionState(
                createTransactionRequestDo(200L, CHARGE),
                createTransaction(300L, AUTHORIZE_TRANSACTION, APPROVED),
                expectedTransaction,
                expectedTransaction.getInitialTransaction());
    }

    @Test
    public void testGivenChargeRequestWithMoreAmountThanRelatedTransactionThenThrowException() {
        validateTransactionState(
                createTransactionRequestDo(200L, CHARGE),
                createTransaction(100L, AUTHORIZE_TRANSACTION, APPROVED));
    }

    @Test
    public void testGivenChargeRequestWithNoRelatedTransactionThenThrowException() {
        validateTransactionState(
                createTransactionRequestDo(200L, CHARGE),
                null);
    }

    @Test
    public void testGivenChargeRequestWithWrongRelatedTransactionThenThrowException() {
        validateTransactionState(
                createTransactionRequestDo(200L, CHARGE),
                createTransaction(300L, CHARGE, APPROVED));
    }

    @Test
    public void testGivenChargeRequestWithRelatedTransactionWithStatusErrorThenThrowException() {
        Transaction relatedTransaction = createTransaction(300L, AUTHORIZE_TRANSACTION, ERROR);
        validateTransactionState(
                createTransactionRequestDo(200L, CHARGE),
                relatedTransaction,
                createTransaction(200L, CHARGE, ERROR),
                relatedTransaction);
    }

    @Test
    public void testGivenChargeRequestWithRelatedTransactionWithStatusReversedThenThrowException() {
        Transaction relatedTransaction = createTransaction(300L, AUTHORIZE_TRANSACTION, REVERSED);
        validateTransactionState(
                createTransactionRequestDo(200L, CHARGE),
                relatedTransaction,
                createTransaction(200L, CHARGE, ERROR),
                relatedTransaction);
    }

// REFUND TRANSACTION TEST

    @Test
    public void testGivenRefundRequestThenCreateChargeTransaction() {
        Transaction expectedTransaction = createTransaction(200L, REFUND, APPROVED);
        Transaction chargeTransaction = createTransaction(200L, CHARGE, APPROVED);
        chargeTransaction.setInitialTransaction(createTransaction(300L, AUTHORIZE_TRANSACTION, APPROVED));
        expectedTransaction.setInitialTransaction(chargeTransaction);
        validateTransactionState(
                createTransactionRequestDo(200L, REFUND),
                chargeTransaction,
                expectedTransaction,
                expectedTransaction.getInitialTransaction());
    }

    @Test
    public void testGivenRefundRequestWithNoRelatedTransactionThenThrowException() {
        validateTransactionState(
                createTransactionRequestDo(200L, REFUND),
                null);
    }

    @Test
    public void testGivenRefundRequestWithWrongRelatedTransactionThenThrowException() {
        validateTransactionState(
                createTransactionRequestDo(200L, REFUND),
                createTransaction(300L, AUTHORIZE_TRANSACTION, APPROVED));
    }

    @Test
    public void testGivenRefundRequestWithRelatedTransactionWithStatusErrorThenThrowException() {
        Transaction relatedTransaction = createTransaction(300L, CHARGE, ERROR);
        validateTransactionState(
                createTransactionRequestDo(200L, REFUND),
                relatedTransaction,
                createTransaction(200L, REFUND, ERROR),
                relatedTransaction);
    }

    @Test
    public void testGivenRefundRequestWithRelatedTransactionWithStatusReversedThenThrowException() {
        Transaction relatedTransaction = createTransaction(300L, CHARGE, REVERSED);
        validateTransactionState(
                createTransactionRequestDo(200L, REFUND),
                relatedTransaction,
                createTransaction(200L, REFUND, ERROR),
                relatedTransaction);
    }


    // REVERSE TRANSACTION TEST
    @Test
    public void testGivenReverseRequestThenCreateChargeTransaction() {
        Transaction expectedTransaction = createTransaction(200L, REVERSAL, APPROVED);
        expectedTransaction.setInitialTransaction(createTransaction(300L, AUTHORIZE_TRANSACTION, REVERSED));
        validateTransactionState(
                createTransactionRequestDo(200L, REVERSAL),
                createTransaction(300L, AUTHORIZE_TRANSACTION, APPROVED),
                expectedTransaction,
                expectedTransaction.getInitialTransaction());
    }

    @Test
    public void testGivenReverseRequestWithNoRelatedTransactionThenThrowException() {
        validateTransactionState(
                createTransactionRequestDo(200L, REVERSAL),
                null);
    }

    @Test
    public void testGivenReversalRequestWithWrongRelatedTransactionThenThrowException() {
        validateTransactionState(
                createTransactionRequestDo(200L, REVERSAL),
                createTransaction(300L, CHARGE, APPROVED));
    }

    @Test
    public void testGivenReverseRequestWithRelatedTransactionWithStatusErrorThenThrowException() {
        Transaction relatedTransaction = createTransaction(300L, AUTHORIZE_TRANSACTION, ERROR);
        validateTransactionState(
                createTransactionRequestDo(200L, REVERSAL),
                relatedTransaction,
                createTransaction(200L, REVERSAL, ERROR),
                relatedTransaction);
    }

    @Test
    public void testGivenReverseRequestWithRelatedTransactionWithStatusReversedThenThrowException() {
        Transaction relatedTransaction = createTransaction(300L, AUTHORIZE_TRANSACTION, REVERSED);
        validateTransactionState(
                createTransactionRequestDo(300L, REVERSAL),
                relatedTransaction,
                createTransaction(300L, REVERSAL, ERROR),
                relatedTransaction);
    }


    private void validateTransactionState(TransactionRequestDto transactionRequestDto,
                                          Transaction relatedTransaction) {
        Assertions.assertThrows(
                TransactionCreateException.class,
                () -> transactionRequestDto.getTransactionType().createTransaction(transactionRequestDto, relatedTransaction));
    }


    private void validateTransactionState(TransactionRequestDto transactionRequestDto,
                                          Transaction relatedTransaction,
                                          Transaction expectedResponse,
                                          Transaction expectedRelatedTransaction) {

        Transaction actualResponse = transactionRequestDto.getTransactionType().createTransaction(transactionRequestDto, relatedTransaction);
        assertEquals(expectedResponse, actualResponse);
        if (expectedRelatedTransaction == null) {
            Assertions.assertEquals(actualResponse.getInitialTransaction(), expectedRelatedTransaction);
        } else {
            assertEquals(actualResponse.getInitialTransaction(), expectedRelatedTransaction);
        }
    }

    private void assertEquals(Transaction expected, Transaction actual) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getAmount(), actual.getAmount()),
                () -> Assertions.assertEquals(expected.getStatus(), actual.getStatus()),
                () -> Assertions.assertEquals(expected.getCustomerEmail(), actual.getCustomerEmail()),
                () -> Assertions.assertEquals(expected.getCustomerPhone(), actual.getCustomerPhone()),
                () -> Assertions.assertEquals(expected.getType(), actual.getType())
        );
    }

    private Transaction createTransaction(Long amount, TransactionType type, TransactionStatus status) {

        return Transaction.builder()
                .amount(amount)
                .customerEmail(CUSTOMER_EMAIL)
                .customerPhone(CUSTOMER_PHONE)
                .status(status)
                .type(type)
                .build();
    }

    private TransactionRequestDto createTransactionRequestDo(Long amount, TransactionType type) {

        return TransactionRequestDto.builder().amount(amount)
                .customerEmail(CUSTOMER_EMAIL)
                .customerPhone(CUSTOMER_PHONE)
                .merchantId(1L)
                .transactionType(type)
                .build();
    }
}
