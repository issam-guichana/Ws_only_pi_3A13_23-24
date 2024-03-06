package services;


import java.io.IOException;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class SmsSender {

    public static boolean sendSms(String destinationNumber, String text) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Replace "YourAuthorizationKey" with your actual Infobip authorization key
        String authorizationKey = "7a66b536be23150e1b831236f2a0f441-a26a55c9-c380-40dc-9cbc-99ca058bddd9";

        // Construct the JSON object
        JSONObject requestBody = new JSONObject();
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        JSONArray destinations = new JSONArray();
        JSONObject destination = new JSONObject();
        destination.put("to", "216" + destinationNumber); // Prefixing with "216" assuming it's a country code
        destinations.put(destination);
        message.put("destinations", destinations);
        message.put("from", "formini.tn"); // Replace with your desired sender ID
        message.put("text", text);
        messages.put(message);
        requestBody.put("messages", messages);

        // Create the RequestBody using the constructed JSON object
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());

        // Build the request
        Request request = new Request.Builder()
                .url("https://dkr8wr.api.infobip.com/sms/2/text/advanced")
                .method("POST", body)
                .addHeader("Authorization", "App " + authorizationKey)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());  // Print API response body
            System.out.println("Etat : " + response.isSuccessful());
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}