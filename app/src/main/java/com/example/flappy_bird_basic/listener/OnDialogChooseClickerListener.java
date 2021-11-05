package com.example.flappy_bird_basic.listener;

import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public interface OnDialogChooseClickerListener {
    /*
     * 选择照相
     * */
    void onTakePhoneItemClick(View view, BottomSheetDialog dialog);
    /*
     * 选择相册
     * */
    void onPhotoAlbumItemClick(View view, BottomSheetDialog dialog);
}

