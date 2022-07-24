package com.example.chatting;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder>{


    ArrayList<Interests> list;
    Context context;

    public InterestAdapter(ArrayList<Interests> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_interest_row,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Interests interests =  list.get(position);

        holder.imageView.setImageResource(interests.getImage());
        holder.textView.setText(interests.getText());

        if(interests.clickShow == 0)
        {
            holder.click.setVisibility(View.INVISIBLE);
        }

        else
        {
            holder.click.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(interests.clickShow == 0)
                {
                    holder.click.setVisibility(View.VISIBLE);
                    interests.clickShow = 1;
                }

                else
                {
                    holder.click.setVisibility(View.INVISIBLE);
                    interests.clickShow = 0;
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        ImageView click;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageR);
            textView = itemView.findViewById(R.id.textR);
            click = itemView.findViewById(R.id.clickR);
        }
    }


    public ArrayList<Interests> getSelecteed()
    {
        ArrayList<Interests> selectedInterestShow = new ArrayList<>();

        for(Interests interestss : list)
        {
            if(interestss.clickShow == 1.)
            {
                selectedInterestShow.add(interestss);
            }
        }

        return selectedInterestShow;
    }

    public ArrayList<Interests> getAll()
    {
        return list;
    }
}
