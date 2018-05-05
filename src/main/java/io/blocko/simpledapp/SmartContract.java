package io.blocko.simpledapp;

import io.blocko.coinstack.BasicEndpoint;
import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.LuaContractBuilder;
import io.blocko.coinstack.TransactionUtil;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.exception.MalformedInputException;
import io.blocko.coinstack.model.ContractBody;
import io.blocko.coinstack.model.ContractResult;
import io.blocko.json.JSONException;
import io.blocko.json.JSONObject;
import java.io.IOException;

public class SmartContract {

  private static int PRINT_INDENT = 2;

  private static long FEE = 10000L;

  private CoinStackClient client;

  // same as contract id
  private String contractAddress;

  public SmartContract(final String endpoint, final String contractAddress)
      throws MalformedInputException {
    setClientWithEndpoint(endpoint);
    setContractAddress(contractAddress);
  }

  public CoinStackClient getClient() {
    return client;
  }

  public void setClientWithEndpoint(final String endpoint) {
    this.client = new CoinStackClient(null, null, new BasicEndpoint(endpoint));
  }

  private void setContractAddress(String contractAddress) {
    this.contractAddress = contractAddress;
  }

  public String getContractAddress() {
    return contractAddress;
  }

  public void define(final String code, final String creatorPrivatekey)
      throws IOException, CoinStackException {
    final LuaContractBuilder lcBuilder = new LuaContractBuilder();
    lcBuilder.setContractId(getContractAddress());
    lcBuilder.setFee(FEE);
    lcBuilder.setDefinition(code);

    String rawTx = lcBuilder.buildTransaction(getClient(), creatorPrivatekey);
    getClient().sendTransaction(rawTx);

    String txHash = TransactionUtil.getTransactionHash(rawTx);
    System.out.println("=== Function definition ===");
    System.out.println("creator : " + creatorPrivatekey);
    System.out.println("contract id : " + getContractAddress());
    System.out.println("tx : " + txHash);
    System.out.println("code :");
    System.out.println(code);
  }

  public void execute(final String code, final String senderPrivatekey)
      throws IOException, CoinStackException {
    final LuaContractBuilder lcBuilder = new LuaContractBuilder();
    lcBuilder.setContractId(getContractAddress());
    lcBuilder.setFee(FEE);
    lcBuilder.setExecution(code);

    String rawTx = lcBuilder.buildTransaction(getClient(), senderPrivatekey);
    getClient().sendTransaction(rawTx);

    String txHash = TransactionUtil.getTransactionHash(rawTx);
    System.out.println("=== Function execution ===");
    System.out.println("creator : " + senderPrivatekey);
    System.out.println("contract id : " + getContractAddress());
    System.out.println("tx : " + txHash);
    System.out.println("code :");
    System.out.println(code);
  }

  public JSONObject query(final String code) throws CoinStackException, IOException, JSONException {
    final ContractResult contractResult =
        getClient().queryContract(getContractAddress(), ContractBody.TYPE_LSC, code);
    JSONObject queryResult = null;
    if (null != contractResult) {
      queryResult = contractResult.asJson();
    }

    System.out.println("=== Query ===");
    System.out.println("contract id : " + getContractAddress());
    System.out.println("code:");
    System.out.println(code);
    System.out.println("result:");
    System.out.println(queryResult.toString(PRINT_INDENT));

    return queryResult;
  }

}
