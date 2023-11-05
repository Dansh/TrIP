package com.example.trip;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WaitingQuiz extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{


    TextView waitingQuizQuestionTv;
    WaitingQuizQuestion[] waitingQuizQuestions;
    Random random;
    RadioButton answer1, answer2, answer3, answer4;
    RadioButton[] answers;
    RadioGroup answersGroup;
    WaitingQuizQuestion currentQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_quiz);


        waitingQuizQuestionTv = findViewById(R.id.waitingQuizQuestionTv);
        answer1 = findViewById(R.id.waitingQuizAnswer1);
        answer2 = findViewById(R.id.waitingQuizAnswer2);
        answer3 = findViewById(R.id.waitingQuizAnswer3);
        answer4 = findViewById(R.id.waitingQuizAnswer4);

        answersGroup = findViewById(R.id.answersGroup);
        answersGroup.setOnCheckedChangeListener(this);

        answers = new RadioButton[]
                {
                        answer1, answer2, answer3, answer4
                };

        waitingQuizQuestions = getAllQuestions();

        random = new Random();


        currentQuestion = getNextQuestion();
        updateScreen();


        Intent serviceIntent = new Intent(this, MusicPlayerService.class);
        stopService(serviceIntent);

        String url = URL.getFullUrl();
        TripPlanRequestManager requestManager = new TripPlanRequestManager(url, new TripPlanRequestManager.OnTripPlanResponseListener() {
            @Override
            public void onTripPlanResponse(String response) {
                // Handle the trip plan response here
                // Create a Gson instance
                Gson gson = new Gson();
                // Define the type for the HashMap
                Type type = new TypeToken<HashMap<String, Object>>() {}.getType();
                // Parse the JSON string into a HashMap
                HashMap<String, Object> dataMap = gson.fromJson(response, type);
                // Print the HashMap
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(key + " -> " + value);
                }
                TripPlanData tripPlanData = TripPlanData.getInstance();
                tripPlanData.setHashMap(dataMap);
                Intent showPlanIntent = new Intent(getApplicationContext(), ShowPlan.class);
                startActivity(showPlanIntent);
                }
            @Override
            public void onTripPlanError(String error) {
                // Handle the trip plan error here
                Log.e(TAG, "Error: " + error);
            }
        });
        requestManager.execute();
    }



    public WaitingQuizQuestion[] getAllQuestions(){

        String[] questions =  new String[]{
                "What is the capital city of Australia?",
                "Which country is home to the Eiffel Tower?",
                "In which city is the Great Wall of China located?",
                "What is the currency used in Japan?",
                "Which country is famous for its tulips and windmills?",
                "What is the official language spoken in Brazil?",
                "Which city is known as the \"Big Apple\"?",
                "Which country is renowned for its ancient pyramids?",
                "What is the currency of Switzerland?",
                "In which city would you find the Colosseum?",
                "Which country is known for its stunning fjords?",
                "What is the official language spoken in Russia?",
                "Which city is home to the iconic Christ the Redeemer statue?",
                "Which country is famous for its kangaroos and koalas?",
                "What is the currency used in Mexico?",
                "In which city is the Acropolis located?",
                "Which country is known for its beautiful beaches and samba music?",
                "What is the official language spoken in Germany?",
                "Which city is often referred to as the \"City of Love\"?",
                "Which country is renowned for its vibrant culture and tango dancing?"
        };

        String[][] answerChoices = {
                {"Canberra", "Sydney", "Melbourne", "Perth"},  // Capital city of Australia
                {"France", "Italy", "Spain", "Germany"},  // Country with the Eiffel Tower
                {"Beijing", "Shanghai", "Hong Kong", "Guangzhou"},  // City with the Great Wall of China
                {"Yen", "Euro", "Dollar", "Pound"},  // Currency used in Japan
                {"Netherlands", "Sweden", "Denmark", "Norway"},  // Country with tulips and windmills
                {"Portuguese", "Spanish", "English", "Brazilian Portuguese"},  // Official language spoken in Brazil
                {"New York City", "Los Angeles", "Chicago", "Houston"},  // City known as the "Big Apple"
                {"Egypt", "Greece", "Mexico", "India"},  // Country renowned for ancient pyramids
                {"Swiss Franc", "Euro", "Pound", "Dollar"},  // Currency of Switzerland
                {"Rome", "Florence", "Venice", "Milan"},  // City with the Colosseum
                {"Norway", "Sweden", "Iceland", "Finland"},  // Country known for stunning fjords
                {"Russian", "German", "English", "French"},  // Official language spoken in Russia
                {"Rio de Janeiro", "Sao Paulo", "Brasilia", "Salvador"},  // City with the Christ the Redeemer statue
                {"Australia", "New Zealand", "Canada", "South Africa"},  // Country with kangaroos and koalas
                {"Peso", "Euro", "Dollar", "Yen"},  // Currency used in Mexico
                {"Athens", "Rome", "Cairo", "Paris"},  // City with the Acropolis
                {"Brazil", "Spain", "Mexico", "Greece"},  // Country with beautiful beaches and samba music
                {"German", "French", "English", "Spanish"},  // Official language spoken in Germany
                {"Paris", "Venice", "Rome", "Vienna"},  // City often referred to as the "City of Love"
                {"Argentina", "Brazil", "Spain", "Mexico"}  // Country renowned for vibrant culture and tango dancing
        };

        WaitingQuizQuestion[] waitingQuizQuestions = new WaitingQuizQuestion[questions.length];

        for (int i = 0; i < waitingQuizQuestions.length; i++)
        {
            waitingQuizQuestions[i] = new WaitingQuizQuestion(questions[i], answerChoices[i], answerChoices[i][0]);
        }

        return waitingQuizQuestions;
    }



    public WaitingQuizQuestion getNextQuestion(){

        int questionIndex = random.nextInt(waitingQuizQuestions.length);  // Generate a random index
        while (waitingQuizQuestions[questionIndex].IsAskedBefore())
        {
            questionIndex++;
            if (questionIndex == waitingQuizQuestions.length)
            {
                questionIndex = 0;
            }
        }
        waitingQuizQuestions[questionIndex].setAskedBefore(true);
        return waitingQuizQuestions[questionIndex];
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton pressedRB = findViewById(i);

        for (int j = 0; j < radioGroup.getChildCount(); j++) {
            radioGroup.getChildAt(j).setEnabled(false);
        }

        if (pressedRB.getText().toString().equals(currentQuestion.getAnswer())) {
            pressedRB.setTextColor(Color.GREEN);
        }
        else
        {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(j);
                if (radioButton != null) {
                    String text = radioButton.getText().toString();
                    if (text.equals(currentQuestion.getAnswer())) {
                        radioButton.setTextColor(Color.GREEN);
                    }
                }
            }
            pressedRB.setTextColor(Color.RED);

        }

        new Handler().postDelayed(
                new Runnable() {
            @Override
            public void run() {
                currentQuestion = getNextQuestion();
                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(j);
                    if (radioButton != null) {
                        radioButton.setTextColor(Color.rgb(48, 168, 255));
                    }
                }
                updateScreen();
                answersGroup.setOnCheckedChangeListener(null);
                pressedRB.setChecked(false);
                answersGroup.setOnCheckedChangeListener(WaitingQuiz.this);

                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    radioGroup.getChildAt(j).setEnabled(true);
                }
            }
        }, 1500); // Delay of 1 second (1000 milliseconds)

    }


    public void updateScreen(){
        waitingQuizQuestionTv.setText(currentQuestion.getQuestion());

        String[] mixedAnswers = currentQuestion.getMixedOptions();

        for (int i = 0; i < answers.length; i++)
        {
            answers[i].setText(mixedAnswers[i]);
        }

    }
}
