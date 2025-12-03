package com.example.mathexampro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<Question> questionList;
    int currentQuestionIndex = 0;
    int skippedCount = 0;
    final int maxSkips = 2;
    boolean scoreShown = false;

    //UI elements
    TextView questionText, questionNumber, skipCounter;
    RadioGroup optionsGroup;
    RadioButton option1, option2, option3, option4;
    Button nextButton, prevButton, skipButton, submitButton, resetButton, resultSummaryButton, reviewButton;

    public static List<Question> reviewQuestions = new ArrayList<>();

    List<Question> masterQuestionBank = new ArrayList<>();

    private void loadQuestions(){
        List<Question> allQuestions = Arrays.asList(
            new Question(" What is the sum of the infinite series:\n 1 − ½ + ¼ − ⅛ + ... ? ", new String[]{"1", "0", "2", "Does not converge"}, 2),
            new Question(" Evaluate the limit:\nlim (x → 0) [sin(3x) / x] ", new String[]{"0", "1", "3", "Undefined"}, 2),
            new Question(" How many integers between 1 and 1000 are divisible by neither 2 nor 5?", new String[]{"400", "500", "300", "600"}, 0),
            new Question(" Find the determinant of the matrix:\n[[2, -1], [4, 3]] ", new String[]{"10", "-10", "11", "5"}, 0),
            new Question(" If P(A) = 0.6, P(B) = 0.5, and P(A ∩ B) = 0.3, what is P(A ∪ B)?", new String[]{"0.8", "1.1", "0.9", "0.7"}, 0),
            new Question(" What is the smallest positive integer x such that: \n 3x ≡ 1 (mod 7)", new String[]{"1", "2", "3", "5"}, 3),
            new Question(" Solve the equation: \n log₂(x² − 1) = 3 ", new String[]{"x = 4", "x = ±3", "x = 3", "x = 2"}, 1)
        );

        Collections.shuffle(allQuestions);
        questionList = allQuestions.subList(0, 5);

        //Backup full question list to pull for replacements (skip function)
        masterQuestionBank = new ArrayList<>(allQuestions);
    }

    private void displayQuestion(int index){
        Question q = questionList.get(index);
        questionNumber.setText("Question " + (index + 1) + " of 5");
        questionText.setText("Q" + (index + 1) + ":" + q.getQuestionText());
        String[] options = q.getOptions();
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);
        option4.setText(options[3]);

        //Always reset first
        optionsGroup.clearCheck();
        for (int i = 0; i < optionsGroup.getChildCount(); i++){
            optionsGroup.getChildAt(i).setEnabled(true);
        }

        //If already submitted, disable and pre-select the chosen option
        if(q.getSelectedAnswerIndex() != null){
            switch (q.getSelectedAnswerIndex()){
                case 0:option1.setChecked(true);break;
                case 1:option2.setChecked(true);break;
                case 2:option3.setChecked(true);break;
                case 3:option4.setChecked(true);break;
            }

            for (int i = 0; i< optionsGroup.getChildCount(); i++){
                optionsGroup.getChildAt(i).setEnabled(false);
            }
        }
    }

    private void checkAndShowFinalScore(){
        if (scoreShown) return;

        int answeredCount = 0;
        int correctCount = 0;

        for(Question q:questionList){
            if (q.getSelectedAnswerIndex() != null){
                answeredCount++;
                if (q.getSelectedAnswerIndex().equals(q.getCorrectAnswerIndex())) {
                    correctCount++;
                }
            }
        }

        //If all 7 questions are answered (or no more skips allowed), show score
        if(answeredCount == 5 && !scoreShown){
            int scorePercent = (int) ((correctCount / 5.0) * 100);
            Toast.makeText(MainActivity.this, "Final Score: " + scorePercent + "%", Toast.LENGTH_SHORT).show();
            scoreShown = true;//prevents showing again

            //Show Reset button, Review button, Result Summary button now
            resultSummaryButton.setVisibility(View.VISIBLE);
            reviewButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionNumber = findViewById(R.id.questionNumber);
        questionText = findViewById(R.id.questionText);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (currentQuestionIndex < questionList.size() - 1){
                    currentQuestionIndex++;
                    displayQuestion(currentQuestionIndex);
                }else{
                    Toast.makeText(MainActivity.this, "You are on the last question!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        prevButton = findViewById(R.id.prevButton);

        prevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (currentQuestionIndex > 0){
                    currentQuestionIndex--;
                    displayQuestion(currentQuestionIndex);
                }else if(currentQuestionIndex == 0){
                    Toast.makeText(MainActivity.this, "You are on the first question!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Question currentQuestion = questionList.get(currentQuestionIndex);

                //Check if already submitted
                if (currentQuestion.getSelectedAnswerIndex() != null){
                    Toast.makeText(MainActivity.this, "You already submitted this question.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Get selected answer
                int selectedID = optionsGroup.getCheckedRadioButtonId();

                if (selectedID == -1){
                    Toast.makeText(MainActivity.this, "Please select an answer first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Determine which option was selected
                int answerIndex = -1;
                if (selectedID == option1.getId())answerIndex = 0;
                else if (selectedID == option2.getId())answerIndex = 1;
                else if (selectedID == option3.getId())answerIndex = 2;
                else if (selectedID == option4.getId())answerIndex = 3;

                //Save selected answer
                currentQuestion.setSelectedAnswerIndex(answerIndex);

                checkAndShowFinalScore();

                //Disable all options
                for (int i = 0; i < optionsGroup.getChildCount(); i++){
                    optionsGroup.getChildAt(i).setEnabled(false);
                }

                Toast.makeText(MainActivity.this, "Answer submitted!", Toast.LENGTH_SHORT).show();
            }
        });

        skipButton = findViewById(R.id.skipButton);
        skipCounter = findViewById(R.id.skipCounter);

        skipButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (skippedCount < maxSkips){
                    skippedCount++;

                    //Get current question index
                    int index = currentQuestionIndex;
                    Question current = questionList.get(index);

                    //Find questions from master bank that are no in questionList and not answered
                    List<Question> unusedQuestions = new ArrayList<>();
                    for (Question q : masterQuestionBank){
                        boolean inUse = questionList.contains(q);
                        boolean answered = q.getSelectedAnswerIndex() != null;
                        boolean isSame = q.equals(current);

                        if(!inUse && !answered && !isSame){
                            unusedQuestions.add(q);
                        }
                    }

                    if (!unusedQuestions.isEmpty()){
                        Collections.shuffle(unusedQuestions);
                        Question newQ = unusedQuestions.get(0);

                        questionList.set(index, newQ);//replace current question
                        displayQuestion(index);
                    }else{
                        Toast.makeText(MainActivity.this, "No unused questions available to skip.", Toast.LENGTH_SHORT).show();
                    }

                    displayQuestion(index);//stay on current index
                    //Update skip counter
                    skipCounter.setText("Skips remaining: " + (maxSkips - skippedCount));

                    //Disable skip button if max used
                    if(skippedCount == maxSkips){
                        skipButton.setEnabled(false);
                    }
                    checkAndShowFinalScore();
                }
            }
        });

        resultSummaryButton = findViewById(R.id.resultSummaryButton);

        resultSummaryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int answered = 0;
                int correct = 0;

                for (Question q:questionList){
                    if(q.getSelectedAnswerIndex() != null){
                        answered++;
                        if(q.getSelectedAnswerIndex().equals(q.getCorrectAnswerIndex())){
                            correct++;
                        }
                    }
                }

                int skipped = 7 - answered;
                int finalScore = (int) ((correct / 5.0) * 100);

                Intent intent = new Intent(MainActivity.this, ResultSummaryActivity.class);
                intent.putExtra("answered", answered);
                intent.putExtra("skipped", skipped);
                intent.putExtra("correct", correct);
                intent.putExtra("score", finalScore);
                startActivity(intent);
            }
        });

        resetButton = findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetExam();
            }
        });

        loadQuestions();
        displayQuestion(currentQuestionIndex);

        reviewButton = findViewById(R.id.reviewButton);

        reviewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reviewQuestions = questionList;//Store current questions
                Intent intent = new Intent(MainActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void resetExam(){
        //Reset question index and skipped count
        currentQuestionIndex = 0;
        skippedCount = 0;
        scoreShown = false;

        //Reload randomized questions
        loadQuestions();
        displayQuestion(currentQuestionIndex);
        //Update skip UI
        skipCounter.setText("Skips remaining: 2");
        skipButton.setEnabled(true);
        submitButton.setEnabled(true);

        //Show first question
        displayQuestion(currentQuestionIndex);

        Toast.makeText(MainActivity.this, "Exam has been reset.", Toast.LENGTH_SHORT).show();

        resultSummaryButton.setVisibility(View.GONE);
        reviewButton.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);
    }
}