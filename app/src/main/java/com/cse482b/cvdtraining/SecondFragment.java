package com.cse482b.cvdtraining;

import android.graphics.drawable.Drawable;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SecondFragment extends Fragment implements View.OnClickListener {
    private static class Question {
        public String text;
        public String imagePath;
        public int correctIndex;
        public String[] answerOptions;

        Question(String text, String imagePath, String[] answerOptions) {
            this.text = text;
            this.imagePath = imagePath;
            if (answerOptions.length == 2) {
                this.correctIndex = 0;
                this.answerOptions = answerOptions;
            } else {
                // TODO: Shuffle answer options
            }
        }

        public boolean isCorrect(String answer) {
            return answer.equals(answerOptions[correctIndex]);
        }
    }

    private FragmentSecondBinding binding;

    private Button falseButton;
    private Button trueButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageView Image;
    private TextView questionTextView;

    private ArrayList<Question> questions;
    private int currentQuestionIndex = 0; // TODO: save visited question scores and use to recommend questions

    private void loadQuestions() {
        // Expected format (3 lines per question):
        // [Question Type]
        // [Image Path]
        // [Answer Options - first one should be the correct one]
        questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(requireContext().getAssets().open("questions.txt")))) {

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                int resId = getResources().getIdentifier(mLine, "string", requireContext().getPackageName());
                String questionText = getResources().getString(resId);
                String imagePath = reader.readLine();
                String[] answerOptions = reader.readLine().split(", ");
                questions.add(new Question(questionText, imagePath, answerOptions));
            }
        } catch (IOException e) {
            Log.d("SecondFragment", "loadQuestions: couldn't open file");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        loadQuestions();

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        falseButton = view.findViewById(R.id.false_button);
        trueButton = view.findViewById(R.id.true_button);
        nextButton = view.findViewById(R.id.next_button);
        prevButton = view.findViewById(R.id.prev_button);
        questionTextView = view.findViewById(R.id.answer_text_view);
        Image = view.findViewById(R.id.question_image);

        falseButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

        binding.buttonHome.setOnClickListener(view1 ->
                NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment)
        );

        updateQuestion();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.false_button:
                checkAnswer("False");
                break;

            case R.id.true_button:
                checkAnswer("True");
                break;

            case R.id.next_button:
                if (currentQuestionIndex < questions.size()) {
                    currentQuestionIndex = currentQuestionIndex + 1;
                    if (currentQuestionIndex == questions.size()) {
                        questionTextView.setText("No More Questions :(");
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
                    currentQuestionIndex--;
                    updateQuestion();
                }
        }
    }

    private void updateQuestion() {
        // TODO: add question types
        Log.d("SecondFragment", "updateQuestion: " + currentQuestionIndex);

        Question current = questions.get(currentQuestionIndex);

        questionTextView.setText(current.text);

        try {
            InputStream ims = requireContext().getAssets().open(current.imagePath);
            Drawable d = Drawable.createFromStream(ims, null);
            Image.setImageDrawable(d);
        } catch (IOException e) {
            Log.d("SecondFragment", "updateQuestion: couldn't find image file");
        }
    }

    private void checkAnswer(String userAnswer) {
        int toastMessageId;

        if (questions.get(currentQuestionIndex).isCorrect(userAnswer)) {
            toastMessageId = R.string.True; // TODO: actual message of whether user was correct
        } else {
            toastMessageId = R.string.False;
        }

        Toast.makeText(getContext(), toastMessageId, Toast.LENGTH_SHORT).show();
    }
}