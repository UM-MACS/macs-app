package com.example.project1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class eventAssessment extends AppCompatActivity {
        DatabaseHelper db;
        Button b1;
        RadioGroup radio1;
        RadioButton radioButton1;
        TextView textView;
        EditText editText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_event_assessment);
            db = new DatabaseHelper(this);
            b1 = (Button)findViewById(R.id.button_submit_assessment);
            radio1 = (RadioGroup) findViewById(R.id.radioGroup);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int radioId = radio1.getCheckedRadioButtonId();
                    radioButton1 = (RadioButton)findViewById(radioId);
                    String text = radioButton1.getText().toString();
                    Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                    Boolean ins =db.insertEventAssessment("lim@gmail.com",text);
                    if(ins){
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                    }
                    Log.e("tag", ""+radioButton1.getText());
                }
            });

        }


    }


