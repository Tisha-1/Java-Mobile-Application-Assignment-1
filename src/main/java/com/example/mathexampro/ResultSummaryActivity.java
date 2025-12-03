package com.example.mathexampro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultSummaryActivity extends AppCompatActivity {
    TextView totalQuestionsText, answeredQuestionsText, skippedQuestionsText, correctAnswersText, finalScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result_summary);

        totalQuestionsText = findViewById(R.id.totalQuestionsText);
        answeredQuestionsText = findViewById(R.id.answeredQuestionsText);
        skippedQuestionsText = findViewById(R.id.skippedQuestionsText);
        correctAnswersText = findViewById(R.id.correctAnswersText);
        finalScoreText = findViewById(R.id.finalScoreText);

        Intent intent = getIntent();
        int answered = intent.getIntExtra("answered", 0);
        int skipped = intent.getIntExtra("skipped", 0);
        int correct = intent.getIntExtra("correct", 0);
        int score = intent.getIntExtra("score", 0);

        totalQuestionsText.setText("Total Questions: 7");
        answeredQuestionsText.setText("Questions Answered: " + answered);
        skippedQuestionsText.setText("Questions Skipped: " + skipped);
        correctAnswersText.setText("Correct Answers: " + correct);
        finalScoreText.setText("Final Score: " + score + "%");
    }
}