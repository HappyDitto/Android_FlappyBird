package utils;

import static utils.Utils.getFirebaseRef;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import userInfo.User;

public  class DatabaseCRUD {
    private static DatabaseReference dbRef = getFirebaseRef();
    private List<User> topUser = new ArrayList<>();

    public DatabaseCRUD() {

    }

    public interface DataStatus{
        void TopScoreUserListIsLoaded();
    }

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

    public static Task getUserBestScore(String uId){
        return dbRef.child("Users").child(uId).child("bestScore").get();
    }

    public static void setUserBestScore(String uId, int score){
        dbRef.child("Users").child(uId).child("bestScore").setValue(score);
    }

    public static void getTopScoreUsers(int topN){
        Query bestScoreUsers = dbRef.child("Users").orderByChild("bestScore").limitToLast(topN);
        bestScoreUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> res = new ArrayList<User>();
                for (DataSnapshot item  : snapshot.getChildren()) {
//                    Log.i("看看信息：",snapshot.getValue().toString());
                    User user = item.getValue(User.class);
                    Log.i("看看信息1：",user.toString());
                    res.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("QUERY ERROR", error.getMessage());
            }
        });
    }



}
