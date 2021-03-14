# uniweb3j
## Overview
This libary is the java client for FT block chain which is based on web3j.

## Usage
```java
//Init Credentials from private key
Credentials accout4testweb3jCredentials = Credentials.create("25c96b945baa89ea60e43b7909d142bd7ba851139eb1eae2e3dfa116b9a8650b");
//Build FtTransactionManager
FtTransactionManager ftTransactionManager = new FtTransactionManager.Builder().fractalWeb3j(fractalWeb3j).credentials(accout4testweb3jCredentials).chainId(ChainId.MAIN).build();
//Create FtTransaction
FtTransaction ftTransaction = new FtTransaction();
ftTransaction.setActionType(ActionType.TRANSFER);
ftTransaction.setAccountName("accout4testweb3j");
ftTransaction.setToAccountName("accout4testweb3j.test1");
ftTransaction.setAssetId(AssetId.FT);
ftTransaction.setAmount(new BigInteger("10000000000000000"));//0.01FT
//Send transaction
FtTransactionReceipt ftTransactionReceipt = ftTransactionManager.ftSendRawTransaction(ftTransaction, PayloadProvider.createTransferPayload());
System.out.println(ftTransactionReceipt);
```
