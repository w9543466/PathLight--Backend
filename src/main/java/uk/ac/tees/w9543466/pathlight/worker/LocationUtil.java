package uk.ac.tees.w9543466.pathlight.worker;

public class LocationUtil {
    public static boolean doesLocationMatch(double lat1, double lng1, double lat2, double lng2, double radius) {
        var distance = distanceBetween(lat1, lng1, lat2, lng2);
        System.out.println("distance between worker and the work = " + distance);
        System.out.println("radius of the worker preferred work location = " + radius);
        return distance <= radius;
    }

    /**
     * @return the distance in meters
     */
    public static double distanceBetween(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371;// earth's radius

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;
    }
}
