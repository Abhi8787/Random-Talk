package com.example.chatting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.viewholder> {

    Context homeActivity;
    ArrayList<Users> usersArrayList;
    HashSet<String> request;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseDatabase database1;
    Users temp;
    ProgressBar progressBar;
    HashSet<String> friends;

    ProgressDialog progressDialog;

    public FriendAdapter(Context homeActivity, ArrayList<Users> usersArrayList, HashSet<String> request , ProgressBar progressBar , HashSet<String> friends) {
        this.homeActivity = homeActivity;
        this.usersArrayList = usersArrayList;
        this.request = request;
        this.progressBar = progressBar;
        this.friends = friends;

    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.request_row,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Users users = usersArrayList.get(position);

        holder.setIsRecyclable(false);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid()))
        {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

        else {
            holder.user_name.setText(users.name);
            holder.user_status.setText(users.status);

            Picasso.get().load(users.imageUri).into(holder.user_profile);

            if (request.contains(users.uid))
                holder.sendRequest.setText("Request Sent");


            if(friends.contains(users.uid))
                holder.sendRequest.setText("Friends");

        }


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homeActivity,checkProfile.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReceiverImage",users.getImageUri());
                intent.putExtra("uid",users.getUid());
                intent.putExtra("status" , users.status);
                intent.putExtra("imageUri",users.imageUri);
                intent.putExtra("HomeActivity","");
                homeActivity.startActivity(intent);
            }
        });


        holder.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(homeActivity);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);

                progressDialog.show();

                auth = FirebaseAuth.getInstance();
                database = FirebaseDatabase.getInstance();
                database1 = FirebaseDatabase.getInstance();


                temp = new Users();
                temp = usersArrayList.get(holder.getAdapterPosition());

                String str = auth.getUid()+ "@" +temp.uid;

                database1.getReference().child("acceptRequest").child(str).setValue(str)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                database.getReference().child("requests").child(auth.getUid()).child(temp.uid).
                                        setValue(temp.uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    //   notifyDataSetChanged();

                                       progressDialog.dismiss();
                                       Intent intent = new Intent(homeActivity,RequestSent.class);
                                       intent.putExtra("name", temp.name);
                                       intent.putExtra("uid",temp.uid);
                                       intent.putExtra("imageUri",temp.imageUri);
                                       homeActivity.startActivity(intent);
                                    }
                                });


                                  // notifyDataSetChanged();
                            }
                        });
            }
        });


    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        CircleImageView user_profile;
        TextView user_name;
        TextView user_status;
        TextView sendRequest;
        LinearLayout view;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            user_profile = itemView.findViewById(R.id.userImage);
            user_name = itemView.findViewById(R.id.userName);
            user_status = itemView.findViewById(R.id.userStatus);
            sendRequest = itemView.findViewById(R.id.sendRequest);
            view = itemView.findViewById(R.id.row);

        }


    }
}
