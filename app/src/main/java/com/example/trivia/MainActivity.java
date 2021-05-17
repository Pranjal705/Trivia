package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionCounterText;
    private TextView questionTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private TextView currentScoreView;
    private TextView totalScoreView;
    private int currentScore = 0;
    private int totalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionCounterText = findViewById(R.id.counter_text);
        questionTextView = findViewById(R.id.question_text);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        currentScoreView = findViewById(R.id.currentScore);
        totalScoreView = findViewById(R.id.totalScore);



        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                resetQuestion();
                totalScore = questionArrayList.size() * 100;
                totalScoreView.setText(getString(R.string.total_score) + totalScore);
                currentScoreView.setText(getString(R.string.current_score) + currentScore*0);
                Log.d("Main", "processFinished: " + questionList);
            }
        });
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;
            case R.id.next_button:
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
                break;
            case R.id.prev_button:
                if(currentQuestionIndex>0)
                {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                }
                break;
            case R.id.question_text:
                break;
            case R.id.counter_text:

        }
    }
    public void updateQuestion()
    {
        questionTextView.setText(questionList.get(currentQuestionIndex).getAnswer());
        questionCounterText.setText(currentQuestionIndex+1 + "/" + questionList.size());
    }

    public void resetQuestion()
    {
        questionTextView.setText(questionList.get(currentQuestionIndex).getAnswer());
        questionCounterText.setText(currentQuestionIndex+1 + "/" + questionList.size());
    }

    public void checkAnswer(boolean userAnswer)
    {
        boolean correctAnswer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int ToastMessageId = 0;
        if (userAnswer == correctAnswer)
        {
            ToastMessageId = R.string.correct_answer;
            fadeView();
            updateScore(true);
        }
        else
        {
            ToastMessageId = R.string.wrong_answer;
            shake_Animation();
            updateScore(false);
        }
        Toast.makeText(MainActivity.this,ToastMessageId,Toast.LENGTH_SHORT).show();
    }

    private void updateScore(boolean correct)
    {
        if(correct)
        {
            currentScore = (currentScore + 100) % (questionList.size()*100);
            currentScoreView.setText(getString(R.string.current_score) + currentScore);
        }
        else
        {
            currentScore = (currentScore - 100) % (questionList.size()*100);
            currentScoreView.setText(getString(R.string.current_score) + currentScore);
        }
    }

    private void fadeView()
    {
        CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(250);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setRepeatCount(1);

        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void shake_Animation()
    {
        Animation shake_animation = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake_animation);

        shake_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}