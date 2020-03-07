package com.xchainunion.web3j.response;

import org.web3j.protocol.core.Response;

import java.util.Optional;

/**
 * @author howaric@163.com
 * @date 2019/11/27
 */
public class FtGetTransactionReceiptResult extends Response<FtTransactionReceipt> {

    public Optional<FtTransactionReceipt> getReceipt() {
        return Optional.ofNullable(getResult());
    }

}
