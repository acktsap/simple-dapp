package io.blocko.simpledapp;

import io.blocko.coinstack.BasicEndpoint;
import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.ECKey;
import io.blocko.coinstack.LuaContractBuilder;
import io.blocko.coinstack.TransactionUtil;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.exception.MalformedInputException;
import io.blocko.coinstack.model.ContractBody;
import io.blocko.coinstack.model.ContractResult;
import io.blocko.json.JSONException;
import io.blocko.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SmartContract {
  
  private static int PRINT_INDENT = 2;

  private static long FEE = 10000L;

  protected CoinStackClient client;

  protected String contractAddress;

  protected String contractPrivatekey;

  public SmartContract(final String endpoint, final String privatekey)
      throws MalformedInputException {
    this.client = new CoinStackClient(null, null, new BasicEndpoint(endpoint));
    this.contractAddress = ECKey.deriveAddress(privatekey);
    this.contractPrivatekey = privatekey;
  }

  public void define(final String code) throws IOException, CoinStackException {
    final LuaContractBuilder lcBuilder = new LuaContractBuilder();
    lcBuilder.setContractId(contractAddress);
    lcBuilder.setDefinition(code);

    String rawTx = lcBuilder.buildTransaction(client, contractPrivatekey);
    client.sendTransaction(rawTx);
    String txHash = TransactionUtil.getTransactionHash(rawTx);

    System.out.println("=== Function definition ===");
    System.out.println("tx: " + txHash);
    System.out.println("code:");
    System.out.println(code);
  }

  public void execute(final String code) throws IOException, CoinStackException {
    LuaContractBuilder lcBuilder = new LuaContractBuilder();
    lcBuilder.setContractId(contractAddress);
    lcBuilder.setFee(FEE);
    lcBuilder.setExecution(code);

    String rawTx = lcBuilder.buildTransaction(client, contractPrivatekey);
    String txHash = TransactionUtil.getTransactionHash(rawTx);
    client.sendTransaction(rawTx);

    System.out.println("=== Function execution ===");
    System.out.println("tx: " + txHash);
    System.out.println("code:");
    System.out.println(code);
  }

  public JSONObject query(final String code) throws CoinStackException, IOException, JSONException {
    ContractResult contractResult =
        client.queryContract(contractAddress, ContractBody.TYPE_LSC, code);
    JSONObject queryResult = null;
    if (null != contractResult) {
      queryResult = contractResult.asJson();
    }

    System.out.println("=== Query ===");
    System.out.println("code:");
    System.out.println(code);
    System.out.println("result:");
    System.out.println(queryResult.toString(PRINT_INDENT));

    return queryResult;
  }

}
