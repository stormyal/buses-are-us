package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;

    public RouteParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }
    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when JSON data does not have expected format
     * @throws RouteDataMissingException when
     * <ul>
     *  <li> JSON data is not an array </li>
     *  <li> JSON data is missing Name, StopNo, Routes or location elements for any stop</li>
     * </ul>
     */

    public void parseRoutes(String jsonResponse)
            throws JSONException, RouteDataMissingException {
        JSONArray array = new JSONArray(jsonResponse);

        for (int index = 0; index < array.length(); index++) {

            JSONObject aRoute = array.getJSONObject(index);
            if (!aRoute.has("Name") || !aRoute.has("RouteNo") || !aRoute.has("Patterns")) {
                throw new RouteDataMissingException();
            }
            String name = aRoute.getString("Name");
            String routeNumber = aRoute.getString("RouteNo");
            JSONArray patterns = aRoute.getJSONArray("Patterns");
            Route route = RouteManager.getInstance().getRouteWithNumber(routeNumber);
            route.setName(name);

            for (int i = 0; i < patterns.length(); i++) {
                JSONObject aPattern = patterns.getJSONObject(i);
                if (!aPattern.has("PatternNo") || !aPattern.has("Destination") || !aPattern.has("Direction")) {
                    throw new RouteDataMissingException();
                }
                String patternNumber = aPattern.getString("PatternNo");
                String destination = aPattern.getString("Destination");
                String direction = aPattern.getString("Direction");
                RoutePattern routePattern = new RoutePattern(patternNumber, destination, direction, route);
                route.addPattern(routePattern);
            }

        }

    }
}
