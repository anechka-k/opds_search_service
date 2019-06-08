package opdssearch.http;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import opdssearch.flibusta.FlibustaClient;
import opdssearch.util.Convert;

public final class SearchBookHandler extends APIServlet.APIRequestHandler
{
  static final SearchBookHandler instance = new SearchBookHandler();

  private SearchBookHandler()
  {
  }

  @Override
  JSONObject processRequest(HttpServletRequest req) throws JSONException
  {    
    String searchQuery = Convert.emptyToNull(req.getParameter("query"));
    JSONObject books = FlibustaClient.searchBooks(searchQuery);
    return books;
  }
}
