package ex03.pyrmont.startup;

import ex03.pyrmont.connector.http.HttpConnector;

public final class Bootstrap {
  public static void main(String[] args) {
    // HTTP连接器
    HttpConnector connector = new HttpConnector();
    connector.start();
  }
}