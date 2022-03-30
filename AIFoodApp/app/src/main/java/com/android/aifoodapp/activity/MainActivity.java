package com.android.aifoodapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.aifoodapp.R;
import com.kakao.sdk.user.UserApiClient;

import java.security.MessageDigest;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MainActivity<Unit> extends AppCompatActivity {

    Activity activity;
    Button btn_logout;

    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", key);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getKeyHash();
    }

    //변수 초기화
    private void initialize(){
        activity = this;
        btn_logout = findViewById(R.id.btn_logout);
    }

   /*btn_logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                @Override
                public Unit invoke(Throwable throwable) {
                    ((LoginActivity)LoginActivity.mContext).updateKakaoLoginUi();
                    return null;
                }
            });
        }
    });*/

}