package ex05.pyrmont.core;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.Contained;
import org.apache.catalina.Container;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.Valve;
import org.apache.catalina.ValveContext;

public class SimplePipeline implements Pipeline {

  public SimplePipeline(Container container) {
    setContainer(container);
  }

  // The basic Valve (if any) associated with this Pipeline.
  protected Valve basic = null;
  // The Container with which this Pipeline is associated.
  protected Container container = null;
  // the array of Valves
  protected Valve valves[] = new Valve[0];

  public void setContainer(Container container) {
    this.container = container;
  }

  public Valve getBasic() {
    return basic;
  }

  public void setBasic(Valve valve) {
    this.basic = valve;
    ((Contained) valve).setContainer(container);
  }

  public void addValve(Valve valve) {
    if (valve instanceof Contained)
      ((Contained) valve).setContainer(this.container);

    synchronized (valves) {
      Valve results[] = new Valve[valves.length +1];
      System.arraycopy(valves, 0, results, 0, valves.length);
      results[valves.length] = valve;
      valves = results;
    }
  }

  public Valve[] getValves() {
    return valves;
  }

  /**
   * 调用管道的阀门
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   *
   * @throws IOException
   * @throws ServletException
   */
  public void invoke(Request request, Response response)
    throws IOException, ServletException {
    // Invoke the first Valve in this pipeline for this request
    (new SimplePipelineValveContext()).invokeNext(request, response);
  }

  public void removeValve(Valve valve) {
  }

  // this class is copied from org.apache.catalina.core.StandardPipeline class's
  // StandardPipelineValveContext inner class.
  protected class SimplePipelineValveContext implements ValveContext {

    protected int stage = 0;

    public String getInfo() {
      return null;
    }

    public void invokeNext(Request request, Response response)
      throws IOException, ServletException {
      int subscript = stage;
      stage = stage + 1;
      // Invoke the requested Valve for the current request thread
      if (subscript < valves.length) {
        System.out.println();
        System.out.println("-----------------------------------------------------------");
        System.out.println(Thread.currentThread().getName()+" >>> subscript: "+subscript +" stage: "+stage);
        System.out.println(Thread.currentThread().getName()+" >>> valves[subscript] - "+valves[subscript].getClass().getName()+" <<< Prepare invoke");

        valves[subscript].invoke(request, response, this);

        System.out.println(Thread.currentThread().getName()+" >>> valves[subscript] - "+valves[subscript].getClass().getName()+" <<< Finish invoke");
        System.out.println("-----------------------------------------------------------");
        System.out.println();
      }
      else if ((subscript == valves.length) && (basic != null)) {
        System.out.println();
        System.out.println("-----------------------------------------------------------");
        System.out.println(Thread.currentThread().getName()+" >>> subscript: "+subscript +" stage: "+stage);
        System.out.println(Thread.currentThread().getName()+" >>> basic - "+basic.getClass().getName()+" <<< Prepare invoke");

        basic.invoke(request, response, this);

        System.out.println(Thread.currentThread().getName()+" >>> basic - "+basic.getClass().getName()+" <<< Finish invoke");
        System.out.println("-----------------------------------------------------------");
        System.out.println();
      }
      else {
        throw new ServletException("No valve");
      }
    }
  } // end of inner class

}