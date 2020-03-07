package com.xchainunion.web3j.response;

import org.web3j.protocol.core.Response;

import java.util.List;

/**
 * @author howaric@163.com
 * @date 2019/11/26
 */
public class FtPendingTransactionResult extends Response<List<String>> {

    public List<String> getPendingTransactionHashList() {
        return getResult();
    }

}
