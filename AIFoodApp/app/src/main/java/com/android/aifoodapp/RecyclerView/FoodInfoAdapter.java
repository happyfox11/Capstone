package com.android.aifoodapp.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.activity.FoodAnalysisActivity;
import com.android.aifoodapp.activity.FoodDetailInfoActivity;
import com.android.aifoodapp.domain.fooddata;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class FoodInfoAdapter extends RecyclerView.Adapter<FoodInfoAdapter.CustomViewHolder> {

    private ArrayList<FoodInfo> items = new ArrayList<FoodInfo>();
    private FoodInfoAdapter.ItemClickListener itemClickListener;


    public FoodInfoAdapter() {
    }
    public FoodInfoAdapter(ArrayList<FoodInfo> items) {
        this.items = items;
    }

    public interface ItemClickListener {
        public void onIntakeChangeClicked(double intake,int position);
        public void modifyFoodClicked(int position);
    }

    public void setItemClickListener(FoodInfoAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override //뷰홀더 객체 생성 Holder
    public FoodInfoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item2, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }


    @Override //어떤 객체를 바인딩할지 설정
    public void onBindViewHolder(@NonNull FoodInfoAdapter.CustomViewHolder holder, int position) {

        /* setting */
        holder.cl_foodInfo.setImageBitmap(items.get(position).getCl_img());
        holder.cl_foodName.setText(items.get(position).getFood().getName());
        holder.cl_caloriesInfo.setText(String.valueOf((int) items.get(position).getFood().getCalorie()));
        holder.cl_intake.setText(String.valueOf(items.get(position).getCl_intake()));
        holder.food_list_name.setText(items.get(position).getFood().getName());

        //화살표 버튼 클릭 시 상세정보 액티비티로 이동
        holder.iv_arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                String foodImg="";
                fooddata food = items.get(holder.getAdapterPosition()).getFood();
                Bitmap bitmap=items.get(holder.getAdapterPosition()).getCl_img();

                Drawable drawable = new BitmapDrawable(bitmap); //bitmap을 drawable로
                Bitmap convertBitmap = ((BitmapDrawable)drawable).getBitmap();
                Bitmap convertBitmap2 = ((FoodAnalysisActivity) FoodAnalysisActivity.mContext).tmpBitmap;
//                Drawable drawable2 = view.getContext().getResources().getDrawable(R.drawable.icon);
//                Bitmap convertBitmap2 = ((BitmapDrawable)drawable2).getBitmap();
                Intent intent = new Intent (view.getContext(), FoodDetailInfoActivity.class);

                //Log.e("ㅎㅎ2",convertBitmap.toString());
                //Log.e("ㅎㅎ3",convertBitmap2.toString());

                if(convertBitmap.equals(convertBitmap2)){
                    foodImg="";//기본이미지
                }
                else{
                    foodImg=((FoodAnalysisActivity) FoodAnalysisActivity.mContext).photoList.get(position);
                    /*
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
                    byte[] bytes = stream.toByteArray();

                    foodImg = Base64.encodeToString(bytes, Base64.DEFAULT);
                    */
                    //Log.e("foodImg", foodImg);
                }

                intent.putExtra("foodImg",foodImg);
                intent.putExtra("foodDetail",food);//선택한 food 상세페이지
                view.getContext().startActivity(intent);
            }
        });

        holder.iv_scrollUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                double intake=items.get(position).getCl_intake();
                intake +=0.5;
                items.set(position,new FoodInfo(items.get(position).getFood(),items.get(position).getCl_img(),intake));
                notifyDataSetChanged();
                if (itemClickListener != null) {
                    itemClickListener.onIntakeChangeClicked(intake,position);
                }
            }
        });

        holder.iv_scrollDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                double intake=items.get(position).getCl_intake();
                if(intake >0){
                    intake -=0.5;
                    items.set(position,new FoodInfo(items.get(position).getFood(),items.get(position).getCl_img(),intake));
                    notifyDataSetChanged();
                    if (itemClickListener != null) {
                        itemClickListener.onIntakeChangeClicked(intake,position);
                    }
                }
            }
        });

        //원하는 음식이 아닐 때 수정하기
        holder.modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                //fooddata food = items.get(holder.getAdapterPosition()).getFood();
                //Intent intent = new Intent (view.getContext(), FoodInputActivity.class);
                if (itemClickListener != null) {
                    itemClickListener.modifyFoodClicked(position);
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


    public void removeById(int position) {
        /*
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                index = i;
                break;
            }
        }
        if (index < 0) return;
        */

        try {
            items.remove(position);
            notifyItemRemoved(position);
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
    }
}
