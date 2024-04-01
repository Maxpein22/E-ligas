package com.example.app_e_ligas;

import android.content.Context;
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

import java.util.ArrayList;

public class PromosRecViewAdapter extends RecyclerView.Adapter<PromosRecViewAdapter.ViewHolder> {

    private ArrayList<PromoModel> promos = new ArrayList<>();
    private Context context;

    public PromosRecViewAdapter(Context context, FragmentManager supportFragmentManager) {
        this.context = context;
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
        PromoModel promo = promos.get(position);
        holder.bind(promo);
    }

    @Override
    public int getItemCount() {
        return promos.size();
    }

    public void setPromos(ArrayList<PromoModel> promos) {
        this.promos = promos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUsername;
        private TextView txtEmail;
        private TextView txtOrganizerName;
        private TextView txtStartDate;
        private TextView txtEndDate;
        private ImageView viewImageId;
        private Button verifyBtn;
        private FlexboxLayout parentBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtOrganizerName = itemView.findViewById(R.id.txtOrganizerName);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
            viewImageId = itemView.findViewById(R.id.viewImageId);
            verifyBtn = itemView.findViewById(R.id.verifyBtn);
            parentBtn = itemView.findViewById(R.id.parentBtn);
        }

        public void bind(PromoModel promo) {
            txtUsername.setText(promo.getEventTitle());
            txtEmail.setText(promo.getDescription());
            txtOrganizerName.setText(promo.getOrganizerName());
            txtStartDate.setText(promo.getStartDate());
            txtEndDate.setText(promo.getEndDate());
            Glide.with(context).load(promo.getEventBanner()).into(viewImageId);
            verifyBtn.setText("JOIN");
        }
    }
}
