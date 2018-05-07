package io.blocko.simpledapp;

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

    final String issueCode = makeCode("issue", uuid, goodsName, price);

    try {
      SmartContract smartContract = new SmartContract(ENDPOINT, contractId);
      smartContract.execute(issueCode, senderPrivatekey);
    } catch (Exception e) {
      e.printStackTrace();
    }

    req.getRequestDispatcher("issue.jsp").forward(req, resp);
  }

  /**
   * Make a code with function name and arguments.
   * 
   * @param funcName function name
   * @param args arguments 
   * @return generated code
   */
  protected String makeCode(final String funcName, final Object... args) {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("ret, ok = call(");
    buffer.append('\"');
    buffer.append(funcName);
    buffer.append('\"');
    for (final Object arg : args) {
      buffer.append(", ");
      if (arg instanceof Integer) {
        buffer.append(arg);
      } else { // String
        buffer.append('\"');
        buffer.append((String) arg);
        buffer.append('\"');
      }
    }
    buffer.append("); assert(ok, ret)");
    return buffer.toString();
  }

}
