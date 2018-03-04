package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for routes stored in a compact format in a txt file
 */
public class RouteMapParser {
    private String fileName;

    public RouteMapParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parse the route map txt file
     */
    public void parse() {
        DataProvider dataProvider = new FileDataProvider(fileName);
        try {
            String c = dataProvider.dataSourceToString();
            if (!c.equals("")) {
                int posn = 0;
                while (posn < c.length()) {
                    int endposn = c.indexOf('\n', posn);
                    String line = c.substring(posn, endposn);
                    parseOnePattern(line);
                    posn = endposn + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one route pattern, adding it to the route that is named within it
     * @param str
     */
    private void parseOnePattern(String str) {
        String routeNumber = "";
        String patternName = "";
        List<LatLon> elements = new ArrayList<>();
        String newString = "";
        String lat = "";
        String lon = "";

        if (!str.isEmpty() && str.startsWith("N")) {
            routeNumber = str.substring(1, str.indexOf("-"));
            patternName = str.substring(str.indexOf("-") + 1, str.indexOf(";"));
            newString = str.substring(str.indexOf(";") + 1);

            while (!newString.isEmpty()) {
                lat = newString.substring(0, newString.indexOf(";"));
                newString = newString.substring(newString.indexOf(";") + 1);
                lon = newString.substring(0, newString.indexOf(";"));
                newString = newString.substring(newString.indexOf(";") + 1);
                elements.add(new LatLon(Double.parseDouble(lat), Double.parseDouble(lon)));
            }

            storeRouteMap(routeNumber, patternName, elements);
        }

    }

    /**
     * Store the parsed pattern into the named route
     * Your parser should call this method to insert each route pattern into the corresponding route object
     * There should be no need to change this method
     *
     * @param routeNumber       the number of the route
     * @param patternName       the name of the pattern
     * @param elements          the coordinate list of the pattern
     */
    private void storeRouteMap(String routeNumber, String patternName, List<LatLon> elements) {
        Route r = RouteManager.getInstance().getRouteWithNumber(routeNumber);
        RoutePattern rp = r.getPattern(patternName);
        rp.setPath(elements);
    }
}
