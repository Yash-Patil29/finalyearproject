package Adapters;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homie.GroupChatActivity;
import com.example.homie.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.core.Context;

import java.util.List;

import Models.AllMethods;
import Models.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {
    GroupChatActivity context;
    List<Message> messages;
    DatabaseReference messagedb;

    public MessageAdapter(GroupChatActivity context, List<Message>messages, DatabaseReference messagedb){
        this.context=context;
        this.messagedb=messagedb;
        this.messages=messages;

    }


    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        return new MessageAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
         Message message = messages.get(position);
         if(message.getName().equals(AllMethods.name)){
             holder.tvTitle.setText("You : " +message.getMessage());
             holder.tvTitle.setGravity(Gravity.START);


         }
         else {
             holder.tvTitle.setText(message.getName()+ ":"+ message.getMessage());
             holder.ibDelete.setVisibility(View.GONE);

         }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageButton ibDelete;
        LinearLayout l1;

        public MessageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=(TextView) itemView.findViewById(R.id.tvTitle);
            ibDelete=(ImageButton) itemView.findViewById(R.id.ibDelete);
            l1 =(LinearLayout)itemView.findViewById(R.id.l1message);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messagedb.child(messages.get(getAdapterPosition()).getKey()).removeValue();

                }
            });
        }
    }
}
