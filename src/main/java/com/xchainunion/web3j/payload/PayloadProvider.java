package com.xchainunion.web3j.payload;

import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

/**
 * https://github.com/fractalplatform/fractal/wiki/%E4%BA%A4%E6%98%93payload%E6%9E%84%E9%80%A0
 */
public class PayloadProvider {

    private PayloadProvider() {
    }

    /**
     * 获取创建账户payload
     *
     * @param accountName
     * @param founder
     * @param publicKey
     * @param description
     * @return
     */
    public static byte[] createAccountPayload(String accountName, String founder, String publicKey, String description) {
        RlpList rlpList = new RlpList(RlpString.create(accountName), RlpString.create(founder),
                RlpString.create(Numeric.hexStringToByteArray(publicKey)), RlpString.create(description));
        return RlpEncoder.encode(rlpList);
    }

    public static byte[] createTransferPayload() {
        return new byte[0];
    }

}
