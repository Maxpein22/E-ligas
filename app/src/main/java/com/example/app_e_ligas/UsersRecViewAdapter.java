package com.example.app_e_ligas;

import static com.example.app_e_ligas.barangay_cencus.censusEditingKey;
import static com.example.app_e_ligas.barangay_cencus.populateFields;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namespace.R;

import java.util.ArrayList;

public class UsersRecViewAdapter extends RecyclerView.Adapter<UsersRecViewAdapter.ViewHolder> {

    private ArrayList<UserCensus> services = new ArrayList<>();
    private ArrayList<ViewHolder> holders = new ArrayList<>();
    Context context;

    public UsersRecViewAdapter(Context context) {
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
        holders.add(position, holder);
        UserCensus service = services.get(position);
        holder.txtServiceName.setText(service.getFullName());
        holder.txtServicePrice.setVisibility(View.GONE);
        holder.parentBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                barangay_servicesActivity.selectedServices.clear();
                // Service doesn't exist, add it
                barangay_cencus.btnAddResponse.callOnClick();
                populateFields(service.toMap());
                censusEditingKey = service.getUserID();
            }
        });

    }


    @Override
    public int getItemCount() {
        return services.size();
    }

    public void setUsers(ArrayList<UserCensus> services) {
        this.services = services;
        Log.i("Services","Services has been set to Adapter");
        notifyDataSetChanged();

    }

    public ArrayList<ViewHolder> getAllHolder(){
        return holders;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtServiceName;
        public TextView txtServicePrice;
        private ScrollView listUsers;
        public CardView parentBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //listUsers = itemView.findViewById(R.id.listUsers);
            parentBtn = itemView.findViewById(R.id.parentBtn);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtServicePrice = itemView.findViewById(R.id.txtServicePrice);
        }
    }

}
