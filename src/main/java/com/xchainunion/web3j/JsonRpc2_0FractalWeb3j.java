package com.xchainunion.web3j;

import com.xchainunion.web3j.request.CallTransaction;
import com.xchainunion.web3j.response.*;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author howaric@163.com
 * @date 2019/11/3
 */
public class JsonRpc2_0FractalWeb3j implements FractalWeb3j {

    protected final Web3jService web3jService;

    public JsonRpc2_0FractalWeb3j(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    @Override
    public Request<?, FtCallResult> ftCall(CallTransaction transaction) {
        return new Request<>(
                "ft_call",
                Arrays.asList(transaction, "latest"),
                web3jService,
                FtCallResult.class);
    }

    @Override
    public Request<?, FtNonceResult> getNonce(String accountName) {
        return new Request<>(
                "account_getNonce",
                Arrays.asList(accountName),
                web3jService,
                FtNonceResult.class);
    }

    @Override
    public Request<?, FtSendTransactionResult> sendRawTransaction(String transactionData) {
        return new Request<>(
                "ft_sendRawTransaction",
                Arrays.asList(transactionData),
                web3jService,
                FtSendTransactionResult.class);
    }

    @Override
    public Request<?, FtGetTransactionReceiptResult> getTransactionReceipt(String transactionHash) {
        return new Request<>(
                "ft_getTransactionReceipt",
                Arrays.asList(transactionHash),
                web3jService,
                FtGetTransactionReceiptResult.class);
    }

    @Override
    public Request<?, FtSendTransactionResult> createPendingTransactionFilter() {
        return new Request<>(
                "ft_newPendingTransactionFilter",
                Collections.emptyList(),
                web3jService,
                FtSendTransactionResult.class);
    }

    @Override
    public Request<?, FtPendingTransactionResult> getFilterChanges(String filterId) {
        return new Request<>(
                "ft_getFilterChanges",
                Arrays.asList(filterId),
                web3jService,
                FtPendingTransactionResult.class);
    }

}
