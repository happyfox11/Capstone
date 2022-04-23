package com.android.aifoodapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.RecyclerView.FoodItem;
import com.android.aifoodapp.RecyclerView.FoodItemAdapter;
import com.android.aifoodapp.activity.FoodAnalysisActivity;
import com.android.aifoodapp.activity.FoodInputActivity;
import com.android.aifoodapp.domain.fooddata;

import java.util.ArrayList;

public class itemSearchAdapter extends RecyclerView.Adapter<Holder> {
    ArrayList<fooddata> arrayList=new ArrayList<>();
    private Context context;
    private itemSearchAdapter.ItemClickListener itemClickListener;
    private fooddata food;

    public itemSearchAdapter(ArrayList<fooddata> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context; //FoodInputActivity.class
    }

    public interface ItemClickListener {
        public void addFoodList(fooddata food);
    }

    public void setItemClickListener(itemSearchAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
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
        holder.tv_search.setText(arrayList.get(position).getName()); //음식 이름으로 화면에 출력

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                food = arrayList.get(holder.getAdapterPosition());
                //Log.e("selectFood : ",arrayList.get(holder.getAdapterPosition()).getName());

                if (itemClickListener != null) {
                    itemClickListener.addFoodList(food);
                }
                /*Intent intent = new Intent(context, FoodAnalysisActivity.class);
                intent.putExtra("addFoodData",food); //선택한 음식데이터 넘기기
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

class Holder extends RecyclerView.ViewHolder {
    TextView tv_search;
    public Holder(@NonNull View itemView) {
        super(itemView);
        tv_search = itemView.findViewById(R.id.tv_search);
    }
}


