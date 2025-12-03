package com.example.mathexampro;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReviewActivity extends AppCompatActivity {
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);

        layout = findViewById(R.id.reviewLayout);

        for (int i = 0; i < MainActivity.reviewQuestions.size();i++){
            Question q = MainActivity.reviewQuestions.get(i);

            TextView qText = new TextView(this);
            qText.setText("Q" + (i+1) + ": " + q.getQuestionText());
            qText.setTextSize(18);
            layout.addView(qText);

            String[] options = q.getOptions();
            int correct = q.getCorrectAnswerIndex();
            Integer selected = q.getSelectedAnswerIndex();

            for(int j = 0; j < options.length; j++){
                TextView opt = new TextView(this);
                opt.setText((char)('A' + j) + ") " + options[j]);

                //Highlight correct in green, wrong in red
                if (j == correct){
                    opt.setTextColor(Color.GREEN);
                }else if (selected != null && j == selected){
                    opt.setTextColor(Color.RED);
                }

                layout.addView(opt);
            }

            //Optional explanation
            TextView explanation = new TextView(this);
            explanation.setText("Explanation: " + getExplanation(i));
            explanation.setTextSize(14);
            explanation.setPadding(0, 8, 0, 24);
            layout.addView(explanation);
        }
    }

    private boolean jEquals(int a){
        return a == MainActivity.reviewQuestions.get(0).getCorrectAnswerIndex();
    }

    private String getExplanation(int index){
        switch (index){
            case 0: return "This is a geometric series with limit 1.";
            case 1: return "Using limit identity: sin(kx)/x _> k.";
            case 2: return "Use inclusion-exclusion for multiples of 2 and 5.";
            case 3: return "Det(A) = ad - bc = (2)(3) - (-1)(4) = 10.";
            case 4: return "P(A ∪ B) = P(A) + P(B) - P(A ∩ B).";
            case 5: return "Solve 3x ≡ 1 mod 7 -> x = 5.";
            case 6: return "log₂(x² - 1) = 3 → x² - 1 = 8 → x = ±3.";
            default: return "";
        }
    }
}