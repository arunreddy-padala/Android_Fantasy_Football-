package edu.neu.cs5520sp22.fantasyleaguecompanion.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class Converters {
    @TypeConverter
    public static ArrayList<String> fromStringArrayList(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<String>>() {}.getType());
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static HashMap<String, Integer> fromStringHashMap(String value){
        return new Gson().fromJson(value, new TypeToken<HashMap<String, Integer>>() {}.getType());
    }

    @TypeConverter
    public static String fromHashMap(HashMap<String, Integer> map){
        return new Gson().toJson(map);
    }
}
