package com.example.flappy_bird_basic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import userInfo.User;

public class RecyclerViewConfig {
    private Context ctx;
    private UsersAdapter mUsersAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<User> userList){
        ctx = context;
        mUsersAdapter = new UsersAdapter(userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        recyclerView.setAdapter(mUsersAdapter);
    }

    class UserItemView extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView userBestScore;
        private TextView userLocation;
        public UserItemView(ViewGroup parent){
            super(LayoutInflater.from(ctx).inflate(R.layout.best_user_item,parent,false));

            userName = (TextView) itemView.findViewById(R.id.user_name);
            userBestScore = (TextView) itemView.findViewById(R.id.user_best_score);
            userLocation = (TextView) itemView.findViewById(R.id.user_location);
        }

        public void bind(User user){
            userName.setText(user.getuName());
            userName.setTextColor(Color.parseColor("#f7fafa"));
            userName.setTextSize(25);
            userBestScore.setText(String.valueOf(user.getBestScore()));
            userBestScore.setTextColor(Color.parseColor("#f7fafa"));
            userBestScore.setTextSize(24);
            userLocation.setText(String.valueOf(user.getLocation()));
            userLocation.setTextColor(Color.parseColor("#f7fafa"));
            userLocation.setTextSize(24);
        }
    }

    class UsersAdapter extends RecyclerView.Adapter<UserItemView>{
        private List<User> mUserList;

        public UsersAdapter(List<User> mUserList) {
            this.mUserList = mUserList;
        }

        @NonNull
        @Override
        public UserItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull UserItemView holder, int position) {
            holder.bind(mUserList.get(position));
        }

        @Override
        public int getItemCount() {
            return mUserList.size();
        }
    }
}
