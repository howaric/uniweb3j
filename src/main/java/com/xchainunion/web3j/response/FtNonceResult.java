package com.xchainunion.web3j.response;

import org.web3j.protocol.core.Response;

import java.math.BigInteger;

/**
 * @author howaric@163.com
 * @date 2019/11/26
 */
public class FtNonceResult extends Response<BigInteger> {

    public BigInteger getNonce() {
        return getResult();
    }

}
