package com.example.app_e_ligas;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PendingReport extends AppCompatActivity {
    Authentication auth;
    FlexboxLayout pendingStatus;
    FlexboxLayout otwStatus;
    FlexboxLayout rejectedStatus;
    Button helpReceived;
    TextView rejectReason;
    Button acknowledgeRejection; // Declare the button


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_report);
        // Remove top nav title
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        pendingStatus = findViewById(R.id.pendingStatus);
        otwStatus = findViewById(R.id.otwStatus);
        rejectedStatus = findViewById(R.id.rejectedStatus);
        helpReceived = findViewById(R.id.helpReceived);
        rejectReason = findViewById(R.id.rejectReason);
        acknowledgeRejection = findViewById(R.id.acknowledgeRejection); // Initialize the button

        acknowledgeRejection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveRejectedReportToHistory();
            }
        });
        helpReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHelpReceived();
            }
        });

        auth = new Authentication(this, null);
        listenToReportChange();
    }

    private void setHelpReceived() {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("reports").child(auth.getUser().getUid());
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    ReportModel report = task.getResult().getValue(ReportModel.class);
                    String key = FirebaseDatabase.getInstance().getReference("reportsHistory").push().getKey();
                    DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference()
                            .child("reportsHistory")
                            .child(key);

                    historyReference.setValue(report)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    historyReference.child("date").setValue(System.currentTimeMillis())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Write was successful!
                                                    mPostReference.removeValue();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Write failed
                                                    System.out.println(e.getMessage());
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    System.out.println(e.getMessage());
                                }
                            });

                }
            }
        });

    }

    private void moveRejectedReportToHistory() {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("reports").child(auth.getUser().getUid());
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    ReportModel report = task.getResult().getValue(ReportModel.class);
                    // Ensure the status is set to "rejected"
                    report.setStatus("rejected");
                    String key = FirebaseDatabase.getInstance().getReference("reportsHistory").push().getKey();
                    DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference()
                            .child("reportsHistory")
                            .child(key);

                    historyReference.setValue(report)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    historyReference.child("date").setValue(System.currentTimeMillis())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Write was successful!
                                                    mPostReference.removeValue();
                                                    // Finish the activity to go back to the previous screen
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Write failed
                                                    Log.e("firebase", "Error writing date to history", e);
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    Log.e("firebase", "Error moving report to history", e);
                                }
                            });
                }
            }
        });
    }


    private void listenToReportChange() {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("reports").child(auth.getUser().getUid());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Intent intent = new Intent(PendingReport.this, barangay_emergencyActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    ReportModel report = dataSnapshot.getValue(ReportModel.class);
                    if (report.getStatus().equals("pending")) {
                        pendingStatus.setVisibility(View.VISIBLE);
                        otwStatus.setVisibility(View.GONE);
                        rejectedStatus.setVisibility(View.GONE);
                    } else if (report.getStatus().equals("rejected")) {
                        pendingStatus.setVisibility(View.GONE);
                        otwStatus.setVisibility(View.GONE);
                        rejectedStatus.setVisibility(View.VISIBLE);
                        rejectReason.setText(report.getRejectReason());
                    } else {
                        pendingStatus.setVisibility(View.GONE);
                        otwStatus.setVisibility(View.VISIBLE);
                        rejectedStatus.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}
