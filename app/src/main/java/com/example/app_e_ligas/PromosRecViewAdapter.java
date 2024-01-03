package com.example.app_e_ligas;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.namespace.R;
import com.google.android.flexbox.FlexboxLayout;

import java.security.AccessControlContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PromosRecViewAdapter extends RecyclerView.Adapter<PromosRecViewAdapter.ViewHolder> {

    private ArrayList<PromoModel> promos = new ArrayList<>();

    FragmentManager parentFragmentManager;
    Context context;

    public PromosRecViewAdapter(Context context, FragmentManager parentFragmentManager) {
        this.context = context;
        this.parentFragmentManager = parentFragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promos_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PromoModel verifyingUser = promos.get(position);

        String dateString = verifyingUser.getDescription();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        holder.txtUsername.setText(verifyingUser.getEventTitle());
        holder.txtEmail.setText(verifyingUser.getDescription());
        Glide.with(context)
                .load(verifyingUser.getEventBanner())
                .into(holder.viewImageId);
        holder.verifyBtn.setText("JOIN");
    }

    @Override
    public int getItemCount() {
        return promos.size();
    }

    public void setPromos(ArrayList<PromoModel> promo) {
        this.promos = promo;
        Log.i("Promos", "Promos have been set to Adapter");
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername;
        TextView txtEmail;
        ImageView viewImageId;
        Button verifyBtn;
        FlexboxLayout parentBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            viewImageId = itemView.findViewById(R.id.viewImageId);
            verifyBtn = itemView.findViewById(R.id.verifyBtn);
            parentBtn = itemView.findViewById(R.id.parentBtn);
        }
    }
}
