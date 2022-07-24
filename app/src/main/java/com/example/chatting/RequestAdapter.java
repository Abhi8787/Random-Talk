package com.example.chatting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.viewholder>{

    Context homeActivity;
    ArrayList<Users> usersArrayList;
    HashSet<String> request;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseDatabase database1;
    FirebaseDatabase database2;
    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;
    ProgressDialog progressDialog2;
    FirebaseDatabase database3;
    FirebaseDatabase database4;
    FirebaseDatabase database5;

    public RequestAdapter(Context homeActivity, ArrayList<Users> usersArrayList, HashSet<String> request) {
        this.homeActivity = homeActivity;
        this.usersArrayList = usersArrayList;
        this.request = request;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(homeActivity).inflate(R.layout.request_received_row,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.setIsRecyclable(false);

        Log.d("Adapter", getItemCount()+"");

        Users users = usersArrayList.get(position);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid()))
        {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

        auth = FirebaseAuth.getInstance();
        String str = users.uid+"@"+auth.getUid();

        if(request.contains(str))
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
                Intent intent = new Intent(homeActivity,checkProfile.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReceiverImage",users.getImageUri());
                intent.putExtra("uid",users.getUid());
                intent.putExtra("status" , users.status);
                homeActivity.startActivity(intent);
            }
        });




        holder.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(homeActivity);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);

                progressDialog1 = new ProgressDialog(homeActivity);
                progressDialog1.setMessage("Please Wait...");
                progressDialog1.setCancelable(false);

                progressDialog2 = new ProgressDialog(homeActivity);
                progressDialog2.setMessage("Please Wait...");
                progressDialog2.setCancelable(false);


                auth = FirebaseAuth.getInstance();
                database = FirebaseDatabase.getInstance();
                database1 = FirebaseDatabase.getInstance();
                database2 = FirebaseDatabase.getInstance();
                database3 = FirebaseDatabase.getInstance();
                database4 = FirebaseDatabase.getInstance();
                database5 = FirebaseDatabase.getInstance();

                database.getReference().child("acceptRequest").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        progressDialog.show();

                        String str = users.uid+"@"+auth.getUid();

                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            String dbstr = dataSnapshot.getValue(String.class);

                            if(str.equals(dbstr))
                            {
                                database1.getReference().child("mychats").child(auth.getUid()).child(dbstr).setValue(dbstr)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                String recchat = auth.getUid()+ "@" +users.uid;
                                                database1.getReference().child("mychats").child(users.uid).child(recchat)
                                                        .setValue(recchat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        database4.getReference().child("friends").child(auth.getUid()).child(users.uid)
                                                                .setValue(users.uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                database5.getReference().child("friends").child(users.uid).child(auth.getUid())
                                                                        .setValue(auth.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {


                                                                        progressDialog1.show();

                                                                        database2.getReference().child("acceptRequest").child(str).removeValue()
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                        progressDialog2.show();

                                                                                        request.remove(str);
                                                                                        ///progressBar.setVisibility(View.GONE);
                                                                                        progressDialog.dismiss();
                                                                                        progressDialog2.dismiss();
                                                                                        Intent intent = new Intent(homeActivity,RequestAccepted.class);
                                                                                        intent.putExtra("name" , users.name);
                                                                                        intent.putExtra("imageUri",users.imageUri);
                                                                                        intent.putExtra("uid",users.uid);
                                                                                        homeActivity.startActivity(intent);
                                                                                        notifyDataSetChanged();
                                                                                    }
                                                                                });

                                                                        progressDialog1.dismiss();






                                                                    }
                                                                });

                                                            }
                                                        });

                                                    }
                                                });


                                            }
                                        });
                            }

                        }
                        progressDialog.dismiss();


                        notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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

        public viewholder(@NonNull View itemView) {
            super(itemView);

            user_profile = itemView.findViewById(R.id.userImage);
            user_name = itemView.findViewById(R.id.userName);
            user_status = itemView.findViewById(R.id.userStatus);
            sendRequest = itemView.findViewById(R.id.sendRequest);


        }


    }
}
