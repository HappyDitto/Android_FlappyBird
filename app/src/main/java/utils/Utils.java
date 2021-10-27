package utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Utils {

    /**
     * Toast a message
     * @param ctx Context
     * @param msg Message
     */
    public static void toast(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Get firebase reference
     * @return firebase reference
     */
    public static DatabaseReference getFirebaseRef(){
        return FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Open an activity
     * @param c Context
     * @param cls Target class
     */
    public static void openActivity(Context c,Class cls){
        Intent intent = new Intent(c, cls);
        c.startActivity(intent);
    }
}
