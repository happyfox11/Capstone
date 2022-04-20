package com.android.aifoodapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;

import java.util.ArrayList;

public class itemSearchAdapter extends RecyclerView.Adapter<Holder> {
    ArrayList<String> list;

    public itemSearchAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.food_list_item3, parent, false);
        return new Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class Holder extends RecyclerView.ViewHolder {
    TextView tv;
    public Holder(@NonNull View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.tv_search);
    }
}


