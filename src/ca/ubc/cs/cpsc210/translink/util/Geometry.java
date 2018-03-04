package ca.ubc.cs.cpsc210.translink.util;

import java.awt.*;

/**
 * Compute relationships between points, lines, and rectangles represented by LatLon objects
 */
public class Geometry {
    /**
     * Return true if the point is inside of, or on the boundary of, the rectangle formed by northWest and southeast
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param point             the point in question
     * @return                  true if the point is on the boundary or inside the rectangle
     */
    public static boolean rectangleContainsPoint(LatLon northWest, LatLon southEast, LatLon point) {
        double lat1 = northWest.getLatitude();
        double lon1 = northWest.getLongitude();

        double lat2 = southEast.getLatitude();
        double lon2 = southEast.getLongitude();

        double lat3 = point.getLatitude();
        double lon3 = point.getLongitude();

        return between(lat2, lat1, lat3) && between(lon1, lon2, lon3);
    }

    /**
     * Return true if the rectangle intersects the line
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param src               one end of the line in question
     * @param dst               the other end of the line in question
     * @return                  true if any point on the line is on the boundary or inside the rectangle
     */
    public static boolean rectangleIntersectsLine(LatLon northWest, LatLon southEast, LatLon src, LatLon dst) {
        if (rectangleContainsPoint(northWest, southEast, src) || rectangleContainsPoint(northWest, southEast, dst)) {
            return true;
        }

        double srcLat = src.getLatitude();
        double srcLon = src.getLongitude();
        double dstLat = dst.getLatitude();
        double dstLon = dst.getLongitude();

        double lat1 = northWest.getLatitude();
        double lon1 = northWest.getLongitude();

        double lat2 = southEast.getLatitude();
        double lon2 = southEast.getLongitude();

//        Line2D line = new Line2D.Double(srcLon,srcLat, dstLon, dstLat);
//        Rectangle2D rectangle = new Rectangle2D.Double(lon1, lat1, lon2 - lon1, lat1 - lat2);
//
//        return line.intersects(rectangle);

        if ((srcLat > lat1 && dstLat > lat1) || (srcLat < lat2 && dstLat < lat2) ||
                (srcLon > lon2 && dstLon > lon2) || (srcLon < lon1 && dstLon < lon1)) {
            return false;
        }
        if ((srcLat == dstLat) || (srcLon == dstLon)) return true;

        double slope = (dstLat - srcLat) / (dstLon - srcLon);
        double b = srcLat - slope * srcLon;

        double topLine = (lat1 - b) / slope;
        double bottomLine = (lat2 - b) / slope;
        double leftLine = slope * lon1 + b;
        double rightLine = slope * lon2 + b;

        return between(lon1, lon2, topLine) || between(lon1, lon2, bottomLine)
                || between(lat2, lat1, leftLine) || between(lat2, lat1, rightLine);


    }

    /**
     * A utility method that you might find helpful in implementing the two previous methods
     * Return true if x is >= lwb and <= upb
     * @param lwb      the lower boundary
     * @param upb      the upper boundary
     * @param x         the value in question
     * @return          true if x is >= lwb and <= upb
     */
    private static boolean between(double lwb, double upb, double x) {
        return lwb <= x && x <= upb;
    }
}
