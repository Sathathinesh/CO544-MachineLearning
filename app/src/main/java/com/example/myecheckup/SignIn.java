package com.example.myecheckup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myecheckup.Common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {
    EditText edtPhone , edtPassword ;
    Button btnSignIn ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignIn = findViewById(R.id.btnSignIn);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);

        // Init Firebase

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting ....");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get User information

                    if(dataSnapshot.child(edtPhone.getText().toString()).exists()){
                        mDialog.dismiss();
                    com.example.myecheckup.Model.User user= dataSnapshot.child(edtPhone.getText().toString()).getValue(com.example.myecheckup.Model.User.class);
                        if (user != null) {
                            if(user.getPassword().equals(edtPassword.getText().toString())){
                                Intent homeIntent= new Intent(SignIn.this,Home.class);
                                Common.currentUser=user ;
                                startActivity(homeIntent);
                                finish();
                            }
                           else {
                         Toast.makeText(SignIn.this, "Sign In Failed !!!!", Toast.LENGTH_LONG).show();
                             }
                        }
                    }
                    else {
                        mDialog.dismiss();
                        Toast.makeText(SignIn.this,"User not exit", Toast.LENGTH_LONG).show();
                     }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
