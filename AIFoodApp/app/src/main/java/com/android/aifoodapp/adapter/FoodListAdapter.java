package com.android.aifoodapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.vo.SubItem;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListHolder> {

    private List<SubItem> subItemList;
    private Context context;

    public FoodListAdapter(List<SubItem> subItemList) {
        this.subItemList = subItemList;
    }

    @NonNull
    @Override
    public FoodListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.food_list_item4, parent, false);
        return new FoodListHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FoodListHolder holder, int position) {
        //SubItem subItem = subItemList.get(position); //왜 하나씩 저장되는건가 ㅠ
        String str="";
/*        for(int i=0;i<getItemCount();i++){
            SubItem subItem=subItemList.get(i);
            str+=subItem.getSubItemTitle()+"\n";
        }*/
        
        // 음식의 번호를 출력
        holder.tv_food_order.setText(Integer.toString(position+1));
        Log.i("check", String.valueOf(getItemCount()));
        
        //음식의 이름을 출력
        str = subItemList.get(position).getSubItemTitle();
        holder.tv_search2.setText(str);

        //음식의 사진을 출력
        if(subItemList.get(position).getSubItemImg().equals("")){
            //기본 설정된 이미지
        }
        else{
            //String을 Bitmap으로
            String temp="";
            try{
                temp= URLDecoder.decode(subItemList.get(position).getSubItemImg(),"UTF-8");
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }

            byte[] decodeByte = Base64.decode(temp, Base64.DEFAULT);
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
            holder.iv_show_food_img.setImageBitmap(compressedBitmap);

        }
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }
}

class FoodListHolder extends RecyclerView.ViewHolder {
    TextView tv_search2;
    TextView tv_food_order;
    ImageView iv_show_food_img;

    public FoodListHolder(@NonNull View itemView) {
        super(itemView);
        tv_search2 = itemView.findViewById(R.id.tv_search2);
        tv_food_order = itemView.findViewById(R.id.tv_food_order);
        iv_show_food_img=itemView.findViewById(R.id.iv_show_food_img);
    }
}
