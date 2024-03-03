package com.example.app_e_ligas;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namespace.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfficialsAdapter extends RecyclerView.Adapter<OfficialsAdapter.OfficialsViewHolder> {

    private List<BarangayOfficial> officials;

    public OfficialsAdapter(List<BarangayOfficial> officials) {
        this.officials = officials;
    }

    @NonNull
    @Override
    public OfficialsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barangay_official, parent, false);
        return new OfficialsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialsViewHolder holder, int position) {
        BarangayOfficial official = officials.get(position);
        holder.bind(official);
    }

    @Override
    public int getItemCount() {
        return officials.size();
    }

    public static class OfficialsViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImageView;
        private TextView fullNameTextView;
        private TextView positionTextView;

        public OfficialsViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
        }

        public void bind(final BarangayOfficial official) {
            // Load profile image using Picasso
            Picasso.get().load(official.getProfileImage()).into(profileImageView);
            fullNameTextView.setText(official.getFirstName() + " " + official.getMiddleName() + " " + official.getLastName());
            positionTextView.setText(official.getPosition());

            // Set OnClickListener to show full details when item clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetailsDialog(official);
                }
            });
        }

        private void showDetailsDialog(BarangayOfficial official) {
            // Inflate layout for the floating box
            View dialogView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.dialog_full_details, null);

            // Initialize TextViews in the floating box
            TextView addressTextView = dialogView.findViewById(R.id.addressTextView);
            TextView civilStatusTextView = dialogView.findViewById(R.id.civilStatusTextView);
            TextView emailTextView = dialogView.findViewById(R.id.emailTextView);
            TextView firstNameTextView = dialogView.findViewById(R.id.firstNameTextView);
            TextView genderTextView = dialogView.findViewById(R.id.genderTextView);
            TextView lastNameTextView = dialogView.findViewById(R.id.lastNameTextView);
            TextView middleNameTextView = dialogView.findViewById(R.id.middleNameTextView);
            TextView phoneNumberTextView = dialogView.findViewById(R.id.phoneNumberTextView);
            TextView positionTextView = dialogView.findViewById(R.id.positionTextView);
            ImageView profileImageView = dialogView.findViewById(R.id.profileImageView);

            // Set details for the selected official
            addressTextView.setText("Address: " + official.getAddress());
            civilStatusTextView.setText("Civil Status: " + official.getCivilStatus());
            emailTextView.setText("Email: " + official.getEmail());
            firstNameTextView.setText("First Name: " + official.getFirstName());
            genderTextView.setText("Gender: " + official.getGender());
            lastNameTextView.setText("Last Name: " + official.getLastName());
            middleNameTextView.setText("Middle Name: " + official.getMiddleName());
            phoneNumberTextView.setText("Phone Number: " + official.getPhoneNumber());
            positionTextView.setText("Position: " + official.getPosition());
            Picasso.get().load(official.getProfileImage()).into(profileImageView);
            
            // Create AlertDialog to display the floating box
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setView(dialogView);
            builder.setPositiveButton("OK", null); // You can add a button to dismiss the dialog if needed
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}