package com.android.aifoodapp.adapter;


import static com.android.aifoodapp.interfaceh.baseURL.url;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.activity.FoodInputActivity;
import com.android.aifoodapp.activity.MainActivity;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.interfaceh.OnCameraClick;
import com.android.aifoodapp.interfaceh.OnEditMealHeight;
import com.android.aifoodapp.interfaceh.OnGalleryClick;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.android.aifoodapp.vo.MealMemberVo;
import com.android.aifoodapp.vo.SubItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealHolder> {

    private Activity activity;
    private ArrayList<MealMemberVo> memberList;
    private OnEditMealHeight onEditMealHeight;
    private OnCameraClick onCameraClick;
    private OnGalleryClick onGalleryClick;
    private MealAdapter.ItemClickListener itemClickListener;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public interface ItemClickListener {
        public void onDetailButtonClicked(int position);
        public void removeButtonClicked(int position);
        public void mealSaveFromPhoto(byte[] byteArray, int position, Bitmap compressedBitmap, Uri photoUri, String tmp);
    }

    public MealAdapter(Activity activity, ArrayList<MealMemberVo> memberList) {
        this.activity = activity;
        this.memberList = memberList;

    }

    public class MealHolder extends RecyclerView.ViewHolder {

        private TextView tv_custom_item_name;
        private Button btn_meal_detail;
        private Button btn_meal_delete;
        private Button btn_from_camera;
        private Button btn_from_gallery;
        private Button btn_from_direct;
        private ImageView iv_img;

        private LinearLayout layout_use_img_qa;
        private LinearLayout layout_use_img;
        private Button btn_use_img_qa;
        private Button btn_use_img_exit;
        private RecyclerView rvSubItem;
        private TextView tv_food_not_exist;
        private Button btn_rv_scroll_end_item;
        private FrameLayout fl_food_list;

        public MealHolder(@NonNull View itemView){
            super(itemView);

            tv_custom_item_name = itemView.findViewById(R.id.tv_custom_item_name);
            btn_meal_detail = itemView.findViewById(R.id.btn_meal_detail);
            btn_meal_delete = itemView.findViewById(R.id.btn_meal_delete);
            btn_from_camera = itemView.findViewById(R.id.btn_from_camera);
            btn_from_gallery = itemView.findViewById(R.id.btn_from_gallery);
            btn_from_direct = itemView.findViewById(R.id.btn_from_direct);
            iv_img = itemView.findViewById(R.id.iv_img);
            layout_use_img_qa = itemView.findViewById(R.id.layout_use_img_qa);
            layout_use_img = itemView.findViewById(R.id.layout_use_img);
            btn_use_img_qa = itemView.findViewById(R.id.btn_use_img_qa);
            btn_use_img_exit = itemView.findViewById(R.id.btn_use_img_exit);
            rvSubItem = (RecyclerView) itemView.findViewById(R.id.recycleFood);
            tv_food_not_exist = itemView.findViewById(R.id.tv_food_not_exist);
            btn_rv_scroll_end_item = itemView.findViewById(R.id.btn_rv_scroll_end_item);
            fl_food_list = itemView.findViewById(R.id.fl_food_list);
        }
    }

    @NonNull
    @Override
    public MealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_meal_list, parent, false);

        MealHolder MealHolder = new MealHolder(view);

        return MealHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealHolder holder, int position) {
        MealMemberVo item=memberList.get(position);
        holder.tv_custom_item_name.setText(memberList.get(position).getName());
        holder.iv_img.setImageBitmap(memberList.get(position).getMealImg());

        //1. ???????????? ??????
        holder.btn_meal_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();

                if (itemClickListener != null) {
                    itemClickListener.onDetailButtonClicked(position);
                }

                //Intent intent = new Intent(activity, FoodAnalysisActivity.class);
                //activity.startActivity(intent);

                Toast.makeText(activity, memberList.get(position).getName()+"??? ?????????????????? ???????????????!", Toast.LENGTH_SHORT).show(); }
        });

        //2. ???????????? ??????
        holder.btn_meal_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setIcon(R.drawable.alert_icon);
                alertDialog.setTitle("?????? ?????? ??????");
                alertDialog.setMessage(memberList.get(position).getName()+"???(???) ????????? ??????????????????????????");

                alertDialog.setPositiveButton("???",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //???????????? ?????? ?????? ??????
                        //holder.iv_img.setImageBitmap(null);

                        memberList.remove(position);
                        notifyItemRemoved(position);

                        if (itemClickListener != null) {
                            itemClickListener.removeButtonClicked(position);
                        }

                        holder.layout_use_img.setVisibility(View.GONE);
                        holder.layout_use_img_qa.setVisibility(View.VISIBLE);

                        onEditMealHeight.onEditMealHeight();
                    }
                });
                alertDialog.setNegativeButton("?????????",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //???????????? ????????? ?????? ?????? --> ?????????

                    }
                });
                alertDialog.show();
            }
        });

        //3. ????????? ?????? ?????? ?????? ???????????????
        holder.btn_use_img_qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setIcon(R.drawable.alert_icon);
                alertDialog.setTitle("????????? ?????? ?????? ??????");
                alertDialog.setMessage("???????????? ???????????? ????????? ?????????????????????????");

                alertDialog.setPositiveButton("????????? ??????",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //???????????? ????????? ?????? ?????? ??????
                        holder.layout_use_img_qa.setVisibility(View.GONE);
                        holder.layout_use_img.setVisibility(View.VISIBLE);
                    }
                });
                alertDialog.setNegativeButton("?????? ??????",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //???????????? ?????? ?????? ?????? ??????
                        if (itemClickListener != null) {
                            itemClickListener.onDetailButtonClicked(position);
                        }
                    }
                });
                alertDialog.show();

            }
        });

        //4. ????????? ?????? ?????? ?????? ??????
        holder.btn_use_img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                holder.layout_use_img_qa.setVisibility(View.VISIBLE);
                holder.layout_use_img.setVisibility(View.GONE);
            }
        });

        // ???????????? ?????? ??????
        //TODO: ???????????? ????????? ???????????? ??????????????? ?????????, ?????? ???????????????, ????????? FoodInputActivity??? ???????????? ?????? ??????????????? ?????? ??? ????????????.
        holder.btn_from_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                if (itemClickListener != null) {
                    itemClickListener.onDetailButtonClicked(position);
                }
            }
        });



        //5. ???????????? ?????? ????????? ???????????? ??????
        holder.btn_from_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                onCameraClick.onCameraClick();

                MainActivity mainActivity = (MainActivity) activity;

                mainActivity.setOnSetImageListener(new MainActivity.OnSetImage() {
                    @Override
                    public void onSetImage(Uri photoUri) {

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), photoUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,80, stream);
                        byte[] byteArray = stream.toByteArray(); //byte[] ==BLOB ??????

                        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

                        holder.iv_img.setImageBitmap(compressedBitmap);
                        memberList.get(position).setMealImg(compressedBitmap);

                        /* bitmap compressedBitmap ?????? ???????????????-> ai ?????? -> ?????? ?????? ????????? ?????????????????? ???????????? */
                        // ???????????? ?????? ???????????? fooddata??? ??????, foodAnalysisActivity??? ?????????
                        // byteArray??? String?????? ???????????? db??? ?????????.(?????? db?????? blob??? ??????)
                        if (itemClickListener != null) {
                            itemClickListener.mealSaveFromPhoto(byteArray, position, compressedBitmap, photoUri, "camera");
                        }

                    }
                });
            }
        });

        //6. ???????????? ????????? ????????? ???????????? ??????
        holder.btn_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                onGalleryClick.onGalleryClick();
                Log.i("check", "gallerybtn:"+(getItemId(position)+1));
                Log.i("check", "holdergallery:"+holder);

                MainActivity mainActivity = (MainActivity) activity;

                mainActivity.setOnSetImageListener(new MainActivity.OnSetImage() {
                    @Override
                    public void onSetImage(Uri photoUri) {

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), photoUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,80, stream);
                        byte[] byteArray = stream.toByteArray();
                        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

                        holder.iv_img.setImageBitmap(compressedBitmap);
                        memberList.get(position).setMealImg(compressedBitmap);

                        if (itemClickListener != null) {
                            itemClickListener.mealSaveFromPhoto(byteArray, position, compressedBitmap, photoUri, "gallery");
                        }

                    }
                });
            }
        });

        // ?????? ???????????? ????????? ??????
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rvSubItem.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItemList().size());

        //Log.e("size",Integer.toString(item.getSubItemList().size()));
