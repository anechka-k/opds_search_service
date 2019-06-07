package opdssearch.http;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public final class JSONResponses
{
  public static final JSONObject ERROR_INCORRECT_REQUEST;
  static
  {
    JSONObject response  = new JSONObject();
    try
    {
      response.put("result", "error");
      response.put("error", "Incorrect request");
    }
    catch (JSONException e)
    {
    }
      
    ERROR_INCORRECT_REQUEST = response;
  }

  public static final JSONObject POST_REQUIRED;
  static
  {
    JSONObject response = new JSONObject();
      
    try
    {
      response.put("result", "error");
      response.put("error", "POST required");
    }
    catch (JSONException e)
    {
    }
      
    POST_REQUIRED = response;
  }
  
  public static final JSONObject UNKNOWN_COMMAND;
  static
  {
    JSONObject response = new JSONObject();
      
    try
    {
      response.put("result", "error");
      response.put("error", "Unknown command");
    }
    catch (JSONException e)
    {
    }
      
    UNKNOWN_COMMAND = response;
  }

  private JSONResponses() {} // never
}
