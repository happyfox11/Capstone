package com.android.aifoodapp.item;

import com.google.gson.annotations.SerializedName;

public class MemberInfoItem {
    public String id;
    public String nickname;
    public char sex;
    public int age;
    public int weight;
    public int height;
    public int activity_index;
    public int target_calories;
    public byte[] profile;

    @Override
    public String toString(){
        return "MemberInfoItem{" +
                "id="+id+
                ", nickname='"+nickname+'\'' +
                ", sex='"+sex+'\''+
                ", age='"+age+'\'' +
                ", weight='"+weight+'\'' +
                ", height='"+height+'\'' +
                ", activity_index='"+activity_index+'\'' +
                ", target_calories='"+target_calories+'\'' +
                ", profile='"+profile+'\'' +
                '}';

    }

}
