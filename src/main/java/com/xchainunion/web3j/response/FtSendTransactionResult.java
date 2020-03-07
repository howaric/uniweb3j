package com.xchainunion.web3j.response;

import org.web3j.protocol.core.Response;

/**
 * @author howaric@163.com
 * @date 2019/11/26
 */
public class FtSendTransactionResult extends Response<String> {

    public String getTransactionHash() {
        return getResult();
    }

}
