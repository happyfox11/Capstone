package com.android.aifoodapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.vo.ReportDaySubItemVo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

// 자식 어답터
public class ReportDaySubItemAdapter extends RecyclerView.Adapter<ReportDaySubItemAdapter.SubItemViewHolder> {

    private List<ReportDaySubItemVo> subItemList;

    ReportDaySubItemAdapter(List<ReportDaySubItemVo> subItemList) {
        this.subItemList = subItemList;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_report_day_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder subItemViewHolder, int i) {
        ReportDaySubItemVo subItem = subItemList.get(i);
        subItemViewHolder.tvSubItemTitle.setText(subItem.getSubItemTitle());
        subItemViewHolder.tv_sub_item_kcal.setText(subItem.getSubItemDesc() + " kcal");

        //음식의 사진을 출력
        if(subItem.getSubItemImage().equals("")){
            //기본 설정된 이미지
        }
        else{
            String temp="";
            try{
                temp= URLDecoder.decode(subItem.getSubItemImage(),"UTF-8");
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }

            byte[] decodeByte = Base64.decode(temp, Base64.DEFAULT);
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
            subItemViewHolder.img_sub_item.setImageBitmap(compressedBitmap);

        }
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubItemTitle;
        TextView tv_sub_item_kcal;
        ImageView img_sub_item;

        SubItemViewHolder(View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.tv_sub_item_title);
            img_sub_item=itemView.findViewById(R.id.img_sub_item);
            tv_sub_item_kcal=itemView.findViewById(R.id.tv_sub_item_kcal);
        }
    }
}
