package utils;

import static utils.Utils.getFirebaseRef;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import userInfo.User;

public  class DatabaseCRUD {
    private static DatabaseReference dbRef = getFirebaseRef();
    public static void addUser(final Activity aca, final User user){
        dbRef.child("Users").child(user.getuId()).setValue(user);
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
        int[] res = {0};
        dbRef.child("Users").child(uId).child("bestScore").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.i("check best score：", String.valueOf(task.getResult().getValue()));
                res[0] =  Integer.parseInt(String.valueOf(task.getResult().getValue()));
            }
        });;
        return res[0];
    }

    public static void setUserBestScore(String uId, int score){
        dbRef.child("Users").child(uId).child("bestScore").setValue(score);
    }

}
