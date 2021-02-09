package com.example.deneme;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<String> userKeyList;
    Activity activity;
    Context context;

    public UserAdapter(List<String> userKeyList, Activity activity, Context context) {
        this.userKeyList = userKeyList;
        this.activity = activity;
        this.context = context;
    }

    //layout tanımlası yapılacak sonrasında viewlara saetleme yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }
//adapter listesi sizeı
    @Override
    public int getItemCount() {
        return 0;
    }
//view tanımlanma işleri
    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View itemView){
            super(itemView);

        }

 }
}
