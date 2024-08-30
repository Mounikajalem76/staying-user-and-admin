package com.example.stayingloginpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        active = (Switch) findViewById(R.id.active);

        login = (Button) findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("login");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String input1 = username.getText().toString();
                        String input2 = password.getText().toString();
                       // Toast.makeText(MainActivity.this, input1, Toast.LENGTH_SHORT).show();
                        if (snapshot.child(input1).exists()) {
                            if (snapshot.child(input1).child("password").getValue(String.class).equals(input2)) {
                                if (active.isChecked()) {
                                    if (snapshot.child(input1).child("as").getValue(String.class).equals("admin")) {
                                        preferences.setDataLogin(MainActivity.this, true);
                                        preferences.setDataAs(MainActivity.this, "admin");
                                        startActivity(new Intent(MainActivity.this, AdminActivity.class));


                                    } else  if (snapshot.child(input1).child("as").getValue(String.class).equals("user")){
                                        preferences.setDataLogin(MainActivity.this, true);
                                        preferences.setDataAs(MainActivity.this, "user");
                                        startActivity(new Intent(MainActivity.this, UserActivity.class));

                                    }

                                } else {
                                    if (snapshot.child(input1).child("as").getValue(String.class).equals("admin")) {
                                        preferences.setDataLogin(MainActivity.this, false);
                                        startActivity(new Intent(MainActivity.this, AdminActivity.class));


                                    } else if (snapshot.child(input1).child("as").getValue(String.class).equals("user")){
                                        preferences.setDataLogin(MainActivity.this, false);
                                        startActivity(new Intent(MainActivity.this, UserActivity.class));


                                    }

                                }


                            } else {
                                Toast.makeText(MainActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "username wrong", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.getDataLogin(this)){
            if (preferences.getDataAs(this).equals("admin")){
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();

            }else if (preferences.getDataAs(this).equals("user")) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                finish();

            }
        }
    }
}