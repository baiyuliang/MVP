package com.byl.mvp.weight;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

public class RxView {

    //默认点击时间间隔为1秒
    @SuppressLint("CheckResult")
    public static void clicks(View view, View.OnClickListener onClickListener) {
        com.jakewharton.rxbinding4.view.RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS).subscribe(v -> onClickListener.onClick(view));
    }

    @SuppressLint("CheckResult")
    public static void clicks(View view, int sec, View.OnClickListener onClickListener) {
        com.jakewharton.rxbinding4.view.RxView.clicks(view).throttleFirst(sec, TimeUnit.SECONDS).subscribe(v -> onClickListener.onClick(view));
    }

    @SuppressLint("CheckResult")
    public static void longClicks(View view, View.OnLongClickListener onLongClickListener) {
        com.jakewharton.rxbinding4.view.RxView.longClicks(view).throttleFirst(1, TimeUnit.SECONDS).subscribe(v -> onLongClickListener.onLongClick(view));
    }

    public static void textChanges(EditText editText, OnEditListener onEditListener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onEditListener.onEdit(s == null ? "" : s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public interface OnEditListener {
        void onEdit(String str);
    }
}
