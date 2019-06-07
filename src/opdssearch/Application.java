package opdssearch;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import opdssearch.http.APIServlet;
import opdssearch.util.Logger;

public class Application
{
  public static final String packageName = Application.class.getPackage().getName();
  
  public static final String version = AppConstants.APP_VERSION;
  public static Boolean terminated = false;
  
  public static int apiPort = AppProperties.getIntProperty("apiPort");
  public static String apiHost = "localhost";
  
  public static void main(String[] args)
  {
    Logger.logMessage(packageName + " " + version);
    
    terminated = false;
    
    Server apiServer = new Server();
    // HTTP Configuration
    HttpConfiguration http_config = new HttpConfiguration();
    
    ServerConnector httpConnector = new ServerConnector(apiServer, new HttpConnectionFactory(http_config));
    httpConnector.setName("unsecured"); // named connector
    httpConnector.setHost(apiHost);
    httpConnector.setPort(apiPort);
    
    ServletContextHandler siteHandler = new ServletContextHandler();
    
    //API servlet
    siteHandler.addServlet(APIServlet.class, "/api");
    
    //allow CORS
    FilterHolder filterHolder = siteHandler.addFilter(CrossOriginFilter.class, "/*", null);
    filterHolder.setInitParameter("allowedHeaders", "*");
    filterHolder.setAsyncSupported(true);
    
    HandlerList apiHandlers = new HandlerList();
    apiHandlers.addHandler(siteHandler);
    
    apiServer.setConnectors(new Connector[] { httpConnector });    
    apiServer.setHandler(apiHandlers);
    apiServer.setStopAtShutdown(true);
    
    try
    {
      apiServer.start();
    }
    catch (Exception e)
    {
      Logger.logMessage("Could not start API server", e);
      return;
    }
    
    Logger.logMessage("Started API server at " + apiHost + ":" + apiPort);
    
    try
    {
      while(true)
      {
        if(terminated) break;
        Thread.sleep(1000);
      }
    } 
    catch (InterruptedException e)
    {
      Logger.logWarningMessage(packageName + " terminated");
      terminated = true;
    }
    
    try
    {
      apiServer.stop();
    }
    catch (Exception e)
    {
      Logger.logMessage("Could not stop API server", e);
    }
    
    Logger.logWarningMessage(packageName + " stopped");
  }
}
