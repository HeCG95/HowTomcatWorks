import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class PrimitiveServlet implements Servlet {

  public void init(ServletConfig config) throws ServletException {
    System.out.println("init");
  }

  public void service(ServletRequest request, ServletResponse response)
    throws ServletException, IOException {
    System.out.println("from service");
    PrintWriter out = response.getWriter();

    out.print("HTTP/1.1 200 OK\r\n");
    out.print("Content-Type: text/html\r\n");
    out.print("\r\n");

    out.println("Hello. Roses are red.");
    out.print("Violets are blue.");// 不打印
  }

  public void destroy() {
    System.out.println("destroy");
  }

  public String getServletInfo() {
    return null;
  }
  public ServletConfig getServletConfig() {
    return null;
  }

}
