package com.sanjaykua.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText name,username,password,email;
    Button register;
    TextView textlogin;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.nameReg);
        username = findViewById(R.id.usernameReg);
        password = findViewById(R.id.passwordReg);
        email = findViewById(R.id.emailReg);
        register = findViewById(R.id.registerReg);
        textlogin = findViewById(R.id.txt_loginReg);
        mAuth = FirebaseAuth.getInstance();

        textlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Please Wait ..");
                progressDialog.show();

                String str_name = name.getText().toString();
                String str_username = username.getText().toString();
                String str_email = email.getText().toString();
                String str_pass = password.getText().toString();

                if (TextUtils.isEmpty(str_name) || TextUtils.isEmpty(str_username) ||
                        TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(RegisterActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else if (str_pass.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must have 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    register(str_name,str_username,str_email,str_pass);
                }
            }
        });
    }

        private void register(final String name, final String username, String email, String password){
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser firebaseUser=mAuth.getCurrentUser();
                                String userID= firebaseUser.getUid();

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

                                HashMap<String, Object> hashMap=new HashMap<>();
                                hashMap.put("id",userID);
                                hashMap.put("fullname", name);
                                hashMap.put("usermane", username.toLowerCase());
                                hashMap.put("bio","");
                                hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/task-37b6f.appspot.com/o/profilepic.png?alt=media&token=ee648ffb-2027-4d86-94cb-267f3abe4b0f");

                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Intent intent=new Intent(RegisterActivity.this,StartActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this,"You can't access with this email & password",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

             }
}
