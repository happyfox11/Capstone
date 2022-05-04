package com.android.aifoodapp.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.activity.MainActivity;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.interfaceh.OnCameraClick;
import com.android.aifoodapp.interfaceh.OnEditMealHeight;
import com.android.aifoodapp.interfaceh.OnGalleryClick;
import com.android.aifoodapp.vo.MealMemberVo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        private ImageView iv_img;

        private LinearLayout layout_use_img_qa;
        private LinearLayout layout_use_img;
        private Button btn_use_img_qa;
        private Button btn_use_img_exit;
        private RecyclerView rvSubItem;

        public MealHolder(@NonNull View itemView){
            super(itemView);

            tv_custom_item_name = itemView.findViewById(R.id.tv_custom_item_name);
            btn_meal_detail = itemView.findViewById(R.id.btn_meal_detail);
            btn_meal_delete = itemView.findViewById(R.id.btn_meal_delete);
            btn_from_camera = itemView.findViewById(R.id.btn_from_camera);
            btn_from_gallery = itemView.findViewById(R.id.btn_from_gallery);
            iv_img = itemView.findViewById(R.id.iv_img);
            layout_use_img_qa = itemView.findViewById(R.id.layout_use_img_qa);
            layout_use_img = itemView.findViewById(R.id.layout_use_img);
            btn_use_img_qa = itemView.findViewById(R.id.btn_use_img_qa);
            btn_use_img_exit = itemView.findViewById(R.id.btn_use_img_exit);
            rvSubItem = (RecyclerView) itemView.findViewById(R.id.recycleFood);
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

        //1. 상세보기 버튼
        holder.btn_meal_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();

                if (itemClickListener != null) {
                    itemClickListener.onDetailButtonClicked(position);
                }

                //Intent intent = new Intent(activity, FoodAnalysisActivity.class);
                //activity.startActivity(intent);

                Toast.makeText(activity, memberList.get(position).getName()+"의 상세화면으로 이동합니다!", Toast.LENGTH_SHORT).show(); }
        });

        //2. 삭제하기 버튼
        holder.btn_meal_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setIcon(R.drawable.alert_icon);
                alertDialog.setTitle("삭제 확인 알림");
                alertDialog.setMessage(memberList.get(position).getName()+"을(를) 정말로 삭제하시겠습니까??");

                alertDialog.setPositiveButton("예",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //알림창의 확인 버튼 클릭
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
                alertDialog.setNegativeButton("아니오",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //알림창의 아니오 버튼 클릭 --> 그대로

                    }
                });
                alertDialog.show();
            }
        });

        //3. 이미지 사용 여부 선택 다이얼로그
        holder.btn_use_img_qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setIcon(R.drawable.alert_icon);
                alertDialog.setTitle("이미지 사용 여부 확인");
                alertDialog.setMessage("이미지를 이용하여 식사를 기록하시겠습니까?");

                alertDialog.setPositiveButton("이미지 사용",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //알림창의 이미지 사용 버튼 클릭
                        holder.layout_use_img_qa.setVisibility(View.GONE);
                        holder.layout_use_img.setVisibility(View.VISIBLE);
                    }
                });
                alertDialog.setNegativeButton("수기 입력",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //알림창의 수기 입력 버튼 클릭
                        if (itemClickListener != null) {
                            itemClickListener.onDetailButtonClicked(position);
                        }
                    }
                });
                alertDialog.show();

            }
        });

        //4. 이미지 사용 하지 않기 버튼
        holder.btn_use_img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                holder.layout_use_img_qa.setVisibility(View.VISIBLE);
                holder.layout_use_img.setVisibility(View.GONE);
            }
        });

        //5. 카메라를 통해 이미지 촬영하기 버튼
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
                        byte[] byteArray = stream.toByteArray();
                        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

                        holder.iv_img.setImageBitmap(compressedBitmap);
                        memberList.get(position).setMealImg(compressedBitmap);


                    }
                });
            }
        });

        //6. 갤러리를 통해서 이미지 가져오기 버튼
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


                    }
                });
            }
        });

        // 자식 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rvSubItem.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItemList().size());

        //Log.e("size",Integer.toString(item.getSubItemList().size()));
        // 자식 어답터 설정
        FoodListAdapter subItemAdapter = new FoodListAdapter(item.getSubItemList());

        holder.rvSubItem.setLayoutManager(layoutManager);
        holder.rvSubItem.setAdapter(subItemAdapter);
        holder.rvSubItem.setRecycledViewPool(viewPool);
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
