package com.android_prince.registrationpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android_prince.registrationpage.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        progressShow();
        DatabaseReference myRef = database.getReference(getIntent().getStringExtra("mobile1"));
        myRef.child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase","Error getting data", task.getException());
                    progressHide();
                    finish();
                }else {
                    progressHide();
                    binding.txtUserName.setText("Name : "+ task.getResult().getValue());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Something went wrong...",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        myRef.child("mobile").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    progressHide();
                    Log.e("firebase","Error getting data", task.getException());
                    finish();
                }else {
                    progressHide();
                    binding.txtUserMobile.setText("Mobile no. : "+ task.getResult().getValue());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressHide();
                Toast.makeText(getApplicationContext(),"Something went wrong...",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void progressShow() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }
    private void progressHide() {
        binding.progressBar.setVisibility(View.INVISIBLE);
    }
}