package com.example.istoryaai;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryQuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryQuizFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoryQuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoryQuizFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoryQuizFragment newInstance(String param1, String param2) {
        StoryQuizFragment fragment = new StoryQuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private final Map<Integer, Integer> selectedAnswers = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_quiz, container, false);

        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()) {
                if (key.startsWith("question_")) {
                    int index = Integer.parseInt(key.split("_")[1]);
                    int selectedRadioIndex = savedInstanceState.getInt(key);
                    selectedAnswers.put(index, selectedRadioIndex);
                }
            }
        }

        // Get the arguments passed to the fragment (JSON string containing the questions)
        Bundle args = getArguments();
        String questionsJSON = "";
        if (args != null) {
            questionsJSON = args.getString("questions");
        }

        // Find the container where the questions will be added
        LinearLayout questionContainer = view.findViewById(R.id.questionsLinearLayout);

        try {
            // Convert the questions JSON string into a JSONArray
            JSONArray questionsArray = new JSONArray(questionsJSON);

            // Iterate through each question in the JSONArray
            for (int questionIndex = 0; questionIndex < questionsArray.length(); questionIndex++) {
                // Single Question JSON Object
                JSONObject questionObj = questionsArray.getJSONObject(questionIndex);


                // Create a wrapper layout for each question
                LinearLayout wrapper = new LinearLayout(requireContext());
                wrapper.setOrientation(LinearLayout.VERTICAL);

                // Set margins for the wrapper (spacing between questions)
                LinearLayout.LayoutParams wrapperParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                wrapperParams.setMargins(0, 0, 0, 24); // margin bottom
                wrapper.setLayoutParams(wrapperParams);

                String question = (questionIndex + 1) + ". " + questionObj.getString("question");
                // Create and set up the TextView for the question
                TextView questionText = new TextView(requireContext());
                questionText.setText(question);
                questionText.setTextSize(20);
                questionText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                // Create a RadioGroup for the choices (multiple radio buttons)
                RadioGroup radioGroup = new RadioGroup(requireContext());
                radioGroup.setOrientation(RadioGroup.VERTICAL);
                radioGroup.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                JSONArray choices = questionObj.getJSONArray("choices");
                int finalQuestionIndex = questionIndex;

                // Add each choice as a RadioButton in the RadioGroup
                for (int choiceIndex = 0; choiceIndex < choices.length(); choiceIndex++) {
                    String choiceText = choices.getString(choiceIndex);
                    RadioButton radioButton = new RadioButton(requireContext());
                    radioButton.setText(choiceText);

                    // Generate a unique ID for each RadioButton
                    int radioButtonId = View.generateViewId(); // Ensure unique ID
                    radioButton.setId(radioButtonId);

                    // Restore selection
                    if (selectedAnswers.containsKey(finalQuestionIndex) && selectedAnswers.get(finalQuestionIndex) == choiceIndex) {
                        radioButton.setChecked(true);
                    }

                    // Add the RadioButton to the RadioGroup
                    radioGroup.addView(radioButton);
                }

                // Set a listener to track when a radio button is selected
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    // Store the selected radio button ID in the map using the question index as the key
                    int index = group.indexOfChild(group.findViewById(checkedId));
                    selectedAnswers.put(finalQuestionIndex, index);
                });

                // If there is a saved state (when fragment is recreated), restore the selection
                if (savedInstanceState != null && savedInstanceState.containsKey("question_" + questionIndex)) {
                    // Get the selected RadioButton ID from saved state and restore the selection
                    int selectedRadioId = savedInstanceState.getInt("question_" + questionIndex);
                    radioGroup.check(selectedRadioId); // Restore the selected RadioButton
                }

                // Add the question TextView and RadioGroup to the wrapper layout
                wrapper.addView(questionText);
                wrapper.addView(radioGroup);

                // Add the wrapper layout containing the question and radio buttons to the main container
                questionContainer.addView(wrapper);
            }

        } catch (JSONException e) {
            e.printStackTrace(); // Handle JSON parsing errors
        }

        // Set up the button for checking answers
        Button checkAnswersBtn = view.findViewById(R.id.checkAnswerBtn);
        checkAnswersBtn.setOnClickListener(v -> {
            // Handle checking answers logic (this can be customized as needed)
            try {
                JSONArray questionsArray = new JSONArray(getArguments().getString("questions"));

                for (int i = 0; i < questionsArray.length(); i++) {
                    JSONObject questionObj = questionsArray.getJSONObject(i);
                    String correctAnswerLetter = questionObj.getString("answer");
                    String explanation = questionObj.getString("explanation");

                    // Get the wrapper view (question block)
                    LinearLayout wrapper = (LinearLayout) questionContainer.getChildAt(i);
                    RadioGroup radioGroup = (RadioGroup) wrapper.getChildAt(1); // Assuming [0]=TextView, [1]=RadioGroup

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId != -1) {
                        int selectedIndex = radioGroup.indexOfChild(radioGroup.findViewById(selectedId));

                        // Map index to letter (0 = a, 1 = b, ...)
                        String[] letterOptions = {"A", "B", "C", "D"};
                        String selectedLetter = selectedIndex < letterOptions.length ? letterOptions[selectedIndex] : "";
                        // Check if incorrect
                        if (!selectedLetter.equals(correctAnswerLetter)) {
                            // Add explanation TextView
                            TextView explanationView = new TextView(requireContext());
                            explanationView.setText("Explanation: " + explanation);
                            explanationView.setTextColor(Color.RED);
                            explanationView.setTextSize(16);
                            explanationView.setPadding(32, 16, 0, 0);

                            // Remove old explanation if it exists
                            if (wrapper.getChildCount() == 3) {
                                wrapper.removeViewAt(2); // Remove previous explanation
                            }

                            wrapper.addView(explanationView);
                        } else {
                            // Remove explanation if answer is correct and already exists
                            if (wrapper.getChildCount() == 3) {
                                wrapper.removeViewAt(2);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        // Return the inflated view for the fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the selected radio button ID for each question when the fragment is about to be destroyed
        for (Map.Entry<Integer, Integer> entry : selectedAnswers.entrySet()) {
            System.out.println("Saving...");
            outState.putInt("question_" + entry.getKey(), entry.getValue());
        }
    }
}