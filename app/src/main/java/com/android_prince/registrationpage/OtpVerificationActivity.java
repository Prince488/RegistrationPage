package com.android_prince.registrationpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android_prince.registrationpage.databinding.ActivityOtpVerificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {

    ActivityOtpVerificationBinding binding;
    private FirebaseAuth firebaseAuth;
    String mVerificationId;
    private String TAG = "MAIN_TAG";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        progressShow();
        initiateOtp();
        otpVerification();
        binding.otpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.editTextOtp.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter 6 digit no.",Toast.LENGTH_LONG).show();
                }else if (binding.editTextOtp.getText().toString().length() !=6){
                    Toast.makeText(getApplicationContext(),"Please Enter 6 digit no.",Toast.LENGTH_LONG).show();
                }else{
                   verifyPhoneNumberWithCode(mVerificationId,binding.editTextOtp.getText().toString());
                   progressShow();
                }
            }
        });
    }

    private void initiateOtp(){
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
        signInWithPhoneAuthCredential(phoneAuthCredential);
            }
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                progressHide();
            }
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                progressHide();
            }
        };
    }
    private void otpVerification(){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth).
                        setPhoneNumber(getIntent().getStringExtra("mobile")).
                        setTimeout(60L,TimeUnit.SECONDS).setActivity(this).
                        setCallbacks(mCallbacks).
                        build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyPhoneNumberWithCode(String mVerificationId,String code){
    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
    signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(getIntent().getStringExtra("mobile"));
                        myRef.child("name").setValue(getIntent().getStringExtra("name"));
                        myRef.child("mobile").setValue(getIntent().getStringExtra("mobile")).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(OtpVerificationActivity.this,ProfileActivity.class);
                                intent.putExtra("name1",getIntent().getStringExtra("name"));
                                intent.putExtra("mobile1",getIntent().getStringExtra("mobile"));
                                startActivity(intent);
                                progressHide();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressHide();
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