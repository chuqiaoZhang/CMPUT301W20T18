package com.example.bee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static com.example.bee.RegistrationActivity.isNumeric;

public class EditProfileActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private ImageView logo;
    private EditText username, email, phone;
    private Button saveBt;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseFirestore db;
    String userID;
    ProgressBar progressBar;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        initializeGUI();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");

        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());



        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isNumeric(phone.getText().toString())){
                    Toast.makeText(EditProfileActivity.this,"invalid phone number",Toast.LENGTH_SHORT).show();
                    return;

                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() ){
                    Toast.makeText(EditProfileActivity.this,"invalid email",Toast.LENGTH_SHORT).show();
                    return;

                }

                ref.child(userID).child("phone").setValue(phone.getText().toString());
                ref.child(userID).child("email").setValue(email.getText().toString());

                startActivity(new Intent(EditProfileActivity.this, DrawerActivity.class));
            }
        });

    }


    private void initializeGUI() {

        logo = findViewById(R.id.ivRegLogo);
        username = findViewById(R.id.inputName);
        phone = findViewById(R.id.inputPhone);
        email = findViewById(R.id.inputEmail);

        saveBt = findViewById(R.id.btnSave);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");
        ref.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    username.setText(dataSnapshot.child("Name").getValue().toString());
                    phone.setText(dataSnapshot.child("phone").getValue().toString());
                    email.setText(dataSnapshot.child("email").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}



