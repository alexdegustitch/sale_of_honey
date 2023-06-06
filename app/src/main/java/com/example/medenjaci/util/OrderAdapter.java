package com.example.medenjaci.util;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medenjaci.R;
import com.example.medenjaci.beans.Order;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<OrderHelper> orders;


    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public interface OnItemClickListener {
        void onButtonClick(int position);
    }

    public OrderAdapter(Context context, ArrayList<OrderHelper> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_row, parent, false);
        return new OrderViewHolder(view, mListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        holder.orderTime.setText(orders.get(position).getTimeOrder());
        holder.orderPrice.setText(Double.toString(orders.get(position).getPrice()) + " RSD");
        holder.orderStatus.setText(orders.get(position).getStatus());
        holder.orderIcon.setImageResource(orders.get(position).getIcon());

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderTime, orderPrice, orderStatus;
        ImageView orderIcon;
        Button button;

        public OrderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            orderTime = itemView.findViewById(R.id.orderRowTime);
            orderPrice = itemView.findViewById(R.id.orderRowPrice);
            orderStatus = itemView.findViewById(R.id.orderRowStatus);
            orderIcon = itemView.findViewById(R.id.orderRowIcon);
            button = itemView.findViewById(R.id.orderButton);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onButtonClick(position);
                        }
                    }
                }
            });
        }
    }
}
