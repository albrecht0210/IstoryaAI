package com.example.istoryaai;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class StoryActivity extends AppCompatActivity {
    private JSONObject storyObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story);
        String stringStoryJson = getIntent().getStringExtra("output");
        try {
            storyObject = new JSONObject(stringStoryJson);
            // Use the JSON object
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomStoryNav);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            try {
                if (item.getItemId() == R.id.story_content) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", storyObject.getString("title"));
                    bundle.putString("story", storyObject.getString("story"));
                    selected = new StoryContentFragment();
                    selected.setArguments(bundle);
                } else if (item.getItemId() == R.id.story_quiz) {
                    Bundle bundle = new Bundle();
                        bundle.putString("questions", storyObject.getJSONArray("questions").toString());
                    selected = new StoryQuizFragment();
                    selected.setArguments(bundle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.storyFrameLayout, selected)  // <- your container layout ID
                        .commit();
                return true;
            }

            return true;
        });

        bottomNav.setSelectedItemId(R.id.story_content);
    }
}