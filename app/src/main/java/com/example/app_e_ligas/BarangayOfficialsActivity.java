package com.example.app_e_ligas;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityBarangayOfficialsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BarangayOfficialsActivity extends DrawerBasedActivity {

    private ActivityBarangayOfficialsBinding activityBarangayOfficialsBinding;
    private RecyclerView recyclerView;
    private OfficialsAdapter officialsAdapter;
    private List<BarangayOfficial> officials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarangayOfficialsBinding = ActivityBarangayOfficialsBinding.inflate(getLayoutInflater());
        setContentView(activityBarangayOfficialsBinding.getRoot());
        allocateActivityTitle("Barangay Officials");

        recyclerView = findViewById(R.id.recyclerView);
        officials = new ArrayList<>();
        officialsAdapter = new OfficialsAdapter(officials);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(officialsAdapter);

        fetchBarangayOfficials();
    }

    private void fetchBarangayOfficials() {
        DatabaseReference officialsRef = FirebaseDatabase.getInstance().getReference().child("officials");
        officialsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                officials.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarangayOfficial official = snapshot.getValue(BarangayOfficial.class);
                    officials.add(official);
                }
                officialsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to fetch barangay officials: " + databaseError.getMessage());
            }
        });
    }
}
