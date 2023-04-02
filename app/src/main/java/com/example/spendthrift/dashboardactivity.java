package com.example.spendthrift;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.spendthrift.databinding.ActivityDashboardactivityBinding;
import com.example.spendthrift.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class dashboardactivity extends AppCompatActivity {
  ActivityDashboardactivityBinding binding;
  FirebaseFirestore firebaseFirestore;
  FirebaseAuth firebaseAuth;
  int sumExpense = 0;
  int sumIncome = 0;
  ArrayList<TransactionModel> transactionModelArrayList;
  TransactionAdapter transactionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        transactionModelArrayList = new ArrayList<>();
        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.historyRecyclerView.setHasFixedSize(true);
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    startActivity( new Intent(dashboardactivity.this , MainActivity.class));
                    finish();
                }
            }
        });

        binding.SignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSignOutDialog();
            }
        });
        binding.addFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(dashboardactivity.this,Addtransactionactivity.class));
                }
                catch (Exception e){

                }
            }
        });
        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(dashboardactivity.this , dashboardactivity.class));
                    finish();
                }
                catch(Exception e){

                }
            }
        });
        loadData();
    }
    private void createSignOutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder( dashboardactivity.this);
        builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete this?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    firebaseAuth.signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }


    private void loadData(){
        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       for(DocumentSnapshot ds : task.getResult()){
                           TransactionModel model = new TransactionModel(
                                   ds.getString("id"),
                                   ds.getString("note"),
                                  ds.getString("amount"),
                                   ds.getString("type"),
                                   ds.getString("data"));
                           int amount = Integer.parseInt(ds.getString("amount"));
                           if(ds.getString("type").equals("Expense")){
                               sumExpense = sumExpense + amount;
                           }
                           else
                           {
                               sumIncome = sumIncome + amount;
                           }
                           transactionModelArrayList.add(model);
                       }
                       binding.totalIncome.setText(String.valueOf(sumIncome));
                       binding.totalExpense.setText(String.valueOf(sumExpense));
                       binding.totalBalance.setText(String.valueOf(sumIncome - sumExpense));
                        transactionAdapter = new TransactionAdapter(dashboardactivity.this,transactionModelArrayList);
                        binding.historyRecyclerView.setAdapter(transactionAdapter);
                    }
                });
    }
}