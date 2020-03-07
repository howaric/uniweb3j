package com.xchainunion.web3j;

import com.xchainunion.web3j.constant.ActionType;
import com.xchainunion.web3j.constant.AssetId;
import com.xchainunion.web3j.constant.ChainHost;
import com.xchainunion.web3j.constant.ChainId;
import com.xchainunion.web3j.payload.PayloadProvider;
import com.xchainunion.web3j.response.FtTransactionReceipt;
import com.xchainunion.web3j.tx.FtTransaction;
import com.xchainunion.web3j.tx.FtTransactionManager;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class ExampleTest {

    private FractalWeb3j fractalWeb3j = FractalWeb3j.build(new HttpService(ChainHost.MAIN));

    @Test
    public void testCreateAccount() throws IOException, TransactionException {
        Credentials accout4testweb3jCredentials = Credentials.create("25c96b945baa89ea60e43b7909d142bd7ba851139eb1eae2e3dfa116b9a8650b");
        FtTransactionManager ftTransactionManager = new FtTransactionManager.Builder().fractalWeb3j(fractalWeb3j).credentials(accout4testweb3jCredentials).chainId(ChainId.MAIN).build();
        //create FtTransaction
        FtTransaction ftTransaction = new FtTransaction();
        ftTransaction.setActionType(ActionType.CREATE_NEW_ACCOUNT);
        ftTransaction.setAccountName("accout4testweb3j");
        ftTransaction.setToAccountName("fractal.account");
        ftTransaction.setAssetId(AssetId.FT);
        ftTransaction.setAmount(new BigInteger("10000000000000000000"));
        //still use accout4testweb3jCredentials as publicKey
        String publicKey = "0x04" + Numeric.toHexStringNoPrefix(accout4testweb3jCredentials.getEcKeyPair().getPublicKey());
        //send transaction
        FtTransactionReceipt ftTransactionReceipt = ftTransactionManager.ftSendRawTransaction(ftTransaction,
                PayloadProvider.createAccountPayload("accout4testweb3j.test1", "accout4testweb3j", publicKey, "my test wallet"));
        System.out.println(ftTransactionReceipt);
    }

    @Test
    public void testTransfer() throws IOException, TransactionException {
        Credentials accout4testweb3jCredentials = Credentials.create("25c96b945baa89ea60e43b7909d142bd7ba851139eb1eae2e3dfa116b9a8650b");
        FtTransactionManager ftTransactionManager = new FtTransactionManager.Builder().fractalWeb3j(fractalWeb3j).credentials(accout4testweb3jCredentials).chainId(ChainId.MAIN).build();
        //create FtTransaction
        FtTransaction ftTransaction = new FtTransaction();
        ftTransaction.setActionType(ActionType.TRANSFER);
        ftTransaction.setAccountName("accout4testweb3j");
        ftTransaction.setToAccountName("accout4testweb3j.test1");
        ftTransaction.setAssetId(AssetId.FT);
        ftTransaction.setAmount(new BigInteger("10000000000000000"));//0.01FT
        //send transaction
        FtTransactionReceipt ftTransactionReceipt = ftTransactionManager.ftSendRawTransaction(ftTransaction, PayloadProvider.createTransferPayload());
        System.out.println(ftTransactionReceipt);
    }

}
