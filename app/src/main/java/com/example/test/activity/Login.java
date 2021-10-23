package com.example.test.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.Database.MyDatabase;
import com.example.test.R;


public class Login extends AppCompatActivity {

    EditText username , password;
    Button login_btn;
    TextView forget_pass,signup;
    MyDatabase database;


    CheckBox rememberme;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    boolean login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        intiView();
        checkLogin();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,RecoveryPassword.class);
                startActivity(intent);
            }
        });
    }




    protected void intiView(){

        username=findViewById(R.id.username_login);
        password=findViewById(R.id.password_login);
        login_btn=findViewById(R.id.btn_login);
        forget_pass=findViewById(R.id.forget_password);
        signup=findViewById(R.id.go_sign_up_btn);
        database=new MyDatabase(this);

        rememberme=findViewById(R.id.remember);
        sharedPreferences=getSharedPreferences("remember file",MODE_PRIVATE);


    }

    protected void login() {

        String name = username.getText().toString();
        String pass = password.getText().toString();
        Cursor cursor = database.userLogin(name, pass);

        if (name.equals("admin") && pass.equals("admin")) {
            Intent intent = new Intent(Login.this, UploadProduct.class);
            startActivity(intent);
            finish();
        } else {


            if (cursor.getCount() <= 0)
                Toast.makeText(this, "Please Check username and password", Toast.LENGTH_SHORT).show();

            else {

                if (rememberme.isChecked())
                    keepLogin(name, pass);

                Toast.makeText(this, "Successfully login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
        }
    }
    protected void checkLogin(){
        login= sharedPreferences.getBoolean("login",false);
        if(login){
            Intent intent=new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void keepLogin(String username , String pass){
            editor=sharedPreferences.edit();
            editor.putString("username",username) ;
            editor.putString("password",pass);
            editor.putBoolean("login",true);
            editor.apply();

    }

}
