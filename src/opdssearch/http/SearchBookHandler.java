package opdssearch.http;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public final class SearchBookHandler extends APIServlet.APIRequestHandler
{
  static final SearchBookHandler instance = new SearchBookHandler();

  private SearchBookHandler()
  {
  }

  @Override
  JSONObject processRequest(HttpServletRequest req) throws JSONException
  {
    JSONObject answer = new JSONObject();

    answer.put("result", "error");
    answer.put("error", "not implemented");
    return answer;
  }
}
