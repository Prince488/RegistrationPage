package com.android_prince.registrationpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android_prince.registrationpage.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        activityMainBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!activityMainBinding.editTextName.getText().toString().isEmpty())&&(!activityMainBinding.editTextMobile.getText().toString().isEmpty())&&(activityMainBinding.editTextMobile.getText().toString().length()==13)){
                    Intent intent = new Intent(MainActivity.this,OtpVerificationActivity.class);
                    intent.putExtra("name",activityMainBinding.editTextName.getText().toString().trim());
                    intent.putExtra("mobile",activityMainBinding.editTextMobile.getText().toString());
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Please Enter Name and Mobile No.",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}