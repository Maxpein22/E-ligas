package com.example.app_e_ligas;

import static com.example.app_e_ligas.barangay_cencus.censusEditingKey;
import static com.example.app_e_ligas.barangay_cencus.populateFields;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namespace.R;

import java.util.ArrayList;
public class UsersRecViewAdapter extends RecyclerView.Adapter<UsersRecViewAdapter.ViewHolder> {

    private ArrayList<UserCensus> users = new ArrayList<>();
    private Context context;

    public UsersRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_service_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserCensus user = users.get(position);
        holder.txtServiceName.setText(user.getFullName());
        holder.txtServicePrice.setVisibility(View.GONE);
        holder.parentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barangay_cencus.btnAddResponse.callOnClick();
                populateFields(user.toMap());
                censusEditingKey = user.getUserID();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(ArrayList<UserCensus> users) {
        this.users = users;
        Log.i("Users", "Users have been set to Adapter");
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtServiceName;
        public TextView txtServicePrice;
        public CardView parentBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentBtn = itemView.findViewById(R.id.parentBtn);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtServicePrice = itemView.findViewById(R.id.txtServicePrice);
        }
    }
}
