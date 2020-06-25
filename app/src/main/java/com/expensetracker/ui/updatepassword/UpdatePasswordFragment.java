/* Update Password Fragment to update password*/

package com.expensetracker.ui.updatepassword;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.expensetracker.DBHelper;
import com.expensetracker.R;

import static android.content.Context.MODE_PRIVATE;

public class UpdatePasswordFragment extends Fragment {

    private DBHelper mDBHelper;
    private  EditText mOldPwd,mNewPwd;
    private Button mBtnUpdatePwd;
    private String oldPwd,newPwd,username;
    private SharedPreferences prefs;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mDBHelper = new DBHelper(getContext());
        View root = inflater.inflate(R.layout.fragment_updatepassword, container, false);
        mOldPwd = root.findViewById(R.id.text_oldpwd);
        mNewPwd = root.findViewById(R.id.text_updatepwd);
        mBtnUpdatePwd = root.findViewById(R.id.button_updatepwd);
        prefs = getContext().getSharedPreferences("expensetracker", MODE_PRIVATE);
        username= prefs.getString("username", "");
        mBtnUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPwd = mOldPwd.getText().toString();
                newPwd = mNewPwd.getText().toString();
                if(mDBHelper.changePassword(username,oldPwd,newPwd)) {
                    Toast.makeText(getContext(), "Updated Password", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getContext(),"Unable to update password",Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }

}