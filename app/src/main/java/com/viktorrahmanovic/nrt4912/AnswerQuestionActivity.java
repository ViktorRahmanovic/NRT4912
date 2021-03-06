package com.viktorrahmanovic.nrt4912;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AnswerQuestionActivity extends AppCompatActivity {

    int student;
    int question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);


        Intent i = getIntent();

        student = i.getIntExtra("CurrentStudent", 0);
        question = i.getIntExtra("CurrentQuestion", 0);


        if (question == 0) {
            ProfActivity.answers[student] = new ArrayList<>();
            for (int j = 0; j<ProfActivity.questions.size();j++) {
                Question q = ProfActivity.questions.get(j).copy();
                ProfActivity.answers[student].add(q);
            }
        }

        Question currentQuestion = ProfActivity.answers[student].get(question);
        // postavljamo Interface

        ((TextView)findViewById(R.id.tvQuestion)).setText("Pitanje: " + currentQuestion.getQuestionText());

        LinearLayout llRoot = ((LinearLayout)findViewById(R.id.llRoot));
        if(currentQuestion.getClass().equals(TextQuestion.class)){
            EditText etAnswer = new EditText(this);
            etAnswer.setId(R.id.etAnswer);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
            etAnswer.setLayoutParams(lp);
            llRoot.addView(etAnswer);



        } else if (currentQuestion.getClass().equals(RadioQuestion.class)) {


            String[] answers = ((RadioQuestion)currentQuestion).getAnswers();
            int noOfAnswers = answers.length;
                    //((RadioQuestion) currentQuestion).getAnswers();
            RadioGroup rgAnswers = new RadioGroup(this);
            rgAnswers.setId(R.id.rgAnswers);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgAnswers.setLayoutParams(lp);
            llRoot.addView(rgAnswers);

            for (int j = 0; j<noOfAnswers; j++) {
                RadioButton rbAnswer = new RadioButton(this);
                rbAnswer.setText(answers[j]);
                rbAnswer.setLayoutParams(lp);
                rgAnswers.addView(rbAnswer);

            }

        } else if (currentQuestion.getClass().equals(CheckboxQuestion.class)) {

            String[] answers = ((CheckboxQuestion)currentQuestion).getAnswers();
            int noOfAnswers = answers.length;
            //((RadioQuestion) currentQuestion).getAnswers();

            LinearLayout llAnswers = new LinearLayout(this);
            llAnswers.setOrientation(LinearLayout.VERTICAL);
            llAnswers.setId(R.id.llAnswers);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llAnswers.setLayoutParams(lp);
            llRoot.addView(llAnswers);

            for (int j = 0; j<noOfAnswers; j++) {
                CheckBox cbAnswer = new CheckBox(this);
                cbAnswer.setText(answers[j]);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
             //   lp2.setMargins(0,0,0,0);
                cbAnswer.setLayoutParams(lp2);
                llAnswers.addView(cbAnswer);

            }
        }



    }

    public void nextQuestionClicked(View view) {

        Question currentQuestion = ProfActivity.answers[student].get(question);

        if(currentQuestion.getClass().equals(TextQuestion.class)) {
            EditText etAnswer = (EditText) findViewById(R.id.etAnswer);
            ((TextQuestion)currentQuestion).setAnswerText(etAnswer.getText().toString());
        }
        else if (currentQuestion.getClass().equals(RadioQuestion.class)) {

            RadioGroup rgAnswers = (RadioGroup) findViewById(R.id.rgAnswers);
            RadioButton rbAnswer = (RadioButton) findViewById(rgAnswers.getCheckedRadioButtonId());
            ((RadioQuestion)currentQuestion).setSelectedAnswer(rbAnswer.getText().toString());
        }
        else if (currentQuestion.getClass().equals(CheckboxQuestion.class)) {

            LinearLayout llAnswers = (LinearLayout) findViewById(R.id.llAnswers);
            String[] selectedAnswers = new String[llAnswers.getChildCount()];
            for (int i = 0,j=0; i < llAnswers.getChildCount(); i++) {
                CheckBox cbCbc = (CheckBox) llAnswers.getChildAt(i);
                if(cbCbc.isChecked()) {
                    selectedAnswers[j++]= cbCbc.getText().toString();
                }
            }

            ((CheckboxQuestion)currentQuestion).setSelectedAnswers(selectedAnswers);

        }

        Log.d("Odgovori: ", ProfActivity.answers[student].toString());

            finish();
        question++;
        if(question == ProfActivity.questions.size()){
            return;
        }

        Intent intent = new Intent(this, AnswerQuestionActivity.class);
        intent.putExtra("CurrentQuestion", question);
        intent.putExtra("CurrentStudent", student);
        startActivity(intent);

    }
}
