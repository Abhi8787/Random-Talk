package com.example.chatting;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyChatAdapter extends RecyclerView.Adapter<MyChatAdapter.viewholder>{

    Context context;
    ArrayList<Users> usersArrayList;
    HashSet<String> hs;

    public MyChatAdapter(Context context, ArrayList<Users> usersArrayList, HashSet<String> hs) {
        this.context = context;
        this.usersArrayList = usersArrayList;
        this.hs = hs;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_row,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.setIsRecyclable(false);

        Users users = usersArrayList.get(position);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid()))
        {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }


        if(hs.contains(users.uid))
        {
            holder.user_name.setText(users.name);
            holder.user_status.setText(users.status);

            Picasso.get().load(users.imageUri).into(holder.user_profile);
        }
        else
        {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReceiverImage",users.getImageUri());
                intent.putExtra("uid",users.getUid());
                intent.putExtra("check","no");
                context.startActivity(intent);
            }
        });






    }

    @Override
    public int getItemCount() {
        return  usersArrayList.size();
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
