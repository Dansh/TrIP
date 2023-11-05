package com.example.trip;
import java.util.HashMap;

public class TripData

{
    private static TripData instance = null;
    public static HashMap<String, String> tripDataHashMap;

    private TripData(){
        tripDataHashMap = new HashMap<String, String>();
    }

    public static TripData getInstance()
    {
        if (instance == null)
        {
            instance = new TripData();
        }
        return instance;
    }


    public void addAnswer(String title, String answer){
        tripDataHashMap.put(title, answer);
    }

    public String getAnswer(String title){
        return tripDataHashMap.get(title);
    }


}
