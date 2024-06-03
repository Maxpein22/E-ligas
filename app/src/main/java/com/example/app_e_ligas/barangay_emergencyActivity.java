package com.example.app_e_ligas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityBarangayEmergencyBinding;

public class barangay_emergencyActivity extends DrawerBasedActivity {
    ActivityBarangayEmergencyBinding activityBarangayEmergencyBinding;

    RelativeLayout fireBtn;
    RelativeLayout earthquakeBtn;
    RelativeLayout typhoonBtn;
    RelativeLayout robberyBtn;
    RelativeLayout injuriesBtn;
    RelativeLayout othersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarangayEmergencyBinding = ActivityBarangayEmergencyBinding.inflate(getLayoutInflater());
        setContentView(activityBarangayEmergencyBinding.getRoot());

        String type = "fire";
        Intent intent = new Intent(getBaseContext(), SubmitReport.class);
        Bundle b = new Bundle();
        System.out.println(type);
        b.putString("emergencyType", type); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);

        allocateActivityTitle("Barangay Emergency");
        fireBtn = findViewById(R.id.fireBtn);
        earthquakeBtn = findViewById(R.id.earthquakeBtn);
        typhoonBtn = findViewById(R.id.typhoonBtn);
        robberyBtn = findViewById(R.id.robberyBtn);
        injuriesBtn = findViewById(R.id.injuriesBtn);
        othersBtn = findViewById(R.id.othersBtn);

        fireBtn.setOnClickListener(emergencySubmit);
        earthquakeBtn.setOnClickListener(emergencySubmit);
        typhoonBtn.setOnClickListener(emergencySubmit);
        robberyBtn.setOnClickListener(emergencySubmit);
        injuriesBtn.setOnClickListener(emergencySubmit);
        othersBtn.setOnClickListener(emergencySubmit);
    }

    View.OnClickListener emergencySubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String type = view.getResources().getResourceEntryName(view.getId()); //get the id of the clicked button
            type = type.substring(0, type.length() - 3); //remove the last 3 char since the id always ends in "Btn"
            Intent intent = new Intent(getBaseContext(), SubmitReport.class);
            Bundle b = new Bundle();
            System.out.println(type);
            b.putString("emergencyType", type); //Your id
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
        }
    };

}