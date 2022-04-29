package com.android.aifoodapp.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aifoodapp.R;
import com.android.aifoodapp.activity.FoodAnalysisActivity;
import com.android.aifoodapp.activity.MainActivity;
import com.android.aifoodapp.interfaceh.OnCameraClick;
import com.android.aifoodapp.interfaceh.OnEditMealHeight;
import com.android.aifoodapp.interfaceh.OnGalleryClick;
import com.android.aifoodapp.vo.MealMemberVo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MealAdapter extends BaseAdapter {

    private Activity activity;
    private List<MealMemberVo> memberList;
    private MealHolder holder;

    private OnEditMealHeight onEditMealHeight;
    private OnCameraClick onCameraClick;
    private OnGalleryClick onGalleryClick;
    private MealAdapter.ItemClickListener itemClickListener;

    public interface ItemClickListener {
        public void onDetailButtonClicked(int position);
    }

    public class MealHolder {
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
            holder.btn_from_camera = convertView.findViewById(R.id.btn_from_camera);
            holder.btn_from_gallery = convertView.findViewById(R.id.btn_from_gallery);
            holder.iv_img = convertView.findViewById(R.id.iv_img);

            holder.layout_use_img_qa = convertView.findViewById(R.id.layout_use_img_qa);
            holder.layout_use_img = convertView.findViewById(R.id.layout_use_img);
            holder.btn_use_img_qa = convertView.findViewById(R.id.btn_use_img_qa);
            holder.btn_use_img_exit = convertView.findViewById(R.id.btn_use_img_exit);

            convertView.setTag(holder);
            Log.i("check", "holdergetView:"+holder);

        }
        else {
            holder = (MealHolder) convertView.getTag();
            Log.i("check", "holder:"+holder);
        }

        holder.btn_meal_detail.setTag("detail"+(getItemId(position)+1));

        holder.tv_custom_item_name.setText(" 식사 "+(getItemId(position)+1));
        memberList.get(position).setName(holder.tv_custom_item_name.getText().toString());

        //상세보기 버튼
        holder.btn_meal_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("check", "detail:"+(getItemId(position)+1));
                Log.i("check", "holderdetail:"+holder);

                if (itemClickListener != null) {
                    itemClickListener.onDetailButtonClicked(position);
                }

                //Intent intent = new Intent(activity, FoodAnalysisActivity.class);
                //activity.startActivity(intent);

                Toast.makeText(activity, "click"+holder.btn_meal_detail.getTag(), Toast.LENGTH_SHORT).show(); }
        });

        View finalConvertView = convertView;

        holder.btn_meal_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealHolder cholder = (MealHolder) finalConvertView.getTag();
                Log.i("check", "holderdelete:"+cholder);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setIcon(R.drawable.alert_icon);
                alertDialog.setTitle("삭제 확인 알림");
                alertDialog.setMessage("식사 "+(getItemId(position)+1)+"을(를) 정말로 삭제하시겠습니까??");

                alertDialog.setPositiveButton("예",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //알림창의 확인 버튼 클릭
                        Log.i("checkcheck","position:"+position);
                        Log.i("checkcheck","before:"+memberList);
                        cholder.iv_img.setImageBitmap(null);

                        memberList.remove(position);
                        notifyDataSetChanged();

                        onEditMealHeight.onEditMealHeight();

                        Log.i("checkcheck","after:"+memberList);
                        Log.i("checkcheck","count:"+getCount());
                        //MainActivity에서 처리
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

        holder.btn_use_img_qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealHolder cholder = (MealHolder) finalConvertView.getTag();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setIcon(R.drawable.alert_icon);
                alertDialog.setTitle("이미지 사용 여부 확인");
                alertDialog.setMessage("이미지를 이용하여 식사를 기록하시겠습니까?");

                alertDialog.setPositiveButton("이미지 사용",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //알림창의 이미지 사용 버튼 클릭
                        cholder.layout_use_img_qa.setVisibility(View.GONE);
                        cholder.layout_use_img.setVisibility(View.VISIBLE);
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

        holder.btn_use_img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealHolder cholder = (MealHolder) finalConvertView.getTag();
                cholder.layout_use_img_qa.setVisibility(View.VISIBLE);
                cholder.layout_use_img.setVisibility(View.GONE);
            }
        });

        holder.btn_from_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCameraClick.onCameraClick();
                Log.i("check", "camerabtn:"+(getItemId(position)+1));
                Log.i("check", "holdercamera:"+holder);

                MealHolder cholder = (MealHolder) finalConvertView.getTag();
                MainActivity mainActivity = (MainActivity) activity;
                //View finalConvertView = convertView;
                mainActivity.setOnSetImageListener(new MainActivity.OnSetImage() {
                    @Override
                    public void onSetImage(Uri photoUri) {

                        //holder.iv_img = (ImageView) finalConvertView.findViewWithTag("fh");
                        Log.i("check", "img:"+(getItemId(position)+1));
                        Log.i("check", "holderimg:"+holder);
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

                        cholder.iv_img.setImageBitmap(bitmap);
                        memberList.get(position).setMealImg(bitmap);
                        //Log.i("checkcheck","img:"+position);
                        //holder.iv_img.setImageURI(photoUri);
                        //holder.iv_img.setRotation(90f);
                        //holder.iv_img.setScaleType(ImageView.ScaleType.FIT_XY);

                    }
                });
            }
        });

        holder.btn_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGalleryClick.onGalleryClick();
                Log.i("check", "gallerybtn:"+(getItemId(position)+1));
                Log.i("check", "holdergallery:"+holder);

                MealHolder cholder = (MealHolder) finalConvertView.getTag();
                MainActivity mainActivity = (MainActivity) activity;
                //View finalConvertView = convertView;

                mainActivity.setOnSetImageListener(new MainActivity.OnSetImage() {
                    @Override
                    public void onSetImage(Uri photoUri) {

                        //holder.iv_img = (ImageView) finalConvertView.findViewWithTag("fh");
                        Log.i("check", "img:"+(getItemId(position)+1));
                        Log.i("check", "holderimg:"+holder);
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

                        cholder.iv_img.setImageBitmap(bitmap);
                        memberList.get(position).setMealImg(bitmap);
                        //holder.iv_img.setImageURI(photoUri);
                        //holder.iv_img.setRotation(90f);
                        //holder.iv_img.setScaleType(ImageView.ScaleType.FIT_XY);

                    }
                });
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