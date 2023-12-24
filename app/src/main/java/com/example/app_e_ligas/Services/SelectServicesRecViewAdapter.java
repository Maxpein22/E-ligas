package com.example.app_e_ligas.Services;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_e_ligas.barangay_servicesActivity;
import com.example.namespace.R;

import java.security.AccessControlContext;
import java.util.ArrayList;

public class SelectServicesRecViewAdapter extends RecyclerView.Adapter<SelectServicesRecViewAdapter.ViewHolder> {

    private ArrayList<ServiceModel> services = new ArrayList<>();
    Context context;

    public SelectServicesRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_service_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceModel service = services.get(position);
        holder.txtServiceName.setText(service.getServiceName());
        holder.txtServicePrice.setVisibility(View.GONE);
        holder.parentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (barangay_servicesActivity.selectedServices.contains(service)) {
                    // Service already exists, remove it
                    barangay_servicesActivity.selectedServices.remove(service);
                    if(service.getServiceName().equals("Others"))barangay_servicesActivity.showOthers(false);
                    ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#ffffff"));
                    ViewCompat.setBackgroundTintList(holder.parentBtn, colorStateList);
                    holder.txtServiceName.setTextColor(Color.parseColor("#212121"));
                    holder.txtServicePrice.setTextColor(Color.parseColor("#212121"));
                } else {
                    // Service doesn't exist, add it
                    if(service.getServiceName().equals("Others"))barangay_servicesActivity.showOthers(true);
                    barangay_servicesActivity.selectedServices.add(service);
                    ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#4496ca"));
                    ViewCompat.setBackgroundTintList(holder.parentBtn, colorStateList);
                    holder.txtServiceName.setTextColor(Color.parseColor("#ffffff"));
                    holder.txtServicePrice.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return services.size();
    }

    public void setUsers(ArrayList<ServiceModel> services) {
        this.services = services;
        Log.i("Services","Services has been set to Adapter");
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtServiceName;
        private TextView txtServicePrice;
        private ScrollView listUsers;
        private CardView parentBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //listUsers = itemView.findViewById(R.id.listUsers);
            parentBtn = itemView.findViewById(R.id.parentBtn);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtServicePrice = itemView.findViewById(R.id.txtServicePrice);
        }
    }

}
