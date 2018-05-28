package io.blocko.simpledapp;

import static io.blocko.simpledapp.ServletConstants.CONTRACT_ADDRESS;
import static io.blocko.simpledapp.ServletConstants.ENDPOINT;

import io.blocko.json.JSONObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/market")
public class MarketServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setAttribute("defaultContractId", CONTRACT_ADDRESS);
    req.setAttribute("market", queryMarketState());
    req.getRequestDispatcher("market.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    final String senderPrivatekey = req.getParameter("sender");
    final String contractId = req.getParameter("contractId");
    final String uuid = req.getParameter("uuid");
    
    final String buttonType = req.getParameter("buttonType");
    String execCode = "";
    if ("Buy".equals(buttonType)) {
      execCode = LuaCodeUtils.makeExecutionCode("buy", uuid);
    } else if ("Confirm".equals(buttonType)) {
      execCode = LuaCodeUtils.makeExecutionCode("confirm", uuid);
    }

    try {
      SmartContract smartContract = new SmartContract(ENDPOINT, contractId);
      smartContract.execute(execCode, senderPrivatekey);
    } catch (Exception e) {
      e.printStackTrace();
    }

    req.setAttribute("market", queryMarketState());
    req.getRequestDispatcher("market.jsp").forward(req, resp);
  }

  protected String queryMarketState() {
    String ret = null;
    try {
      final String queryCode = LuaCodeUtils.makeQueryCode("showMarket");
      SmartContract smartContract = new SmartContract(ENDPOINT, CONTRACT_ADDRESS);
      JSONObject result = smartContract.query(queryCode);
      JSONObject market = result.getJSONObject("result");
      ret = market.toString(2);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

}
