package com.android.aifoodapp.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aifoodapp.R;
import com.android.aifoodapp.activity.FoodAnalysisActivity;
import com.android.aifoodapp.interfaceh.OnEditMealHeight;
import com.android.aifoodapp.vo.MealMemberVo;

import java.util.List;

public class MealAdapter extends BaseAdapter {

    private Activity activity;
    private List<MealMemberVo> memberList;
    private MealHolder holder;

    private OnEditMealHeight onEditMealHeight;


    public class MealHolder {
        private TextView tv_custom_item_name;
        private Button btn_meal_detail;
        private Button btn_meal_delete;
//        private Button btn_from_camera;
//        private Button btn_from_gallery;
//        private ImageView iv_img;
    }

    public MealAdapter(Activity activity, List<MealMemberVo> memberList) {
        this.activity = activity;
        this.memberList = memberList;

    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("check", "getView:"+(getItemId(position)+1));
        if(convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_meal_list, parent, false);

            holder = new MealHolder();

            holder.tv_custom_item_name = convertView.findViewById(R.id.tv_custom_item_name);
            holder.btn_meal_detail = convertView.findViewById(R.id.btn_meal_detail);
            holder.btn_meal_delete = convertView.findViewById(R.id.btn_meal_delete);
//            holder.btn_from_camera = convertView.findViewById(R.id.btn_from_camera);
//            holder.btn_from_gallery = convertView.findViewById(R.id.btn_from_gallery);
//            holder.iv_img = convertView.findViewById(R.id.iv_img);

            convertView.setTag(holder);
            Log.i("check", "holdergetView:"+holder);

        }
        else {
            holder = (MealHolder) convertView.getTag();
            Log.i("check", "holder:"+holder);
        }

        holder.btn_meal_detail.setTag("detail"+(getItemId(position)+1));

        holder.tv_custom_item_name.setText(" 식사 "+(getItemId(position)+1));


        holder.btn_meal_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("check", "detail:"+(getItemId(position)+1));
                Log.i("check", "holderdetail:"+holder);
                Intent intent = new Intent(activity, FoodAnalysisActivity.class);
                activity.startActivity(intent);

                Toast.makeText(activity, "click"+holder.btn_meal_detail.getTag(), Toast.LENGTH_SHORT).show(); }
        });

        holder.btn_meal_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setIcon(R.drawable.alert_icon);
                alertDialog.setTitle("삭제 확인 알림");
                alertDialog.setMessage("식사 "+(getItemId(position)+1)+"을(를) 정말로 삭제하시겠습니까??");

                alertDialog.setPositiveButton("예",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //알림창의 확인 버튼 클릭
                        memberList.remove(position);
                        notifyDataSetChanged();

                        //MainActivity에서 처리
                        onEditMealHeight.onEditMealHeight();
                    }
                });
                alertDialog.setNegativeButton("아니오",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //알림창의 아니오 버튼 클릭

                    }
                });
                alertDialog.show();

            }
        });


        return convertView;
    }

    public void addItem(MealMemberVo mealMemberVo)
    {
        memberList.add(mealMemberVo);
        notifyDataSetChanged();
    }

    public void setOnEditMealHeightListener(OnEditMealHeight onEditMealHeightListener) {
        this.onEditMealHeight = onEditMealHeightListener;
    }


}