package com.xchainunion.web3j.tx;

import com.xchainunion.web3j.FractalWeb3j;
import com.xchainunion.web3j.constant.ActionType;
import com.xchainunion.web3j.constant.ChainId;
import com.xchainunion.web3j.payload.PayloadProvider;
import com.xchainunion.web3j.request.CallTransaction;
import com.xchainunion.web3j.response.FtCallResult;
import com.xchainunion.web3j.response.FtSendTransactionResult;
import com.xchainunion.web3j.response.FtTransactionReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class FtTransactionManager {

    private Logger logger = LoggerFactory.getLogger(FtTransactionManager.class);

    private FractalWeb3j fractalWeb3j;
    private FtTransactionReceiptProcessor ftTransactionReceiptProcessor;
    private FeePayer feePayer;
    private Credentials credentials;
    private FtGasProvider ftGasProvider = new FtGasProvider();
    private int chainId;

    private FtTransactionManager(FractalWeb3j fractalWeb3j, FtTransactionReceiptProcessor ftTransactionReceiptProcessor, FeePayer feePayer, Credentials credentials, FtGasProvider ftGasProvider, int chainId) {
        this.fractalWeb3j = fractalWeb3j;
        this.ftTransactionReceiptProcessor = ftTransactionReceiptProcessor;
        this.feePayer = feePayer;
        this.credentials = credentials;
        this.ftGasProvider = ftGasProvider;
        this.chainId = chainId;
    }

    /**
     * 发起合约交易
     *
     * @param ftTransaction
     * @param function
     * @return
     * @throws IOException
     * @throws TransactionException
     */
    public FtTransactionReceipt ftSendContractTransaction(FtTransaction ftTransaction, Function function) throws IOException, TransactionException {
        ftTransaction.setActionType(ActionType.CALL_CONTRACT);
        return ftSendRawTransaction(ftTransaction, Numeric.hexStringToByteArray(FunctionEncoder.encode(function)));
    }

    /**
     * 发起普通交易
     *
     * @param ftTransaction
     * @param payload       {@link PayloadProvider}
     * @return
     * @throws IOException
     * @throws TransactionException
     */
    public FtTransactionReceipt ftSendRawTransaction(FtTransaction ftTransaction, byte[] payload) throws IOException, TransactionException {
        ftTransaction.setNonce(getNonce(ftTransaction.getAccountName()));
        ftTransaction.setGasLimit(ftGasProvider.getGasLimit());
        ftTransaction.setGasPrice(ftGasProvider.getGasPrice());
        ftTransaction.setPayload(payload);
        String message = Numeric.toHexString(FtTransactionEncoder.signMessage(ftTransaction, chainId, credentials, feePayer));
        FtSendTransactionResult ftSendTransactionResult = fractalWeb3j.sendRawTransaction(message).send();
        logger.debug("TransactionHash: {}", ftSendTransactionResult.getTransactionHash());
        return ftTransactionReceiptProcessor.waitForTransactionReceipt(ftSendTransactionResult.getTransactionHash());
    }

    /**
     * @param callTransaction
     * @param function
     * @return
     * @throws IOException
     */
    public List<Type> ftCall(CallTransaction callTransaction, Function function) throws IOException {
        callTransaction.setActionType(ActionType.CALL_CONTRACT);
        callTransaction.setAssetId(BigInteger.ZERO);
        callTransaction.setValue(BigInteger.ZERO);
        callTransaction.setData(FunctionEncoder.encode(function));
        FtCallResult ftCallResult = fractalWeb3j.ftCall(callTransaction).send();
        String result = ftCallResult.getResult();
        return FunctionReturnDecoder.decode(result, function.getOutputParameters());
    }

    protected BigInteger getNonce(String accountName) throws IOException {
        return fractalWeb3j.getNonce(accountName).send().getNonce();
    }


    public static class Builder {
        private FractalWeb3j fractalWeb3j;
        private FtTransactionReceiptProcessor ftTransactionReceiptProcessor;
        private FeePayer feePayer;
        private Credentials credentials;
        private FtGasProvider ftGasProvider;
        private int chainId;

        public Builder fractalWeb3j(FractalWeb3j fractalWeb3j) {
            this.fractalWeb3j = fractalWeb3j;
            return this;
        }

        public Builder ftTransactionReceiptProcessor(FtTransactionReceiptProcessor ftTransactionReceiptProcessor) {
            this.ftTransactionReceiptProcessor = ftTransactionReceiptProcessor;
            return this;
        }

        public Builder feePayer(FeePayer feePayer) {
            this.feePayer = feePayer;
            return this;
        }

        public Builder credentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public Builder ftGasProvider(FtGasProvider ftGasProvider) {
            this.ftGasProvider = ftGasProvider;
            return this;
        }

        public Builder chainId(int chainId) {
            this.chainId = chainId;
            return this;
        }

        public FtTransactionManager build() {
            if (ftTransactionReceiptProcessor == null) {
                ftTransactionReceiptProcessor = new PollFtTransactionReceiptProcessor(fractalWeb3j);
            }
            if (ftGasProvider == null) {
                ftGasProvider = new FtGasProvider();
            }
            if (chainId == 0) {
                chainId = ChainId.TEST;
            }
            return new FtTransactionManager(fractalWeb3j, ftTransactionReceiptProcessor, feePayer, credentials, ftGasProvider, chainId);
        }

    }

}
