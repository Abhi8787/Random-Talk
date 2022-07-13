package com.example.chatting;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.time.temporal.Temporal;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {
    Context homeActivity;
    ArrayList<Users> usersArrayList;
    public UserAdapter(HomeActivity homeActivity, ArrayList<Users> usersArrayList) {
      this.homeActivity = homeActivity;
      this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.item_user_row,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Users users = usersArrayList.get(position);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid()))
        {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }


        holder.user_name.setText(users.name);
        holder.user_status.setText(users.status);

        Picasso.get().load(users.imageUri).into(holder.user_profile);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(homeActivity,ChatActivity.class);
               intent.putExtra("name",users.getName());
               intent.putExtra("ReceiverImage",users.getImageUri());
               intent.putExtra("uid",users.getUid());
               homeActivity.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    class viewholder extends RecyclerView.ViewHolder{

        CircleImageView user_profile;
        TextView user_name;
        TextView user_status;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            user_profile = itemView.findViewById(R.id.userImage);
            user_name = itemView.findViewById(R.id.userName);
            user_status = itemView.findViewById(R.id.userStatus);


        }
    }
}
