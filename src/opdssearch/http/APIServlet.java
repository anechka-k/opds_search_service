package opdssearch.http;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import opdssearch.util.Convert;
import opdssearch.util.Logger;

public class APIServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  static final Map<String,APIRequestHandler> apiRequestHandlers;
  
  abstract static class APIRequestHandler
  {
    private final List<String> parameters;

    APIRequestHandler(String... parameters)
    {
      this.parameters = Collections.unmodifiableList(Arrays.asList(parameters));
    }

    final List<String> getParameters()
    {
      return parameters;
    }

    abstract JSONObject processRequest(HttpServletRequest request) throws Exception;
  }
  
  static
  {
    Map<String,APIRequestHandler> map = new HashMap<>();
    
    map.put("searchBook", SearchBookHandler.instance);
    
    apiRequestHandlers = Collections.unmodifiableMap(map);
  }
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    process(req, resp);
  }
  
  private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException 
  {
    resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, private");
    resp.setHeader("Pragma", "no-cache");
    resp.setDateHeader("Expires", 0);

    JSONObject response = new JSONObject();

    try
    {
      if (! "POST".equals(req.getMethod()))
      {
        response = JSONResponses.POST_REQUIRED;
        return;
      }
      
      String command = Convert.emptyToNull(req.getParameter("request"));
      APIRequestHandler apiRequestHandler = apiRequestHandlers.get(command);
      
      if(apiRequestHandler == null)
      {
        response = JSONResponses.UNKNOWN_COMMAND;
        return;
      }
      
      response = apiRequestHandler.processRequest(req);
    }
    catch (Exception e) 
    {
      Logger.logMessage("Error processing API request", e);
      response = JSONResponses.ERROR_INCORRECT_REQUEST;
    }
    finally
    {
    	resp.setContentType("application/json");
      try (Writer writer = resp.getWriter())
      {
        response.write(writer);
      }
      catch (JSONException e)
      {
        Logger.logMessage("Error writing JSON", e);
      }
    }
  }
}
