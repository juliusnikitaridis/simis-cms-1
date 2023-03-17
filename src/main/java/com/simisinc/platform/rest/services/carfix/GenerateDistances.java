package com.simisinc.platform.rest.services.carfix;

public class GenerateDistances {

    public static void main(String[]args) {
        // -33.87149194907947, 18.61520205628264
        //-33.87227585316099, 18.644599065910324
        System.out.println(GeoUtils.distanceInKilometers(-33.87149194907947,18.61520205628264,-33.87227585316099,18.644599065910324));
        System.out.println(GeoUtils.distanceInKilometers(-33.87227585316099,18.644599065910324,-33.87149194907947,18.61520205628264));
    }

}


 class GeoUtils {
    public GeoUtils() {
    }

    public static double distanceInMiles(double var0, double var2, double var4, double var6) {
        return radiansTodegrees(Math.acos(Math.sin(degreesToRadians(var0)) * Math.sin(degreesToRadians(var4)) + Math.cos(degreesToRadians(var0)) * Math.cos(degreesToRadians(var4)) * Math.cos(degreesToRadians(var2 - var6)))) * 60.0D * 1.1515D;
    }

    public static double distanceInKilometers(double var0, double var2, double var4, double var6) {
        return distanceInMiles(var0, var2, var4, var6) * 1.609344D;
    }

    public static double radiansTodegrees(double var0) {
        return var0 * 180.0D / 3.141592653589793D;
    }

    public static double degreesToRadians(double var0) {
        return var0 * 3.141592653589793D / 180.0D;
    }
}
