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
import com.android.aifoodapp.domain.fooddata;

import de.hdodenhof.circleimageview.CircleImageView;


import java.util.ArrayList;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.CustomViewHolder> {

    private final ArrayList<FoodItem> arrayList;
    private ItemClickListener itemClickListener;

    public FoodItemAdapter(ArrayList<FoodItem> arrayList) {
        this.arrayList = arrayList;
    }

    public interface ItemClickListener {
        public void onItemClicked(FoodItem item);
        public void onRemoveButtonClicked(int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //Holder
    @NonNull
    @Override
    public FoodItemAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false);

        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override //어떤 객체를 바인딩할지 설정
    public void onBindViewHolder(@NonNull FoodItemAdapter.CustomViewHolder holder, int position) {

        holder.fl_foodInfo.setImageResource(Integer.parseInt(arrayList.get(position).getFl_image()));
        holder.fl_foodInfo.setBorderColor(Color.WHITE);
        holder.iv_minusBtn.setImageResource(arrayList.get(position).getMinusBtn());
        holder.fl_foodName.setText(arrayList.get(position).getFl_foodName());

        holder.fl_foodInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition(); //holder.getBindingAdapterPosition()

                if (itemClickListener != null) {
                    itemClickListener.onItemClicked(arrayList.get(position));
                }

                holder.fl_foodInfo.setBorderColor(Color.RED); //바깥 외부선 빨간색
                // notifyDataSetChanged();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (i != position) {
                        notifyItemChanged(i, null);
                    }
                }
            }
        });

        holder.iv_minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                //holder.getBindingAdapterPosition()

                if (itemClickListener != null) {
                    itemClickListener.onRemoveButtonClicked(position);
                }
                remove(position);
            }
        });

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

            this.fl_foodInfo = (CircleImageView) itemView.findViewById(R.id.fl_foodInfo); //이미지
            this.iv_minusBtn = (ImageView) itemView.findViewById(R.id.iv_minusBtn);
            this.fl_foodName = (TextView) itemView.findViewById(R.id.fl_foodName);
        }
    }
}





