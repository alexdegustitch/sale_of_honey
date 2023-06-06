package com.example.medenjaci.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medenjaci.R;

import java.util.ArrayList;


public class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderAdapter.ItemOrderViewHolder> {

    Context context;
    ArrayList<ItemInOrder> itemsInOrder;

    public ItemOrderAdapter(Context context, ArrayList<ItemInOrder> itemsInOrder) {
        this.context = context;
        this.itemsInOrder = itemsInOrder;
    }

    @NonNull
    @Override
    public ItemOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_in_order_row, parent, false);
        return new ItemOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemOrderViewHolder holder, int position) {
        holder.name.setText(itemsInOrder.get(position).getName());
        holder.price.setText(itemsInOrder.get(position).getPrice());
        holder.icon.setImageResource(itemsInOrder.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return itemsInOrder.size();
    }

    public class ItemOrderViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name, price;

        public ItemOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.itemOrderImage);
            name = itemView.findViewById(R.id.itemOrderName);
            price = itemView.findViewById(R.id.itemOrderPrice);
        }
    }
}
