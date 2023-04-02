package com.example.spendthrift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.spendthrift.databinding.ActivityDashboardactivityBinding;
import com.example.spendthrift.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
     ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       firebaseAuth = firebaseAuth.getInstance();
       firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser()!= null){
                   try{
                       startActivity(new Intent(MainActivity.this , dashboardactivity.class));
                       finish();
                   }
                   catch(Exception e)
                   {

                   }
               }
           }
       });
       binding.goToSignUpscreen.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, signupActivity.class);
               try {
                   startActivity(intent);
               } catch (Exception e) {

               }
           }
       });
       binding.btnLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email = binding.emailLogin.getText().toString().trim();
               String password = binding.passwordLogin.getText().toString().trim();
               if(email.length() <= 0 || password.length() <= 0){
                   return;
               }
               firebaseAuth.signInWithEmailAndPassword(email ,password)
                       .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {
                       try {
                           startActivity(new Intent(MainActivity.this , dashboardactivity.class));
                       }
                       catch(Exception e)
                       {

                       }
                   }
                   })  .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               });
           }
       });
    }
    }

