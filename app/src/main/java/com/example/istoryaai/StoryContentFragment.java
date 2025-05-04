package com.example.istoryaai;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryContentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoryContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoryContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoryContentFragment newInstance(String param1, String param2) {
        StoryContentFragment fragment = new StoryContentFragment();
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

    private TextView titleTextView, storyTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_content, container, false);

        Bundle args = getArguments();

        String story = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis cursus quis lectus tempor elementum. Pellentesque sed est ac nunc bibendum lobortis vel ut mi. Pellentesque id tincidunt magna. Mauris congue, arcu quis tempus vehicula, lacus odio cursus odio, sit amet varius odio elit ut justo. Nam convallis arcu molestie dolor faucibus, vel sollicitudin lorem aliquam. Nam ut hendrerit tortor. Morbi risus velit, aliquet non erat nec, gravida lacinia dolor. Nam nec ligula auctor, lacinia enim vitae, aliquam purus.  Mauris efficitur, risus ut bibendum rutrum, massa neque viverra orci, blandit faucibus odio ex ut tellus. Sed erat enim, tincidunt quis bibendum sed, euismod tristique lectus. Curabitur ullamcorper enim mi, vel ultrices lectus tincidunt a. Cras placerat libero felis, sit amet pharetra dolor pretium et. Curabitur in ultrices orci, et pretium leo. Duis nulla orci, viverra quis diam et, commodo eleifend tellus. Praesent elementum dui a massa malesuada finibus. Quisque et tellus gravida, mattis nisi eget, pulvinar lectus. Pellentesque vel nunc odio. Nulla vel interdum magna, volutpat maximus tortor. Nulla sodales egestas tellus, et fringilla leo pellentesque in. Maecenas mollis, justo a molestie iaculis, quam leo ultrices mauris, in sodales justo urna sit amet erat. Proin semper aliquam dui ac sagittis. Donec tempus nulla id accumsan tempus. Nullam euismod aliquam lectus id faucibus.  Donec a est in justo venenatis molestie. Nulla ac sodales lorem, at facilisis quam. Maecenas eu justo auctor, vulputate erat sit amet, dignissim risus. Donec placerat libero a nulla congue rutrum. Sed pretium libero ante, id tristique ante dapibus quis. Integer dictum ullamcorper massa id tincidunt. Aenean a augue elit. Sed ultrices arcu sed felis luctus maximus. Etiam non ipsum ipsum. Sed mi ante, aliquam sed congue ac, cursus quis ipsum. Curabitur at leo lacus. Cras eu felis nulla. In hac habitasse platea dictumst. Integer ac est sed lorem mollis dignissim sed eget ligula. In commodo vulputate elit, quis scelerisque neque tempus vel. Donec in justo tincidunt mauris mattis elementum eget id mauris.";
        String title = "Title of Lorem";

        titleTextView = view.findViewById(R.id.titleTextView);
        storyTextView = view.findViewById(R.id.storyTextView);
        if (args != null) {
            title = args.getString("title");
            story = args.getString("story");
        }
        titleTextView.setText(title);
        storyTextView.setText(story);

        return view;
    }
}