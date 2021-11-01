package userInfo;

import android.net.Uri;

import java.io.Serializable;

public class User implements Serializable {
    private String uId;
    private int bestScore;
    private String location;
    private String uName;
    private Uri photoURL;
    public User() {
    }

    public User(String uId, String uName, Uri photoURL) {
        this.uId = uId;
        this.bestScore = 0;
        this.location = "Melbourne";
        this.uName = uName;
        this.photoURL = photoURL;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public Uri getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(Uri photoURL) {
        this.photoURL = photoURL;
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
                ", photoURL=" + photoURL +
                '}';
    }
}
