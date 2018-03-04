package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {

    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop         stop to which parsed arrivals are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException                when JSON response does not have expected format
     * @throws ArrivalsDataMissingException when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {
        JSONArray arrivals = new JSONArray(jsonResponse);

        for (int index = 0; index < arrivals.length(); index++) {
            JSONObject arrival = arrivals.getJSONObject(index);
            if (!arrival.has("RouteNo")) continue;
            String routeNo = arrival.getString("RouteNo");
            Route route = RouteManager.getInstance().getRouteWithNumber(routeNo);

            if (!arrival.has("Schedules")) continue;

            JSONArray schedules = arrival.getJSONArray("Schedules");
            for (int i = 0; i < schedules.length(); i++) {
                JSONObject schedule = schedules.getJSONObject(i);
                if (!schedule.has("ExpectedCountdown") || !schedule.has("Destination") || !schedule.has("ScheduleStatus")) {
                    continue;
                }


                if ((schedule.get("ExpectedCountdown").equals(null)) ||
                        (schedule.get("Destination").equals(null)) ||
                        (schedule.get("ScheduleStatus").equals(null)))
                    continue;

                int expectedCountdown = schedule.getInt("ExpectedCountdown");
                String destination = schedule.getString("Destination");
                String status = schedule.getString("ScheduleStatus");
                Arrival a = new Arrival(expectedCountdown, destination, route);
                a.setStatus(status);
                stop.addArrival(a);
            }
        }

        if (stop.getArrivals().size() == 0) {
            throw new ArrivalsDataMissingException();
        }

    }
}
