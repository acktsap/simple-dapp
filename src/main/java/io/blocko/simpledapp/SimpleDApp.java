package io.blocko.simpledapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SimpleDApp {

  private static final String endpoint = "http://localhost:3000";
  private static final String privateKey = "KyQxDqRvUYAKnMHN3PvHwQUwpQvtu5Bz2MDARaNBZiTsdTRAaj5e";

  private static final String DEFINITION_FILE = "./src/main/lua/def.lua";
  private static final String EXECUTION_FILE = "./src/main/lua/exec.lua";
  private static final String QUERY_FILE = "./src/main/lua/query.lua";
  
  public static void main(String[] args) throws Exception {
    SmartContract smartContract = new SmartContract(endpoint, privateKey);
    smartContract.define(readContentFrom(DEFINITION_FILE));
    smartContract.execute(readContentFrom(EXECUTION_FILE));
    smartContract.query(readContentFrom(QUERY_FILE));
  }

  public static String readContentFrom(final String fileName) throws IOException {
    final StringBuilder content = new StringBuilder();
    final BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
    String newLine;
    while ((newLine = bufferedReader.readLine()) != null) {
      content.append(newLine + "\n");
    }
    bufferedReader.close();
    return content.toString();
  }

}
