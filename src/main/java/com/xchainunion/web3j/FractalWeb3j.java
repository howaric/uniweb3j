package com.xchainunion.web3j;

import org.web3j.protocol.Web3jService;

/**
 * @author howaric@163.com
 * @date 2019/11/3
 */
public interface FractalWeb3j extends Fractal {

    static FractalWeb3j build(Web3jService web3jService) {
        return new JsonRpc2_0FractalWeb3j(web3jService);
    }

}
