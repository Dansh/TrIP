package com.example.trip;

public class URL {

    private static String baseUrl = "https://trip-api-387923.lm.r.appspot.com/?";

    public static String getFullUrl(){
        TripData tripData = TripData.getInstance();

        String parametersUrl = "destination=" + tripData.getAnswer("Destination") +
                "&duration=" + tripData.getAnswer("Duration") +
                "&budget=" + tripData.getAnswer("Budget") +
                "&interests=" + tripData.getAnswer("Interests") +
                "&accommodationpref=" + tripData.getAnswer("Accommodation preferences") +
                "&transportation=" + tripData.getAnswer("Transportation") +
                "&travelcomp=" + tripData.getAnswer("Travel companions") +
                "&specialreq=" + tripData.getAnswer("Special requirements") +
                "&season=" + tripData.getAnswer("Season") +
                "&additionalinfo=" + tripData.getAnswer("Any additional information");


        parametersUrl = parametersUrl.replace(' ', '+');
        System.out.println(baseUrl + parametersUrl);

        return baseUrl + parametersUrl;
    }
}
