package com.android.aifoodapp;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class ProgressDialog extends Dialog
{
    public ProgressDialog(Context context)
    {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//dialog 타이틀 안보이게
        setContentView(R.layout.loading);
    }
}