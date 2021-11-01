package utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication {
    private FirebaseAuth myAuth;

    public Authentication() {
        this.myAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getMyAuth() {
        return myAuth.getCurrentUser();
    }

    public boolean isLogin() {
        return this.getMyAuth() != null;
    }
}
