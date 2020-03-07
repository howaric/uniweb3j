package com.xchainunion.web3j.tx;

import com.xchainunion.web3j.FractalWeb3j;
import com.xchainunion.web3j.response.FtGetTransactionReceiptResult;
import com.xchainunion.web3j.response.FtTransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Optional;

public abstract class FtTransactionReceiptProcessor {

    protected FractalWeb3j fractalWeb3j;

    public abstract FtTransactionReceipt waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException;

    Optional<FtTransactionReceipt> sendTransactionReceiptRequest(String transactionHash) throws IOException, TransactionException {
        FtGetTransactionReceiptResult ftGetTransactionReceiptResult = fractalWeb3j.getTransactionReceipt(transactionHash).send();
        if (ftGetTransactionReceiptResult.hasError()) {
            throw new TransactionException("Error processing request: " + ftGetTransactionReceiptResult.getError().getMessage());
        } else {
            return ftGetTransactionReceiptResult.getReceipt();
        }
    }
}
