package com.example.chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.viewholder> {

    Context context;
    ArrayList<String> listArrayList;


    public ListAdapter(Context context, ArrayList<String> listArrayList) {
        this.context = context;
        this.listArrayList = listArrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_row,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        String str = listArrayList.get(position);
        holder.textView.setText(str);
    }

    @Override
    public int getItemCount() {
        return listArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder
    {
        TextView textView;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.interestText);

        }
    }

}
