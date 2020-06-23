package com.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText mTxtUsrName, mTxtPwd;
    private Button mBtnLogin, mBtnSignUp;
    Context context;
    private AwesomeValidation awesomeValidation;
    //HashMap is used for storing the username and passwords
    private HashMap<String, String> usersMap = new HashMap<String, String>();
    private static final int SIGN_UP_ACTIVITY_REQUEST_CODE = 1, WELCOME_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mBtnLogin = findViewById(R.id.button_login);
        mBtnSignUp = findViewById(R.id.button_signup);
        mTxtUsrName = findViewById(R.id.text_username);
        mTxtPwd = findViewById(R.id.text_password);
        context = getApplicationContext();
        awesomeValidation.addValidation(this, R.id.text_username, RegexTemplate.NOT_EMPTY, R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.text_password, RegexTemplate.NOT_EMPTY, R.string.pwdempty);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    String username = mTxtUsrName.getText().toString().trim();
                    if (usersMap.containsKey(username)) {
                        String password = mTxtPwd.getText().toString().trim();
                        if (usersMap.get(username).equals(password)) {
                            Intent welcome = new Intent(context, HomeScreen.class);
                            welcome.putExtra("usersMap", usersMap);
                            welcome.putExtra("username", username);
                            Log.v("MainActivity:onCreateMethod", "Verified User");
                            startActivityForResult(welcome, WELCOME_ACTIVITY_REQUEST_CODE);
                        } else {
                            mTxtPwd.setText("");
                            Toast.makeText(context, "Please enter a valid password", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        mTxtPwd.setText("");
                        Toast.makeText(context, "Invalid User", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signup = new Intent(context, SignUpActivity.class);
                signup.putExtra("usersMap", usersMap);
                startActivityForResult(signup, SIGN_UP_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    // onActivityResult method for processing the results retrieved from different activities.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.v("MainActivity:onActivityResult", "Received Result");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP_ACTIVITY_REQUEST_CODE || requestCode == WELCOME_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                usersMap.putAll((HashMap<String, String>) data.getSerializableExtra("usersMap"));
            }
        }
        //Clearing the views values
        mTxtPwd.setText("");
        mTxtUsrName.setText("");
    }
}