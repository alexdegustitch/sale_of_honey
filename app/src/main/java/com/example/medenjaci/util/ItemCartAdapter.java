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

public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.ItemCartViewHolder> {

    Context context;
    ArrayList<CartItem> itemsInCart;

    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public ItemCartAdapter(Context context, ArrayList<CartItem> itemsInCart) {

        this.context = context;
        this.itemsInCart = itemsInCart;
    }


    @NonNull
    @Override
    public ItemCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_in_cart_row, parent, false);
        return new ItemCartViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCartViewHolder holder, int position) {
        holder.name.setText(itemsInCart.get(position).getName());
        holder.part1.setText(itemsInCart.get(position).getPart1());
        holder.part2.setText(itemsInCart.get(position).getPart2());
        holder.image.setImageResource(itemsInCart.get(position).getImageResource());
        //holder.button.setOnClickListener()
    }

    @Override
    public int getItemCount() {
        return itemsInCart.size();
    }

    public class ItemCartViewHolder extends RecyclerView.ViewHolder {

        TextView name, part1, part2;
        ImageView image, button;

        public ItemCartViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.itemOrderName);
            part1 = itemView.findViewById(R.id.itemOrderPrice);
            part2 = itemView.findViewById(R.id.itemCartPrice2);
            image = itemView.findViewById(R.id.itemOrderImage);
            button = itemView.findViewById(R.id.itemCartDelete);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
