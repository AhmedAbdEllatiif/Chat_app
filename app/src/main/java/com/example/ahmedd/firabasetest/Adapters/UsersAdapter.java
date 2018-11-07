package com.example.ahmedd.firabasetest.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public UsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User userItem = userList.get(position);

        holder.userName.setText(userItem.getUserName());
        if (userItem.getImageURL().equals("default")) {
            holder.profile_pic.setImageResource(R.mipmap.ic_launcher);
        } else {
            Picasso.get().load(userItem.getImageURL()).into(holder.profile_pic);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        ImageView profile_pic;

        public ViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName_AtCardView);
            profile_pic = itemView.findViewById(R.id.profile_pic_AtCardView);

        }
    }
}
