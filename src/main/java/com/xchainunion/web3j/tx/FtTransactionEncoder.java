package com.xchainunion.web3j.tx;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author howaric@163.com
 * @date 2019/11/5
 */
public class FtTransactionEncoder {

    public static byte[] signMessage(FtTransaction ftTransaction, int chainId, Credentials credentials, FeePayer feePayer) {
        if (feePayer != null) {
            ftTransaction.setGasPrice(BigInteger.ZERO);
        }
        Sign.SignatureData signatureData = signTransaction(ftTransaction, credentials, chainId);
        RlpList payer = null;
        if (feePayer != null) {
            Sign.SignatureData payerSign = signPayer(ftTransaction, feePayer, chainId);
            payer = encodePayer(feePayer, payerSign, createFtSignatureData(payerSign, chainId));
        }
        return encode(ftTransaction, signatureData, createFtSignatureData(signatureData, chainId), payer);
    }

    private static RlpList encodePayer(FeePayer feePayer, Sign.SignatureData payerSign, byte[] v) {
        RlpList rlpList = new RlpList(
                RlpString.create(feePayer.getGasPrice()),
                RlpString.create(feePayer.getPayerAccount()),
                new RlpList(
                        RlpString.create(0),//parentIndex
                        new RlpList(new RlpList(
                                RlpString.create(v),
                                RlpString.create(payerSign.getR()),
                                RlpString.create(payerSign.getS()),
                                new RlpList(
                                        RlpString.create(0)//index
                                ))
                        )
                )
        );
        return rlpList;
    }

    private static Sign.SignatureData signPayer(FtTransaction ftTransaction, FeePayer feePayer, int chainId) {
        List<RlpType> action = new ArrayList<>();
        action.add(RlpString.create(ftTransaction.getAccountName()));
        action.add(RlpString.create(ftTransaction.getActionType()));
        action.add(RlpString.create(ftTransaction.getNonce()));
        action.add(RlpString.create(ftTransaction.getToAccountName()));
        action.add(RlpString.create(ftTransaction.getGasLimit()));
        action.add(RlpString.create(ftTransaction.getAmount()));
        action.add(RlpString.create(ftTransaction.getPayload()));
        action.add(RlpString.create(ftTransaction.getAssetId()));
        action.add(RlpString.create(ftTransaction.getRemark()));
        action.add(RlpString.create(feePayer.getPayerAccount()));
        action.add(RlpString.create(feePayer.getGasPrice()));
        action.add(RlpString.create(chainId));
        action.add(RlpString.create(0));
        action.add(RlpString.create(0));
        RlpList rlpList = new RlpList(action);
        List<RlpType> result = new ArrayList<>();
        result.add(RlpString.create(Hash.sha3(Hash.sha3(RlpEncoder.encode(rlpList)))));
        result.add(RlpString.create(0));
        result.add(RlpString.create(ftTransaction.getGasPrice()));
        return Sign.signMessage(Hash.sha3(RlpEncoder.encode(new RlpList(result))), feePayer.getPayerCredential().getEcKeyPair(), false);
    }

    private static byte[] createFtSignatureData(
            Sign.SignatureData signatureData, int chainId) {
        int v = signatureData.getV();
        if (chainId != 0) {
            v = (Numeric.toBigInt("0x1c").intValue() == v) ? 1 : 0;
            v += (chainId * 2 + 35);
        }
        return Numeric.hexStringToByteArray(Numeric.toHexStringWithPrefix(BigInteger.valueOf(v)));
    }

    private static Sign.SignatureData signTransaction(FtTransaction ftTransaction, Credentials credentials, int chainId) {
        List<RlpType> action = new ArrayList<>();
        action.add(RlpString.create(ftTransaction.getAccountName()));
        action.add(RlpString.create(ftTransaction.getActionType()));
        action.add(RlpString.create(ftTransaction.getNonce()));
        action.add(RlpString.create(ftTransaction.getToAccountName()));
        action.add(RlpString.create(ftTransaction.getGasLimit()));
        action.add(RlpString.create(ftTransaction.getAmount()));
        action.add(RlpString.create(ftTransaction.getPayload()));
        action.add(RlpString.create(ftTransaction.getAssetId()));
        action.add(RlpString.create(ftTransaction.getRemark()));
        action.add(RlpString.create(chainId));
        action.add(RlpString.create(0));
        action.add(RlpString.create(0));
        RlpList rlpList = new RlpList(action);
        List<RlpType> result = new ArrayList<>();
        result.add(RlpString.create(Hash.sha3(Hash.sha3(RlpEncoder.encode(rlpList)))));
        result.add(RlpString.create(0));
        result.add(RlpString.create(ftTransaction.getGasPrice()));
        Sign.SignatureData signatureData = Sign.signMessage(Hash.sha3(RlpEncoder.encode(new RlpList(result))), credentials.getEcKeyPair(), false);
        return signatureData;
    }

    private static byte[] encode(FtTransaction ftTransaction, Sign.SignatureData signatureData, byte[] v, RlpList payer) {
        List<RlpType> action = new ArrayList<>();
        action.add(RlpString.create(ftTransaction.getActionType()));
        action.add(RlpString.create(ftTransaction.getNonce()));
        action.add(RlpString.create(ftTransaction.getAssetId()));
        action.add(RlpString.create(ftTransaction.getAccountName()));
        action.add(RlpString.create(ftTransaction.getToAccountName()));
        action.add(RlpString.create(ftTransaction.getGasLimit()));
        action.add(RlpString.create(ftTransaction.getAmount()));
        action.add(RlpString.create(ftTransaction.getPayload()));
        action.add(RlpString.create(ftTransaction.getRemark()));
        action.add(new RlpList(
                RlpString.create(0),
                new RlpList(new RlpList(
                        RlpString.create(v),
                        RlpString.create(signatureData.getR()),
                        RlpString.create(signatureData.getS()),
                        new RlpList(
                                RlpString.create(0)
                        ))
                )
        ));
        if (payer != null) {
            action.add(payer);
        }
        RlpList actionList = new RlpList(action);
        RlpList rlpList = new RlpList(RlpString.create(0), RlpString.create(ftTransaction.getGasPrice()), new RlpList(actionList));
        return RlpEncoder.encode(rlpList);
    }

}
