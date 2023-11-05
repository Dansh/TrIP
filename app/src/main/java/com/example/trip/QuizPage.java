package com.example.trip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class QuizPage extends AppCompatActivity implements View.OnClickListener {


    int questionsNum;
    LinkedHashMap<String, String> quizTitlesAndQuestions = new LinkedHashMap<String, String>() {{
        put("Destination", "Which city, country, or specific location do you want to visit?");
        put("Duration", "How long do you plan to stay on your trip? Could you provide the number of days or specific dates if you have them?");
        put("Budget", "What is your budget for the trip? It would be helpful to know this information to suggest suitable accommodations, transportation options, and activities.");
        put("Interests", "What are your interests? Are you into adventure, history, nature, shopping, or any specific activities or attractions you'd like to experience during your trip?");
        put("Accommodation preferences", "What are your accommodation preferences? Do you prefer hotels, hostels, vacation rentals, or do you have any specific requirements?");
        put("Transportation", "How do you plan on getting around? Are you open to flying, driving, or taking public transportation? Or do you have any specific transportation preferences?");
        put("Travel companions", "Are you traveling alone, with a partner, friends, or family? It would be helpful to know the number of people and their ages to suggest suitable activities and accommodations.");
        put("Special requirements", "Do you have any special requirements or needs? For example, do you have dietary restrictions, accessibility concerns, or any specific arrangements?");
        put("Season", "What time of year do you plan to travel? Knowing the season can help us suggest appropriate activities and consider weather conditions.");
        put("Any additional information", "Is there any other information you would like to share? Do you have any specific preferences, constraints, or questions?");
    }};


    List<String> quizTitles = new ArrayList<>(quizTitlesAndQuestions.keySet());
    List<String> quizQuestions = new ArrayList<>(quizTitlesAndQuestions.values());

    Button submitQuestionBtn;
    TextView quizTitleTv, quizQuestionTv;
    EditText quizInputEt;
    TripData tripDataSingelton;
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);


        questionsNum = 0;
         v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        quizTitleTv = findViewById(R.id.quizTitleTv);
        quizQuestionTv = findViewById(R.id.quizQuestionTv);

        submitQuestionBtn = findViewById(R.id.submitQuestionBtn);
        submitQuestionBtn.setOnClickListener(this);

        quizInputEt = findViewById(R.id.quizInputEt);

        tripDataSingelton = TripData.getInstance();

        Intent serviceIntent = new Intent(this, MusicPlayerService.class);
        startService(serviceIntent);
    }


    @Override
    public void onClick(View view) {
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
        if (view == submitQuestionBtn)
        {
            if (questionsNum < quizQuestions.size()-1)
            {
                String input = quizInputEt.getText().toString();
                System.out.println(quizTitles.get(questionsNum));
                tripDataSingelton.addAnswer(quizTitles.get(questionsNum), input);

                questionsNum++;
                quizTitleTv.setText(quizTitles.get(questionsNum));
                quizQuestionTv.setText(quizQuestions.get(questionsNum));
                quizInputEt.setText("");

            }
            else
            {
                Intent waitingQuizIntent = new Intent(getApplicationContext(), WaitingQuiz.class);
                startActivity(waitingQuizIntent); // redirect to show plan page
            }
        }
    }



}