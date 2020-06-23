/* A signup activity which registers the user  */

package com.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText mTxtUsrName, mTxtPwd, mTxtRetypePwd, mTxtEmail, mTxtPhone;
    private Button mBtnSignUpNow;
    Context context;
    private AwesomeValidation awesomeValidation;
    private HashMap<String, String> usersMap = new HashMap<>();
    String username = "", password = "",email="",phone="",checkUsr="";
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Boolean mUserExits=false;
    String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mBtnSignUpNow = (Button) findViewById(R.id.button_signup);
        mTxtUsrName = (EditText) findViewById(R.id.text_usrsignup);
        mTxtPwd = (EditText) findViewById(R.id.text_pwdsignup);
        mTxtRetypePwd = (EditText) findViewById(R.id.text_retypepwd);
        mTxtEmail = (EditText) findViewById(R.id.text_email);
        mTxtPhone = (EditText) findViewById(R.id.text_phone);
        context = getApplicationContext();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        Log.v("SignUp Activity:onCreate", "Checking validation");
        mBtnSignUpNow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validateForm();
            }
        });

    }

    /* Validating different fields of registration*/
    private void validateForm() {
        validateUserName();
        validatePassword();
        validateEmail();
        validatePhone();

        if (awesomeValidation.validate()) {
            email=mTxtEmail.getText().toString();
            phone=mTxtPhone.getText().toString();
            createUser();

            Intent main_activity = new Intent(context, LoginActivity.class);
            usersMap.put(username, password);
            main_activity.putExtra("usersMap", usersMap);
            setResult(Activity.RESULT_OK, main_activity);
            finish();
        }
    }

    private void validateUserName() {
        Intent intent = getIntent();
        usersMap = (HashMap<String, String>) intent.getSerializableExtra("usersMap");
        username = mTxtUsrName.getText().toString().trim();
        // Custom Validator to check whether the username is unique or not
        awesomeValidation.addValidation(this, R.id.text_usrsignup, new SimpleCustomValidation() {
            @Override
            public boolean compare(final String input) {
                mFirebaseDatabase.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Get map of users in datasnapshot
                                collectUsers((Map<String,Object>) dataSnapshot.getValue(),input);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        });

                if (mUserExits)
                    return false;
                else
                    return true;
            }
        }, R.string.userexists);
        awesomeValidation.addValidation(this, R.id.text_usrsignup, RegexTemplate.NOT_EMPTY, R.string.userempty);
    }

    private void validatePassword() {
        String regexPwd = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[@#$%^&+=]).{4,}";
        awesomeValidation.addValidation(this, R.id.text_pwdsignup, regexPwd, R.string.pwderror);
        password = mTxtPwd.getText().toString().trim();
        String confirmPwd = mTxtRetypePwd.getText().toString().trim();
        if (confirmPwd == null || confirmPwd.length() == 0 || confirmPwd.isEmpty()) {
            awesomeValidation.addValidation(this, R.id.text_retypepwd, RegexTemplate.NOT_EMPTY, R.string.pwdempty);
        } else {
            awesomeValidation.addValidation(this, R.id.text_retypepwd, R.id.text_pwdsignup, R.string.confirmerror);
        }
    }

    private void validateEmail() {
        awesomeValidation.addValidation(this, R.id.text_email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
    }

    private void validatePhone() {
        awesomeValidation.addValidation(this, R.id.text_phone, "^[2-9]{1}[0-9]{9}$", R.string.mobileerror);
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser() {
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }
        User user = new User(email, username,password,phone);
        mFirebaseDatabase.child(userId).setValue(user);
        //addUserChangeListener();
    }

    private void collectUsers(Map<String,Object> users,String usrname) {

        ArrayList<String> username = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            if(usrname!=null && usrname==singleUser.get("username").toString())
            {
                mUserExits= true;
                return;
            }

        }
    }
}