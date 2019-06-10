package com.gupta54622.rahul.indainerails;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {

        // create an empty StringBuilder "result", this will be used to store the returned data
        StringBuilder result = new StringBuilder();

        // Create an url
        URL url;

        // Crete an object of HttpURLConnection
        HttpURLConnection httpURLConnection = null;

        try {

            url = new URL(urls[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int data = inputStreamReader.read();

            while (data != -1) {
                result.append((char) data);
                data = inputStreamReader.read();
            }

            return String.valueOf(result);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Convert the result string into JSON object


        try {
            JSONObject jsonObject = new JSONObject(result);

            Log.i("JSON", result);
            MainActivity.progressBar.setVisibility(View.INVISIBLE);
            MainActivity.floatingActionButtonCancelChecking.setVisibility(View.INVISIBLE);

            MainActivity.buttonCheckStatus.setText("Check Status");
            MainActivity.buttonCheckStatus.setEnabled(true);

            JSONArray jsonArray = jsonObject.getJSONArray("passengers");
            JSONObject jsonObjectPassenger = jsonArray.getJSONObject(0);

            String desiredResult = "STATUS\n\n";

            desiredResult += "Booking Status: " + jsonObjectPassenger.getString("booking_status") + "\n\n";
            desiredResult += "Current Status: " + jsonObjectPassenger.getString("current_status") + "\n\n";
            desiredResult += "Journy Date: " + jsonObject.getString("doj");

            MainActivity.textViewStatus.setText(desiredResult);
            MainActivity.cardView.setVisibility(View.VISIBLE);
            MainActivity.textViewStatus.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
