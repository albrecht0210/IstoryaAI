package com.example.istoryaai;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoryChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story_choice);
        Button randomStoryBtn = findViewById(R.id.randomStoryBtn);
        randomStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = "Generate a JSON object that contains random Cebuano story with comprehension questions and answers.";

                Intent intent = new Intent(StoryChoiceActivity.this, StoryActivity.class);
                makeApiCall(prompt, intent);
            }
        });
        EditText topicTextField = findViewById(R.id.topicTextField);

        Button topicStoryBtn = findViewById(R.id.topicStoryBtn);
        topicStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = "Generate a JSON object that contains a Cebuano story about a " + topicTextField.getText().toString() + " with comprehension questions and answers.";

                Intent intent = new Intent(StoryChoiceActivity.this, StoryActivity.class);
                makeApiCall(prompt, intent);
            }
        });
    }

    private void makeApiCall(String prompt, Intent intent) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        JSONObject data = new JSONObject();
        try {
            data.put("model", "cebuano_model_q4");
            data.put("prompt", prompt);
            data.put("stream", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                data.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://glowworm-clever-shrew.ngrok-free.app/api/generate")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace(); // This is where you'd catch SocketTimeoutException
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONObject responseJson = new JSONObject(responseString);
                        String responseStringRaw = responseJson.getString("response");
                        JSONObject responseData = new JSONObject(responseStringRaw);

                        runOnUiThread(() -> {
                            intent.putExtra("output", responseData.toString());
                            startActivity(intent);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("API_ERROR", "Response code: " + response.code());
                }
            }
        });
    }
}