package userInfo;

import java.io.Serializable;

public class User implements Serializable {
    private String uId;
    private int bestScore;
    private String location;
    public User() {
    }

    public User(String uId) {
        this.uId = uId;
        this.bestScore = 0;
        this.location = "Melbourne";
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

}