/*        for(SubItem i  :item.getSubItemList()){
            Log.i("check",i.getSubItemTitle());
        }*/

        // ?????? ????????? ??????
        FoodListAdapter subItemAdapter = new FoodListAdapter(item.getSubItemList());

        holder.rvSubItem.setLayoutManager(layoutManager);
        holder.rvSubItem.setAdapter(subItemAdapter);
        holder.rvSubItem.setRecycledViewPool(viewPool);

        //???????????? ???????????? ??????
        if(layoutManager.getItemCount() > 0){
            holder.tv_food_not_exist.setVisibility(View.GONE);
            holder.fl_food_list.setVisibility(View.VISIBLE);
        }
        else{//???????????? ???????????? ?????? ??????, ???????????? ???????????? ???????????? ????????? ??????
            holder.tv_food_not_exist.setVisibility(View.VISIBLE);
            holder.fl_food_list.setVisibility(View.GONE);
        }

        // (>>) ?????? ?????????, ?????????????????? ????????? ??????????????? ??????????????? ??????
        holder.btn_rv_scroll_end_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();
                holder.rvSubItem.scrollToPosition(item.getSubItemList().size()-1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public void addItem(MealMemberVo MealMemberVo) {
        memberList.add(MealMemberVo);
        notifyDataSetChanged();
    }


    public void setOnEditMealHeightListener(OnEditMealHeight onEditMealHeightListener) {
        this.onEditMealHeight = onEditMealHeightListener;
    }

    public void setOnCameraClickListener(OnCameraClick onCameraClickListener) {
        this.onCameraClick = onCameraClickListener;
    }

    public void setOnGalleryClickListener(OnGalleryClick onGalleryClickListener) {
        this.onGalleryClick = onGalleryClickListener;
    }

    public void setItemClickListener(MealAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }




}
