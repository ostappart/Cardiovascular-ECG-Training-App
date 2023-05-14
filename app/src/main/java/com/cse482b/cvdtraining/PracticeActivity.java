package com.cse482b.cvdtraining;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class PracticeActivity extends AppCompatActivity implements View.OnClickListener {
    private class Question {
        public String text;
        public String imagePath;
        public int correctIndex;
        public String[] answerOptions;

        Question(String text, String imagePath, String[] answerOptions) {
            this.text = text;
            this.imagePath = imagePath;
            if (answerOptions.length == 2 && (answerOptions[0].equals("True") || answerOptions[0].equals("False"))) {
                // translate and order options for true/false questions:
                this.correctIndex = (answerOptions[0].equals("True")) ? 0 : 1;
                this.answerOptions = new String[] { getString(R.string.True), getString(R.string.False) };
            } else {
                String correct = answerOptions[0];
                List<String> options = Arrays.asList(answerOptions);
                Collections.shuffle(options);
                this.answerOptions = options.toArray(new String[0]);
                this.correctIndex = options.indexOf(correct);
            }
        }

        public boolean isCorrect(String answer) {
            return answer.equals(answerOptions[correctIndex]);
        }

        public boolean isCorrect(int answerIX) {
            return answerIX == correctIndex;
        }
    }

    /** UI Elements */
    private Button topLeft;
    private Button topRight;
    private Button bottomLeft;
    private Button bottomRight;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageView Image;
    private TextView questionTextView;

    /** SharedPreferences for saved data */
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    /** Question Data */
    private String questionCategory;
    private List<Question> questions;
    private int currentQuestionIndex = 0;

    private void loadQuestions() {
        questionCategory = sharedPref.getString("questionCategory", "defaultQuestions");
        currentQuestionIndex = sharedPref.getInt("currentIX" + questionCategory, 0);
        // Expected format (3 lines per question):
        // [Question Type]
        // [Image Path]
        // [Answer Options - first one should be the correct one]
        questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getAssets().open("questions/" + questionCategory + ".txt")))) {

            String questionType;
            while ((questionType = reader.readLine()) != null) {
                int resId = getResources().getIdentifier(questionType, "string", getPackageName());
                String questionText = getResources().getString(resId);
                String imagePath = reader.readLine();
                String[] answerOptions = reader.readLine().split(", ");
                questions.add(new Question(questionText, imagePath, answerOptions));
            }
        } catch (IOException e) {
            Log.d("PracticeActivity", "loadQuestions: couldn't open file");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        sharedPref = getSharedPreferences("com.CDV.training.questions", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        loadQuestions();

        topLeft = findViewById(R.id.buttonLeftTop);
        topRight = findViewById(R.id.buttonRightTop);
        bottomLeft = findViewById(R.id.buttonLeftBottom);
        bottomRight = findViewById(R.id.buttonRightBottom);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        questionTextView = findViewById(R.id.question_text);
        Image = findViewById(R.id.question_image);

        topLeft.setOnClickListener(this);
        topRight.setOnClickListener(this);
        bottomLeft.setOnClickListener(this);
        bottomRight.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

        updateQuestion();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadQuestions();
        updateQuestion();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLeftTop:
                checkAnswer(0);
                break;
            case R.id.buttonRightTop:
                checkAnswer(1);
                break;
            case R.id.buttonLeftBottom:
                checkAnswer(2);
                break;
            case R.id.buttonRightBottom:
                checkAnswer(3);
                break;
            case R.id.next_button:
                if (currentQuestionIndex < questions.size()) {
                    currentQuestionIndex = currentQuestionIndex + 1;
                    updateQuestion();
                    editor.putInt("currentIX" + questionCategory, currentQuestionIndex);
                    editor.apply();
                }
                break;
            case R.id.prev_button:
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    updateQuestion();
                    editor.putInt("currentIX" + questionCategory, currentQuestionIndex);
                    editor.apply();
                }
                break;
        }
    }

    private void updateQuestion() {
        Log.d("PracticeActivity", "updateQuestion: " + currentQuestionIndex);

        if (currentQuestionIndex == questions.size()) {
            questionTextView.setText(R.string.noMoreQuestions);
            nextButton.setVisibility(View.INVISIBLE);
            topLeft.setVisibility(View.INVISIBLE);
            topRight.setVisibility(View.INVISIBLE);
            bottomLeft.setVisibility(View.INVISIBLE);
            bottomRight.setVisibility(View.INVISIBLE);
            Image.setVisibility(View.INVISIBLE);

            prevButton.setVisibility(View.VISIBLE);
            return;
        } else {
            nextButton.setVisibility(View.VISIBLE);
            topLeft.setVisibility(View.VISIBLE);
            topRight.setVisibility(View.VISIBLE);
            bottomLeft.setVisibility(View.VISIBLE);
            bottomRight.setVisibility(View.VISIBLE);
            Image.setVisibility(View.VISIBLE);
        }

        if (currentQuestionIndex == 0) prevButton.setVisibility(View.INVISIBLE);
        else prevButton.setVisibility(View.VISIBLE);

        Question current = questions.get(currentQuestionIndex);

        questionTextView.setText(current.text);

        if (current.answerOptions.length > 0) topLeft.setText(current.answerOptions[0]);
        else topLeft.setVisibility(View.INVISIBLE);
        if (current.answerOptions.length > 1) topRight.setText(current.answerOptions[1]);
        else topRight.setVisibility(View.INVISIBLE);
        if (current.answerOptions.length > 2) bottomLeft.setText(current.answerOptions[2]);
        else bottomLeft.setVisibility(View.INVISIBLE);
        if (current.answerOptions.length > 3) bottomRight.setText(current.answerOptions[3]);
        else bottomRight.setVisibility(View.INVISIBLE);

        try {
            InputStream ims = getAssets().open(current.imagePath);
            Drawable d = Drawable.createFromStream(ims, null);
            Image.setImageDrawable(d);
        } catch (IOException e) {
            Log.d("PracticeActivity", "updateQuestion: couldn't find image file");
        }
    }

    private void checkAnswer(int userAnswer) {
        int toastMessageId;

        if (questions.get(currentQuestionIndex).isCorrect(userAnswer)) {
            toastMessageId = R.string.correct;
        } else {
            toastMessageId = R.string.incorrect;
        }

        Toast.makeText(getApplicationContext(), toastMessageId, Toast.LENGTH_SHORT).show();
    }
}