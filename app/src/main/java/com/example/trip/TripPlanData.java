package com.example.trip;
import java.util.HashMap;
import java.util.Map;

public class TripPlanData

{
    private static TripPlanData instance = null;
    private static Map<String, Object> planHashMap;

    private TripPlanData(){
        planHashMap = new HashMap<>();
    }

    public static TripPlanData getInstance()
    {
        if (instance == null)
        {
            instance = new TripPlanData();
        }
        return instance;
    }

    public Map<String, Object> getHashMap()
    {
        return planHashMap;
    }

    public void setHashMap(Map<String, Object> newHashMap)
    {
        planHashMap = newHashMap;
    }

    public void add(String key, Object val){
        planHashMap.put(key, val);
    }

    public Object get(Object key)
    {
        return planHashMap.get(key);
    }

    public Map getDays()
    {
        return (Map) planHashMap.get("days");
    }


}