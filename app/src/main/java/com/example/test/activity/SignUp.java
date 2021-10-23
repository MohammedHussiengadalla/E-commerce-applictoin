package com.example.test.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.test.Database.MyDatabase;
import com.example.test.Model.CustomerModel;
import com.example.test.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUp extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();

    EditText username , email,password,Birthday,Phone;
    Button signup_btn;
    Spinner gender;
    MyDatabase database;
    DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username=findViewById(R.id.username_signup);
        email=findViewById(R.id.email_signup);
        password=findViewById(R.id.password_signup);

        Birthday=findViewById(R.id.Birthday);
        Phone=findViewById(R.id.Phone);
        gender=findViewById(R.id.gender);
        signup_btn=findViewById(R.id.btn_signup);
        database=new MyDatabase(this);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        Birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SignUp.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Birthday.setText(sdf.format(myCalendar.getTime()));
    }
    
    protected void signUp() {
        String name = username.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String gen = gender.getSelectedItem().toString();
        String birthdate =  Birthday.getText().toString();
        String joptitle=Phone.getText().toString();

            if (name.equals("") || mail.equals("") || pass.equals(""))
                Toast.makeText(this, "Enter All Data,Please", Toast.LENGTH_SHORT).show();
            else {
                 CustomerModel customerModel=new CustomerModel(name,mail,pass,gen,joptitle,birthdate);
                database.insertCustomer(customerModel);
                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }


        }
    }
