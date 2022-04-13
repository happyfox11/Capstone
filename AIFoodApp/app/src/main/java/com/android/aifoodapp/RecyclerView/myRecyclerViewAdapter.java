package com.android.aifoodapp.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;

import de.hdodenhof.circleimageview.CircleImageView;



import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

    public class myRecyclerViewAdapter extends RecyclerView.Adapter<myRecyclerViewAdapter.CustomViewHolder> {

        private ArrayList<FoodItem> arrayList;

        public myRecyclerViewAdapter(ArrayList<FoodItem> arrayList) {
            this.arrayList = arrayList;
        }


        @NonNull
        @Override //뷰홀더 객체 생성
        public myRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false);
            CustomViewHolder holder = new CustomViewHolder(view);

            return holder;
        }

        @Override //어떤 객체를 바인딩할지 설정
        public void onBindViewHolder(@NonNull myRecyclerViewAdapter.CustomViewHolder holder, int position) {

            holder.fl_foodInfo.setImageResource(arrayList.get(position).getFl_foodInfo());
            holder.fl_foodInfo.setBorderColor(Color.WHITE);

            holder.fl_foodInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.fl_foodInfo.setBorderColor(Color.RED);
                    // notifyDataSetChanged();
                    for (int i=0; i< arrayList.size();i++){

                        if( i != holder.getAdapterPosition()){
                            notifyItemChanged(i,null);
                        }
                    }


                }

            });

            holder.iv_minusBtn.setImageResource(arrayList.get(position).getMinusBtn());

            holder.iv_minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove(holder.getAdapterPosition());
                }
            });

            holder.fl_foodName.setText(arrayList.get(position).getFl_foodName());

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


        //아이템 추가
        public void addItem(FoodItem food) {
            arrayList.add(food);
        }


        //아이템 삭제
        public void remove(int position) {
            try {
                arrayList.remove(position);
                notifyItemRemoved(position);
                //notifyDataSetChanged();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }



        public class CustomViewHolder extends RecyclerView.ViewHolder {

            protected CircleImageView fl_foodInfo;
            protected ImageView iv_minusBtn;
            protected TextView fl_foodName;


            public CustomViewHolder(@NonNull View itemView) {

                super(itemView);

                this.fl_foodInfo = (CircleImageView) itemView.findViewById(R.id.fl_foodInfo);
                this.iv_minusBtn = (ImageView) itemView.findViewById(R.id.iv_minusBtn);
                this.fl_foodName = (TextView) itemView.findViewById(R.id.fl_foodName);


            }



        }
    }





