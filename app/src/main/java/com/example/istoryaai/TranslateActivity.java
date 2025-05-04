package com.example.istoryaai;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslateActivity extends AppCompatActivity {
    private TextView translatedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_translate);

        TextView translationTitleTextView = findViewById(R.id.translationTitleTextView);
        translatedTextView = findViewById(R.id.translatedTextView);
        EditText translateTargetText = findViewById(R.id.editTextTextMultiLine);
        SwitchCompat translationTargetSwitch = findViewById(R.id.translationTargetSwitch);
        Button translateBtn = findViewById(R.id.translateApiBtn);

        if (translationTargetSwitch.isChecked()) {
            translationTitleTextView.setText("English to Cebuano");
            translateTargetText.setText("");
            translateTargetText.setHint("English");
        } else {
            translationTitleTextView.setText("Cebuano to English");
            translateTargetText.setText("");
            translateTargetText.setHint("Cebuano");
        }

        translationTargetSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                translationTitleTextView.setText("English to Cebuano");
                translateTargetText.setText("");
                translateTargetText.setHint("English");
            } else {

                translationTitleTextView.setText("Cebuano to English");
                translateTargetText.setText("");
                translateTargetText.setHint("Cebuano");
            }
        });

        translateBtn.setOnClickListener(v -> {
            String targetText = translateTargetText.getText().toString();
            if (translationTargetSwitch.isChecked()) {
                if (targetText.split(" ").length == 1) {
                    makeApiCall("Translate this English word to Cebuano: " + targetText);
                } else {
                    makeApiCall("Translate this English sentence to Cebuano: " + targetText);
                }
            } else {
                if (targetText.split(" ").length == 1) {
                    makeApiCall("Translate this Cebuano word to English: " + targetText);
                } else {
                    makeApiCall("Translate this Cebuano sentence to English: " + targetText);
                }
            }
        });
    }

    private void makeApiCall(String prompt) {
        OkHttpClient client = new OkHttpClient();

        JSONObject data = new JSONObject();
        try {
            data.put("model", "cebuano_model_q8");
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
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONObject responseJson = new JSONObject(responseString);
                        String translatedText = responseJson.getString("response");

                        runOnUiThread(() -> {
                            translatedTextView.setText(translatedText);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}