package com.example.ecommerceapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.http.HttpUtils;
import com.example.ecommerceapp.models.ItemData;

import java.io.File;
import java.util.ArrayList;

public class ItemsCardViewAdapter extends RecyclerView.Adapter<ItemsCardViewAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<ItemData> itemData;
    private final Context mContext;
    private final OnItemClickListener onItemClickListener;
    private final OnItemDeleteClickListener onItemDeleteClickListener;

    public ItemsCardViewAdapter(@NonNull Context context, ArrayList<ItemData> dataSetItem, OnItemClickListener onItemClickListener1, OnItemDeleteClickListener onItemDeleteClickListener1) {
        inflater = LayoutInflater.from(context);
        itemData = dataSetItem;
        mContext = context;
        onItemClickListener = onItemClickListener1;
        onItemDeleteClickListener = onItemDeleteClickListener1;
    }

    @NonNull
    @Override
    public ItemsCardViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsCardViewAdapter.MyViewHolder holder, int position) {
        final ItemData dataSetItem = itemData.get(holder.getAdapterPosition());

        final TextView textViewName = holder.textViewItem;
        final TextView textViewPrice = holder.textViewPrice;
        final ImageView imageItem = holder.imageItem;
        final ImageView imageViewDelete = holder.imageViewDelete;


        textViewName.setText(dataSetItem.getName());
        textViewPrice.setText(dataSetItem.getPrice());
        Glide.with(mContext).load(dataSetItem.getImage()).into(imageItem);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.OnItemClickListener(dataSetItem, holder.getAdapterPosition());
            }
        });

        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemDeleteClickListener.OnItemClickListener(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem;
        TextView textViewPrice;
        ImageView imageItem;
        ImageView saleTag;
        ImageView imageViewDelete;
        TextView tagText;

        MyViewHolder(final View itemView) {
            super(itemView);
            this.textViewItem = itemView.findViewById(R.id.textViewItem);
            this.textViewPrice = itemView.findViewById(R.id.textViewPrice);
            this.imageItem = itemView.findViewById(R.id.imageItem);
            this.saleTag = itemView.findViewById(R.id.tag);
            this.tagText = itemView.findViewById(R.id.tagText);
            this.imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }

    public interface OnItemClickListener {
        void OnItemClickListener(ItemData itemData, int position);
    }

    public interface OnItemDeleteClickListener {
        void OnItemClickListener(int position);
    }
}
