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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        try {
            holder.bind(item);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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

        public void bind(Request item) throws ParseException {

            txtTitle.setText(item.getType());
            String createdAt = item.getCreatedAt();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Parse string to Date
            Date date = dateFormat.parse(createdAt);

            // Create a Calendar instance and set it to the parsed date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Add 6 months
            calendar.add(Calendar.MONTH, 6);

            // Get the date after adding 6 months
            Date dateAfter6Months = calendar.getTime();

            // Define output date format
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM dd, yyyy");

            // Format the date to "April 21, 2024"
            String formattedDate = "";
            if (!item.getStatus().equals("rejected")) {
                formattedDate = "\nNote: You can claim your request within 1-2 days.";
            }

            if (item.getType().equals("Barangay ID")) {
                formattedDate = "\nNote: Next request of this service will be available at " + outputDateFormat.format(dateAfter6Months) + " and you can claim your request within 1-2 days.";
            }

            if(item.getStatus().equals("rejected")){
                formattedDate = "";
            }
            txtSubtitle.setText(item.getDescription() + formattedDate);

            String status = item.getStatus();
            String resultString = status.replace("-", " ");
            statusBtn.setText(resultString);
            statusBtn.setEnabled(false);
            statusBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.white));
            switch (item.getStatus()) {
                case "on-going":
                    statusBtn.setTextColor(ContextCompat.getColorStateList(context, R.color.md_yellow_800));
                    break;
                case "ready-to-claim":
                    statusBtn.setTextColor(ContextCompat.getColorStateList(context, R.color.md_green_800));
                    break;
                case "claimed":
                    statusBtn.setTextColor(ContextCompat.getColorStateList(context, R.color.md_blue_800));
                    break;
                default:
                    // Handle other cases or set a default color
                    statusBtn.setTextColor(ContextCompat.getColorStateList(context, R.color.black));
                    break;
            }
        }
    }
}

