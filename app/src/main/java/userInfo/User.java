package userInfo;

import android.net.Uri;

import java.io.Serializable;
import java.net.URI;

public class User implements Serializable {
    private String uId;
    private int bestScore;
    private String location;
    private String uName;
    public User() {
    }

    public User(String uId, String uName) {
        this.uId = uId;
        this.bestScore = 0;
        this.location = "Melbourne";
        if(uName!=null) this.uName = uName;
        else this.uName = "Default Name";
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    @Override
    public String toString() {
        return "User{" +
                "uId='" + uId + '\'' +
                ", bestScore=" + bestScore +
                ", location='" + location + '\'' +
                ", uName='" + uName + '\'' +
                '}';
    }
}
