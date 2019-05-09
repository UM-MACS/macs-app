package com.example.project1;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseHelper db;
    private EditText e1,e2,e3,e5,e6,e7;
    private Button b1,b2;
    private Spinner spinner;
    private TextView inputLabel;
    private TextView mTextMessage;
//    private Patient patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent i1 = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(i1);
                        break;
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(RegisterActivity.this,emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(RegisterActivity.this,scheduleActivity.class);
                        startActivity(i3);
                        break;
                }
                return true;
            }
        });
        //finish

        db = new DatabaseHelper(this);
        e1 = (EditText)findViewById(R.id.email);
        e2 = (EditText)findViewById(R.id.confirm_password);
        e3 = (EditText)findViewById(R.id.confirm_password2);
        e5 = (EditText)findViewById(R.id.editText); //name
        e6 = (EditText)findViewById(R.id.editText3); //contact no
        e7 = (EditText)findViewById(R.id.editText4); //age
        b1 = (Button)findViewById(R.id.register);
        b2 = (Button)findViewById(R.id.button2);
//        patient = new Patient();

        //choose user type
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userType, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        //click on login button
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //click on register button
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
//                        Patient.getInstance().setPatientEmail(e1.getText().toString());
//                        Patient.getInstance().setPatientName(e5.getText().toString());
//                        Patient.getInstance().setPatientPassword(e2.getText().toString());
//                        Patient.getInstance().setPatientContact(e6.getText().toString());
//                        Patient.getInstance().setPatientage(e7.getText().toString());
                        String s1 = e1.getText().toString(); //email
                        String s2 = e2.getText().toString(); //pw
                        String s3 = e3.getText().toString(); //confirm pw
                        String s5 = e5.getText().toString(); //name
                        String s6 = e6.getText().toString(); //contact no
                        String s7 = e7.getText().toString(); //age
                        String s4 = spinner.getSelectedItem().toString(); //user type


                        //check if fields are empty
                        if(s1.equals("")||s2.equals("")||s3.equals("")||s4.equals("Please Select One")||s5.equals("")||s6.equals("")||s7.equals("")){
                            Toast.makeText(getApplicationContext(),"Field(s) are empty",Toast.LENGTH_SHORT).show();
                        }
                        else{ //check if password matches
                            if(s2.equals(s3)){
                                Boolean checkMail = db.checkMail(s1);
                                if(checkMail==true){
                                    if(s4.equals("Patient")) {
                                        Patient.getInstance().setPatientEmail(s1); //email
                                        Patient.getInstance().setPatientPassword(s2); //pw
                                        Patient.getInstance().setPatientName(s5);
                                        Patient.getInstance().setPatientContact(s6);
                                        Patient.getInstance().setPatientAge(s7);
                                        Log.e( "Tag", "hello");
                                        Boolean insert = db.insertPatientTable(s1,s5,s2,s6,s7);

                                        if (insert == true) {
                                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                  }
                                  else if(s4.equals("Caregiver")) {
                                        Caregiver.getInstance().setCaregiverEmail(s1);
                                        Caregiver.getInstance().setCaregiverPassword(s2);
                                        Caregiver.getInstance().setCaregiverName(s5);
                                        Caregiver.getInstance().setCaregiverContact(s6);
                                        Caregiver.getInstance().setCaregiverAge(s7);
                                        Boolean insert2 = db.insertCaregiverTable(s1,s5,s2,s6,s7);

                                        if (insert2 == true) {
                                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Email is Used, Please Re-enter", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_SHORT).show();
                            }
                        }

            }
        });
        mTextMessage = (TextView) findViewById(R.id.message);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

        parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> parent){

    }

}

