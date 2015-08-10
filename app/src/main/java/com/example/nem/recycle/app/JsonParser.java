package com.example.nem.recycle.app;


import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Класс, отвечающий за парсинг JSON и заполнение массива данными.
 */
public class JsonParser extends AsyncTask<Void, Void, List<Person>>{
    private List<Person> persons = new ArrayList<>();
    HttpURLConnection connection;
    BufferedReader reader;
    String resultJson;

    @Override
    protected List<Person> doInBackground (Void... params) {

        try {
            URL url = new URL("http://109.120.187.164:81/people.json");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            resultJson = buffer.toString();
        }catch (MalformedURLException e){
            Log.d("tag", "URL problem");
        }catch (IOException e){
            Log.d("tag", "connection problem");
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }


        JSONArray jsonArrayPerson = null;
        try {
            jsonArrayPerson = new JSONArray(resultJson);
        }catch (JSONException e){
            Log.d("err", "JSON error");
            e.printStackTrace();
        }
        for (int i = 0; i < jsonArrayPerson.length(); i++){
            try {
                persons.add(getPersonFromJson(jsonArrayPerson.getJSONObject(i)));
            }catch (JSONException e){
                Log.d("err", "JSON get error");
                e.printStackTrace();
            }
        }
        return persons;
    }

    // Создание объекта Person. Id есть у всех, остальное - опционально.
    private Person getPersonFromJson(JSONObject object) throws JSONException{
        Person person = new Person();
        person.setId(object.getString("_id"));
        person.setIsActive(object.optBoolean("isActive"));
        person.setPicture(object.optString("picture"));
        person.setAge(object.optInt("age"));
        person.setName(object.optString("name"));
        person.setGender(object.optString("gender"));
        person.setEmail(object.optString("email"));
        person.setPhone(object.optString("phone"));
        person.setAddress(object.optString("address"));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss zzzzz", Locale.ENGLISH);
        try {
            person.setRegistered(format.parse(object.optString("registered")));
        }catch (ParseException e){
            person.setRegistered(new Date(0));
        }
        return person;
    }


}
