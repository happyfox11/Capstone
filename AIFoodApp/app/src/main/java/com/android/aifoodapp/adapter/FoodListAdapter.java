package com.android.aifoodapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.meal;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<Holder2> {

    ArrayList<meal> arrayList=new ArrayList<>();
    private Context context;

    public FoodListAdapter(ArrayList<meal> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.food_list_item4, parent, false);
        return new Holder2(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder2 holder, int position) {
        for(meal repo: arrayList){
            if(repo.getTimeflag()==position){
                holder.tv_search2.setText(repo.getMealname()); //음식 이름으로 화면에 출력
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

class Holder2 extends RecyclerView.ViewHolder {
    TextView tv_search2;
    public Holder2(@NonNull View itemView) {
        super(itemView);
        tv_search2 = itemView.findViewById(R.id.tv_search2);
    }
}
