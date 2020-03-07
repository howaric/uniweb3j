package com.xchainunion.web3j.tx;

import com.xchainunion.web3j.FractalWeb3j;
import com.xchainunion.web3j.response.FtTransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Optional;

public class PollFtTransactionReceiptProcessor extends FtTransactionReceiptProcessor {

    private final long sleepDuration;
    private final int attempts;

    public PollFtTransactionReceiptProcessor(FractalWeb3j fractalWeb3j) {
        this(fractalWeb3j, 10000, 60);
    }

    public PollFtTransactionReceiptProcessor(FractalWeb3j fractalWeb3j, long sleepDuration, int attempts) {
        super.fractalWeb3j = fractalWeb3j;
        this.sleepDuration = sleepDuration;
        this.attempts = attempts;
    }

    @Override
    public FtTransactionReceipt waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException {
        return getTransactionReceipt(transactionHash, sleepDuration, attempts);
    }

    private FtTransactionReceipt getTransactionReceipt(
            String transactionHash, long sleepDuration, int attempts)
            throws IOException, TransactionException {

        Optional<FtTransactionReceipt> ftTransactionDetails = sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!ftTransactionDetails.isPresent()) {
                try {
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    throw new TransactionException(e);
                }
                ftTransactionDetails = sendTransactionReceiptRequest(transactionHash);
            } else {
                return ftTransactionDetails.get();
            }
        }
        throw new TransactionException("Transaction receipt was not generated after "
                + ((sleepDuration * attempts) / 1000
                + " seconds for transaction: " + transactionHash));
    }
}
