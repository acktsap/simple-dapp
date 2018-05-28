package io.blocko.simpledapp;

import static io.blocko.simpledapp.ServletConstants.CONTRACT_ADDRESS;
import static io.blocko.simpledapp.ServletConstants.ENDPOINT;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/issue")
public class IssueServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setAttribute("defaultContractId", CONTRACT_ADDRESS);
    RequestDispatcher view = req.getRequestDispatcher("issue.jsp");
    view.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    final String senderPrivatekey = req.getParameter("sender");
    final String contractId = req.getParameter("contractId");
    final String uuid = UUID.randomUUID().toString();
    final String goodsName = req.getParameter("goodsName");
    final Integer price = Integer.valueOf(req.getParameter("price"));

    final String issueCode = LuaCodeUtils.makeExecutionCode("issue", uuid, goodsName, price);

    try {
      SmartContract smartContract = new SmartContract(ENDPOINT, contractId);
      smartContract.execute(issueCode, senderPrivatekey);
    } catch (Exception e) {
      e.printStackTrace();
    }

    req.getRequestDispatcher("issue.jsp").forward(req, resp);
  }

}
