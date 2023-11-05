package com.example.trip;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TripPlanRequestManager extends AsyncTask<Void, Void, String> {
    private static final String TAG = TripPlanRequestManager.class.getSimpleName();

    private String url;
    private OnTripPlanResponseListener responseListener;

    public TripPlanRequestManager(String url, OnTripPlanResponseListener responseListener) {
        this.url = url;
        this.responseListener = responseListener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // Create a URL object from the specified URL
            URL requestUrl = new URL(url);
            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            // Set the request method to GET
            connection.setRequestMethod("GET");
            // Get the response code
            int responseCode = connection.getResponseCode();
            // Check if the request was successful
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the connection's input stream
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                // Return the response
                return response.toString();
            } else {
                // Print the error response if the request was not successful
                Log.e(TAG, "HTTP request failed with response code: " + responseCode);
            }
            // Disconnect the connection
            connection.disconnect();
        } catch (IOException e) {
            System.out.println("hi");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (response != null) {
            // Pass the response to the response listener
            responseListener.onTripPlanResponse(response);
        } else {
            // Handle the case when response is null
            responseListener.onTripPlanError("HTTP request failed");
        }
    }

    public interface OnTripPlanResponseListener {
        void onTripPlanResponse(String response);

        void onTripPlanError(String error);
    }
}