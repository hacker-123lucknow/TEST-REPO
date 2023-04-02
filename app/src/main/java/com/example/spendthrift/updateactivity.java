package com.example.spendthrift;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.spendthrift.databinding.ActivityUpdateactivityBinding;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class updateactivity extends AppCompatActivity {
     FirebaseAuth firebaseAuth;
     FirebaseFirestore firebaseFirestore;

    ActivityUpdateactivityBinding binding;
    String newType;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String id = getIntent().getStringExtra("id");
        String amount = getIntent().getStringExtra("amount");
        String note = getIntent().getStringExtra("note");
        String type = getIntent().getStringExtra("type");
        binding.userAmountAdd.setText(amount);
        binding.userNoteAdd.setText(note);
        switch (type)
        {
            case "Income":
                newType = "Income";
                binding.incomeCheckBox.setChecked(true);
                break;
            case "Expense":
                newType = "Expense";
                binding.expenseCheckBox.setChecked(true);
                break;
        }
        binding.incomeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newType = "Income";
                binding.incomeCheckBox.setChecked(true);
                binding.expenseCheckBox.setChecked(false);
            }
        });
        binding.expenseCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newType = "Expense";
                binding.incomeCheckBox.setChecked(false);
                binding.expenseCheckBox.setChecked(true);
            }
        });
        binding.btnUpdateTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = binding.userAmountAdd.getText().toString();
                String note = binding.userNoteAdd.getText().toString();
            firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
                    .collection("Notes").document(id)
                    .update("amount" , amount , "note" ,note , "newtype",type  )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(updateactivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(updateactivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
        binding.btnDeleteTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
                        .collection("Notes")
                        .document(id).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onBackPressed();
                                Toast.makeText(updateactivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(updateactivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
    }
}