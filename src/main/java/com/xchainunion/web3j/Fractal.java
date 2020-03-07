package com.xchainunion.web3j;

import com.xchainunion.web3j.request.CallTransaction;
import com.xchainunion.web3j.response.*;
import org.web3j.protocol.core.Request;

/**
 * @author howaric@163.com
 * @date 2019/11/3
 */
public interface Fractal {

    Request<?, FtCallResult> ftCall(CallTransaction transaction);

    Request<?, FtNonceResult> getNonce(String accountName);

    Request<?, FtSendTransactionResult> sendRawTransaction(String transactionData);

    Request<?, FtGetTransactionReceiptResult> getTransactionReceipt(String transactionHash);

    Request<?, FtSendTransactionResult> createPendingTransactionFilter();

    Request<?, FtPendingTransactionResult> getFilterChanges(String filterId);

}
