package com.example.medenjaci.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medenjaci.ItemDetailsActivity;
import com.example.medenjaci.R;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder> {

    String names[];
    String prices[];
    int images[];
    Context context;
    int ids[];

    public IndexAdapter(Context ct, String names[], String prices[], int images[], int ids[]) {

        context = ct;
        this.names = names;
        this.prices = prices;
        this.images = images;
        this.ids = ids;

    }

    @NonNull
    @Override
    public IndexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.index_row, parent, false);
        return new IndexViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IndexViewHolder holder, int position) {
        holder.nameText.setText(names[position]);
        holder.priceText.setText(prices[position]);
        holder.image.setImageResource(images[position]);

        holder.indexRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailsActivity.class);
                intent.putExtra("item_id", ids[position]);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class IndexViewHolder extends RecyclerView.ViewHolder {

        TextView nameText, priceText;
        ImageView image;
        ConstraintLayout indexRow;

        public IndexViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.itemOrderName);
            priceText = itemView.findViewById(R.id.itemCartPrice2);
            image = itemView.findViewById(R.id.itemOrderImage);
            indexRow = itemView.findViewById(R.id.indexRow);

        }
    }
}
