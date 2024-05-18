package com.example.app_e_ligas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.namespace.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PromoBottomSheetFragment extends BottomSheetDialogFragment {

    private TextView txtUsername;
    private TextView txtEmail;
    private TextView txtOrganizerName;
    private TextView txtStartDate;
    private TextView txtEndDate;
    private ImageView viewImageId;
    private TextView txtFacebookLink;
    private FlexboxLayout parentBtn;
    private FlexboxLayout moreInfoContainer;
    PromoModel promo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemView =  inflater.inflate(R.layout.promos_list, container, false);
        txtUsername = itemView.findViewById(R.id.txtUsername);
        txtEmail = itemView.findViewById(R.id.txtEmail);
        txtOrganizerName = itemView.findViewById(R.id.txtOrganizerName);
        txtStartDate = itemView.findViewById(R.id.txtStartDate);
        txtEndDate = itemView.findViewById(R.id.txtEndDate);
        viewImageId = itemView.findViewById(R.id.viewImageId);
        txtFacebookLink = itemView.findViewById(R.id.txtFacebookLink);
        parentBtn = itemView.findViewById(R.id.parentBtn);
        moreInfoContainer = itemView.findViewById(R.id.moreInfoContainer);
        return itemView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(txtUsername == null)return;
        txtUsername.setText(promo.getEventTitle());
        txtUsername.setText(promo.getEventTitle());
        txtEmail.setText(promo.getDescription());
        txtOrganizerName.setText(promo.getOrganizerName());
        txtStartDate.setText(firebaseDateFormat(promo.getStartDate()));
        txtEndDate.setText(firebaseDateFormat(promo.getEndDate()));
        Glide.with(this).load(promo.getEventBanner()).into(viewImageId);
        parentBtn.setPadding(40,60,40,500);
        txtFacebookLink.setVisibility(View.VISIBLE);
        moreInfoContainer.setVisibility(View.VISIBLE);
        // Retrieve the Facebook link from Firebase
        String facebookLink = promo.getEventLink();

        // Set an onClickListener on the Facebook link TextView
        txtFacebookLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the Facebook link
                if (facebookLink != null && !facebookLink.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(facebookLink));

                    // Check if Facebook app is installed, if yes, open the link in it
                    intent.setPackage("com.facebook.katana");

                    // Check if there's an app to handle this intent
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        // If Facebook app is not installed, open the link in a browser
                        intent.setPackage(null);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    public static String firebaseDateFormat(String dashedDate){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

        try {
            Date date = inputFormat.parse(dashedDate);
            String formattedDate = outputFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dashedDate;
    }

    void updateContent(PromoModel promo){
        this.promo = promo;
    }



}
