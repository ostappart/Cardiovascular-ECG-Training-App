package com.cse482b.cvdtraining;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.cse482b.cvdtraining.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment implements View.OnClickListener {

    private FragmentSecondBinding binding;

    private Button falseButton;
    private Button trueButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageView Image;
    private TextView questionTextView;
    private int currentQuestionIndex = 0; // TODO: save visited question scores and use to recommend questions

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setting up the buttons
        // associated with id
        falseButton = view.findViewById(R.id.false_button);
        trueButton = view.findViewById(R.id.true_button);
        nextButton = view.findViewById(R.id.next_button);
        prevButton = view.findViewById(R.id.prev_button);
        // register our buttons to listen to
        // click events
        questionTextView
                = view.findViewById(R.id.answer_text_view);
        Image = view.findViewById(R.id.question_image);
        falseButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

        binding.buttonHome.setOnClickListener(view1 ->
                NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        // checking which button is
        // clicked by user
        // in this case user choose false
        switch (v.getId()) {
            case R.id.false_button:
                checkAnswer(false);
                break;

            case R.id.true_button:
                checkAnswer(true);
                break;

            case R.id.next_button:
                // go to next question
                if (currentQuestionIndex < 7) { // TODO: should be number of questions, not 7
                    currentQuestionIndex = currentQuestionIndex + 1;
                    if (currentQuestionIndex == 6) {
                        questionTextView.setText("Hurra!");
                        nextButton.setVisibility(
                                View.INVISIBLE);
                        prevButton.setVisibility(
                                View.INVISIBLE);
                        trueButton.setVisibility(
                                View.INVISIBLE);
                        falseButton.setVisibility(
                                View.INVISIBLE);
                    } else {
                        updateQuestion();
                    }
                }

                break;
            case R.id.prev_button:
                if (currentQuestionIndex > 0) {
                    // TODO: set currentQuestionIndex
                    updateQuestion();
                }
        }
    }

    private void updateQuestion() {
        // TODO: add question types
        Log.d("SecondFragment", "updateQuestion: " + currentQuestionIndex);

        questionTextView.setText("Text of the current question"); // TODO

        switch (currentQuestionIndex) {
            case 1:
                // setting up image for each question
                // Image.setImageResource(R.drawable.q1);
                Image.setImageBitmap(BitmapFactory.decodeFile("assets/"));
                break;
        }
    }
    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = true; // TODO: set to whether currentQuestionIndex is true
        int toastMessageId;

        if (userChooseCorrect == answerIsTrue) {
            toastMessageId = R.string.True;
        } else {
            toastMessageId = R.string.False;
        }

        Toast.makeText(getContext(), toastMessageId, Toast.LENGTH_SHORT).show();
    }
}