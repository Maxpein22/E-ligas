package com.example.app_e_ligas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WecolmeAcvtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wecolme_acvtivity);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Emergency App", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Emergency App", "Failed to read value.", error.toException());
            }
        });

        // Set up button click listeners
        Button residentButton = findViewById(R.id.Resident);
        Button notResidentButton = findViewById(R.id.NotResident);

    }

    public void onButtonSignUpClicked(View view) {
        // Check which button triggered the click event
        int viewId = view.getId();

        if (viewId == R.id.Resident) {
            // Start SignInActivity if the "Resident" button is clicked
            Intent intent = new Intent(WecolmeAcvtivity.this, SignInActivity.class);
            startActivity(intent);

        } else if (viewId == R.id.NotResident) {
            // Start dam_monitoringActivity if the "Not Resident" button is clicked
            Intent intent = new Intent(WecolmeAcvtivity.this, dam_monitoringActivity.class);
            startActivity(intent);

        }
    }

}
