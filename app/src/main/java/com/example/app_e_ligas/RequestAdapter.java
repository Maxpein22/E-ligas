package com.example.app_e_ligas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namespace.R;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.YourViewHolder> {

    private List<Request> dataList;
    private Context context;

    public RequestAdapter(List<Request> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public YourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout, parent, false);
        return new YourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourViewHolder holder, int position) {
        Request item = dataList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class YourViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtSubtitle;
        private Button statusBtn;
        public YourViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle); // Replace with actual ID
            txtSubtitle = itemView.findViewById(R.id.txtSubtitle); // Replace with actual ID
            statusBtn = itemView.findViewById(R.id.statusBtn); // Replace with actual ID
        }

        public void bind(Request item) {

            txtTitle.setText(item.getType());
            txtSubtitle.setText(item.getDescription());

            String status = item.getStatus();
            String resultString = status.replace("-", " ");
            statusBtn.setText(resultString);
            statusBtn.setEnabled(false);
            statusBtn.setTextColor(ContextCompat.getColorStateList(context, R.color.black));
            switch (item.getStatus()) {
                case "on-going":
                    statusBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.onGoingColor));
                    break;
                case "ready-to-claim":
                    statusBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.readyToClaimColor));
                    break;
                case "claimed":
                    statusBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.claimedColor));
                    break;
                default:
                    // Handle other cases or set a default color
                    break;
            }
        }
    }
}

