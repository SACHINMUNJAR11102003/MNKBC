package com.example.mukherjinagarkbc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.net.ssl.SSLSocketFactory;

public class MainActivity extends AppCompatActivity {
    CountDownTimer countDownTimer;
    int time;
    private final long startTimeInMillis = 30 * 1000; // 1 minute in milliseconds
    private long timeLeftInMillis = startTimeInMillis;
    Button skip_button,next_button;
    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }
    int skipped=0;

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped() {
        this.skipped =skipped+1;
    }

    TextView question_textview,timer_textview,skippped_textview;
    RadioButton option1_textview,option2_textview,
            option3_textview,option4_textview;

    ArrayList<QuestionModel> questionModelArrayList;
    int currentQuestionIndex = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        skip_button=findViewById(R.id.skip_question_button);
        next_button=findViewById(R.id.next_question_button);
        question_textview=findViewById(R.id.question_textveiw);
        option1_textview=findViewById(R.id.option1);
        option2_textview=findViewById(R.id.option2);
        option3_textview=findViewById(R.id.option3);
        option4_textview=findViewById(R.id.option4);
        skippped_textview=findViewById(R.id.skipped_textview);
        timer_textview=findViewById(R.id.timer_textview);


        questionModelArrayList=new ArrayList<>();

        /*SSLSocketFactory sslSocketFactory = null;
        try {
            //sslSocketFactory = SSLUtil.getSSLSocketFactory(this, R.raw.my_certificate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a new HurlStack with the custom SSLSocketFactory
        HurlStack hurlStack = new HurlStack(null, sslSocketFactory);*/

        // Initialize the RequestQueue with the custom HurlStack
        //RequestQueue requestQueue = Volley.newRequestQueue(this, hurlStack);
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        String url="http://192.168.56.1:8080/questions";

        Toast.makeText(this, "reached 0", Toast.LENGTH_SHORT).show();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    Toast.makeText(MainActivity.this, " reached 1", Toast.LENGTH_SHORT).show();

                    for (int i=0;i<response.length();i++){
                        JSONObject jsonObject=response.getJSONObject(i);
                        String question=jsonObject.getString("question");
                        String option_1=jsonObject.getString("option_1");
                        String option_2=jsonObject.getString("option_2");
                        String option_3=jsonObject.getString("option_3");
                        String option_4=jsonObject.getString("option_4");
                        int right_ans=jsonObject.getInt("right_ans");
                        int ques_time=jsonObject.getInt("ques_time");

                        questionModelArrayList.add(new QuestionModel(question,option_1,option_2,option_3,option_4,right_ans,ques_time));

                        Toast.makeText(MainActivity.this, "reached 2", Toast.LENGTH_SHORT).show();

                    }

                    Toast.makeText(MainActivity.this, "data fetched successfully", Toast.LENGTH_SHORT).show();


                    if (!questionModelArrayList.isEmpty()){
                        Toast.makeText(MainActivity.this, "list is not empty", Toast.LENGTH_SHORT).show();
                        //displayQuestion(currentQuestionIndex);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "No Questions available", Toast.LENGTH_SHORT).show();
                    }

                }
                 catch (JSONException e) {
                    throw new RuntimeException(e);
                 }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, ""+questionModelArrayList.size(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(error.getMessage());
            }
        });

        requestQueue.add(jsonArrayRequest);

        //questionModelArrayList=new ArrayList<>();
        //questionModelArrayList.add(new QuestionModel("Who is Prime Minister Of India ?","Draupdi Murmu","Narendra Modi","S Jayshankar","Yogi Adityanath",30));
        //questionModelArrayList.add(new QuestionModel("Who is President Of India ?","Draupdi Murmu","Narendra Modi","S Jayshankar","Yogi Adityanath",20));
        //questionModelArrayList.add(new QuestionModel("Who is Foriegn Minister Of India ?","Draupdi Murmu","Narendra Modi","S Jayshankar","Yogi Adityanath",30));
        //questionModelArrayList.add(new QuestionModel("Who is Chief Minister Of UP ?","Draupdi Murmu","Narendra Modi","S Jayshankar","Yogi Adityanath",20));


        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //moveToNextQuestion();
                setSkipped();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //moveToNextQuestion();
            }
        });


        long startTimeInMillis = getTime() * 1000;

        startTimer(startTimeInMillis);

    }


    private void startTimer(long startTimeInMillis) {
        // Cancel the previous timer if it exists
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(startTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                //moveToNextQuestion();
                updateCountdownText();
            }
        }.start();
    }

    private void displayQuestion(int index) {
        QuestionModel currentQuestion = questionModelArrayList.get(index);
        // Display the current question (You can update UI accordingly)
        String question=currentQuestion.question;
        String option1=currentQuestion.option1;
        String option2= currentQuestion.option2;
        String option3= currentQuestion.option3;
        String option4= currentQuestion.option4;
        int get_time=currentQuestion.time;
        setTime(get_time);
        skippped_textview.setText(String.valueOf(getSkipped()));


        question_textview.setText(question);
        option1_textview.setText(option1);
        option2_textview.setText(option2);
        option3_textview.setText(option3);
        option4_textview.setText(option4);

        startTimer(startTimeInMillis);
    }

    private void updateCountdownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d", seconds);
        timer_textview.setText(timeLeftFormatted);
    }

   /* private void moveToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionModelArrayList.size()) {
            // If there are more questions, display the next question
            displayQuestion(currentQuestionIndex);
        } else {
            // If there are no more questions, display a message or handle as needed
            Toast.makeText(MainActivity.this, "reached 3", Toast.LENGTH_SHORT).show();
            // You can reset the index to 0 to start from the beginning again
            // currentQuestionIndex = 0;
        }
    }*/

}