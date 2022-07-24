package com.example.chatting;

import static com.example.chatting.ChatActivity.receiverImage;
import static com.example.chatting.ChatActivity.senderImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == ITEM_SEND)
        {
           View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
           return new SenderViewHolder(view);
        }

        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

         Messages messages = messagesArrayList.get(position);

         if(holder.getClass() == SenderViewHolder.class)
         {
             SenderViewHolder viewHolder = (SenderViewHolder) holder;
             viewHolder.txt_message.setText(messages.getMessage());

             Picasso.get().load(senderImage).into(viewHolder.circleImageView);

         }

         else
         {
             ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
             viewHolder.txt_message.setText(messages.getMessage());

             Picasso.get().load(receiverImage).into(viewHolder.circleImageView);

         }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND;
        }
        else
        {
            return ITEM_RECEIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txt_message;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile);
            txt_message = itemView.findViewById(R.id.txtMessages);

        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txt_message;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile);
            txt_message = itemView.findViewById(R.id.txtMessages);

        }
    }

}
