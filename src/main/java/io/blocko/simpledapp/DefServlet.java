package io.blocko.simpledapp;

import static io.blocko.simpledapp.ServletConstants.CONTRACT_ADDRESS;
import static io.blocko.simpledapp.ServletConstants.ENDPOINT;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/def")
public class DefServlet extends HttpServlet {
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setAttribute("defaultContractId", CONTRACT_ADDRESS);
    RequestDispatcher view = req.getRequestDispatcher("def.jsp");
    view.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    final String creatorPrivatekey = req.getParameter("creator");
    final String contractId = req.getParameter("contractId");
    final String code = req.getParameter("code");

    try {
      SmartContract smartContract = new SmartContract(ENDPOINT, contractId);
      smartContract.define(code, creatorPrivatekey);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    req.getRequestDispatcher("def.jsp").forward(req, resp);
  }

}
