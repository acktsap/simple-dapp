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

  /**
   * Define a smart contract.
   * 
   * @param code smart contract definition code
   * @param contractPrivatekey contract private key to sign on transaction
   * @throws IOException when building or sending transaction failed
   * @throws CoinStackException when any of coinstack processing failed
   */
  public void define(final String code, final String contractPrivatekey)
      throws IOException, CoinStackException {
    final LuaContractBuilder lcBuilder = new LuaContractBuilder();
    lcBuilder.setContractId(getContractAddress());
    lcBuilder.setFee(FEE);
    lcBuilder.setDefinition(code);

    String rawTx = lcBuilder.buildTransaction(getClient(), contractPrivatekey);
    getClient().sendTransaction(rawTx);

    String txHash = TransactionUtil.getTransactionHash(rawTx);
    System.out.println("=== Function definition ===");
    System.out.println("contract private key : " + contractPrivatekey);
    System.out.println("contract id : " + getContractAddress());
    System.out.println("tx : " + txHash);
    System.out.println("code :");
    System.out.println(code);
  }

  /**
   * Execute a smart contract.
   * 
   * @param code smart contract execution code
   * @param senderPrivatekey sender private key to sign on transaction
   * @throws IOException when building or sending transaction failed
   * @throws CoinStackException when any of coinstack processing failed
   */
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
    System.out.println("sender : " + senderPrivatekey);
    System.out.println("contract id : " + getContractAddress());
    System.out.println("tx : " + txHash);
    System.out.println("code :");
    System.out.println(code);
  }

  /**
   * Query a state made by smart contract.
   * 
   * @param code query code.
   * @return query result in json format
   * @throws IOException when building or sending transaction failed
   * @throws CoinStackException when any of coinstack processing failed
   * @throws JSONException when json processing failed
   */
  public JSONObject query(final String code) throws IOException, CoinStackException, JSONException {
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
