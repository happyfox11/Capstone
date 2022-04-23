package com.android.aifoodapp.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.activity.FoodDetailInfoActivity;

import java.util.ArrayList;


public class FoodInfoAdapter extends RecyclerView.Adapter<FoodInfoAdapter.CustomViewHolder> {

    private ArrayList<FoodInfo> items = new ArrayList<FoodInfo>();
    double num = 0;

    public FoodInfoAdapter() {
    }
    public FoodInfoAdapter(ArrayList<FoodInfo> items) {
        this.items = items;
    }
    @NonNull
    @Override //뷰홀더 객체 생성
    public FoodInfoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item2, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }


    @Override //어떤 객체를 바인딩할지 설정
    public void onBindViewHolder(@NonNull FoodInfoAdapter.CustomViewHolder holder, int position) {

        FoodInfo item = items.get(position);
        holder.setItem(item);


        //화살표 버튼 클릭 시 상세정보 액티비티로 이동
        holder.iv_arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String foodName = holder.cl_foodName.getText().toString();
                Intent intent = new Intent (view.getContext(), FoodDetailInfoActivity.class);

                intent.putExtra("foodName",foodName);
                view.getContext().startActivity(intent);
            }
        });

        holder.iv_scrollUpBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                num +=0.5;
                holder.cl_intake.setText("" + num);
            }
        });


        holder.iv_scrollDownBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(num >0){
                    num -=0.5;
                    holder.cl_intake.setText("" + num);
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    //아이템 추가
    public void addItem(FoodInfo item) {
        items.add(item);
    }

    public void setItems(ArrayList<FoodInfo> items) {
        this.items = items;
    }

    public FoodInfo getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, FoodInfo item) {
        items.get(position);
    }

    //아이템 삭제
    public void remove(int position) {
        try {
            items.remove(position);
            notifyItemRemoved(position);
            //notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }


    public void removeById(int id) {
        int index = -1;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                index = i;
                break;
            }
        }

        if (index < 0) return;

        try {
            items.remove(index);
            notifyItemRemoved(index);
            //notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView cl_foodInfo;
        protected TextView cl_foodName;
        protected TextView cl_caloriesInfo;
        protected TextView cl_intake;
        protected TextView food_list_name;
        protected ImageView iv_scrollUpBtn, iv_scrollDownBtn,iv_arrowBtn;
        protected Button modifyBtn;

        public CustomViewHolder(@NonNull View view) {

            super(view);

            this.cl_foodInfo = (ImageView) view.findViewById(R.id.cl_foodInfo);
            this.cl_foodName = (TextView) view.findViewById(R.id.cl_foodName);
            this.cl_caloriesInfo = (TextView) view.findViewById(R.id.cl_caloriesInfo);
            this.cl_intake = (TextView) view.findViewById(R.id.cl_intake);
            this.food_list_name = (TextView) view.findViewById(R.id.food_list_name);
            this.iv_scrollUpBtn = (ImageView) view.findViewById(R.id.iv_scrollUpBtn);
            this.iv_arrowBtn = (ImageView) view.findViewById(R.id.iv_arrowBtn);
            this.iv_scrollDownBtn = (ImageView) view.findViewById(R.id.iv_scrollDownBtn);
            this.iv_arrowBtn = (ImageView) view.findViewById(R.id.iv_arrowBtn);
            this.modifyBtn = (Button) view.findViewById(R.id.modifyBtn);

        }

        public void setItem(FoodInfo item) {
            cl_foodInfo.setImageResource(item.getCl_foodInfo());
            cl_foodName.setText(item.getCl_foodName());
            cl_caloriesInfo.setText(item.getCl_caloriesInfo());
            cl_intake.setText((item.getCl_intake()));
            food_list_name.setText(item.getFood_list_name());

        }
    }
}
