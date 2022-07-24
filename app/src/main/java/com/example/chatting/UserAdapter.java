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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {
    Context homeActivity;
    ArrayList<Users> usersArrayList;
    HashMap<String,ArrayList<String>> hm;
    ArrayList<String> array;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseDatabase database1;
    ProgressDialog progressDialog;
    Users tempp;

    HashSet<String> friends;
    HashSet<String> request;




    public UserAdapter(Context homeActivity, ArrayList<Users> usersArrayList, HashMap<String, ArrayList<String>> hm,
                       ArrayList<String> array , HashSet<String> friends , HashSet<String> request) {
        this.homeActivity = homeActivity;
        this.usersArrayList = usersArrayList;
        this.hm = hm;
        this.array = array;
        this.friends = friends;
        this.request = request;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.match_row,parent,false);
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


        Boolean flag = false;

       ArrayList<String> temp = new ArrayList<>();

      if(hm.containsKey(users.uid) )
       temp = hm.get(users.uid);
      else
          temp = new ArrayList<>();

        for(String val : temp)
        {
            for(String vall : array)
            {
                if(val.equals(vall))
                    flag = true;

                Log.d("fire" , val + "----" + vall);
            }
        }



        if(flag == true )
        {
            holder.user_name.setText(users.name);
            holder.user_status.setText(users.status);

            Picasso.get().load(users.imageUri).into(holder.user_profile);



            if (request.contains(users.uid))
                holder.sendRequest.setText("Request Sent");


            if(friends.contains(users.uid))
                holder.sendRequest.setText("Friends");


        }

        else
        {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }



        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homeActivity,checkProfileMatch1.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReceiverImage",users.getImageUri());
                intent.putExtra("uid",users.getUid());
                intent.putExtra("status" , users.status);
                intent.putExtra("imageUri",users.imageUri);
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


                tempp = new Users();
                tempp = usersArrayList.get(holder.getAdapterPosition());

                String str = auth.getUid()+ "@" +tempp.uid;

                database1.getReference().child("acceptRequest").child(str).setValue(str)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                database.getReference().child("requests").child(auth.getUid()).child(tempp.uid).
                                        setValue(tempp.uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //   notifyDataSetChanged();

                                        progressDialog.dismiss();
                                        Intent intent = new Intent(homeActivity,RequestSent.class);
                                        intent.putExtra("HomeActivity" , "HomeActivity");
                                        intent.putExtra("name", tempp.name);
                                        intent.putExtra("uid",tempp.uid);
                                        intent.putExtra("imageUri",tempp.imageUri);
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

    class viewholder extends RecyclerView.ViewHolder{

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
