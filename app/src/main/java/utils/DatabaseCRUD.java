package utils;

import static utils.Utils.getFirebaseRef;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import userInfo.User;

public  class DatabaseCRUD {
    private static DatabaseReference dbRef = getFirebaseRef();
    public static void addUser(final Activity aca, final User user){
        dbRef.child("Users").push().setValue(user);
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Utils.toast(aca,"Account Created Successfully!!");
//                }else{
//                    Utils.toast(aca,"Failed");
//                }
//            }
//        });
    }

    public static int getUserBestScore(String uId){
        dbRef.child("User").child(uId).get();
        return 0;
    }

//    public static User getCurrentUser(){
//
//    }
}
