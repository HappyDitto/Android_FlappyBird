package com.example.flappy_bird_basic;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import userInfo.User;
import utils.DatabaseCRUD;

public class TopUserList extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_user_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list);
        new DatabaseCRUD().getTopScoreUsers(new DatabaseCRUD.DataStatus() {
            @Override
            public void TopScoreUserListIsLoaded(List<User> topUserList) {
                new RecyclerViewConfig().setConfig(recyclerView,TopUserList.this, topUserList);
            }
        },10);
    }
}
