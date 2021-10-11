package com.example.task.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.task.Details_Activity;
import com.example.task.R;
import com.example.task.model.List_Model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class List_Adaptor extends RecyclerView.Adapter<List_Adaptor.RecyclerHolder> {

    List<List_Model> models;
    Context context;

    public List_Adaptor(List<List_Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public List_Adaptor.RecyclerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_list_item,null,false);

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull List_Adaptor.RecyclerHolder holder, int position) {

        holder.email.setText(models.get(position).getEmail().toLowerCase());
        holder.Name.setText(ChangeToCamelCase(models.get(position).getName()));
        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_id = models.get(position).getEmail().toString().toLowerCase();
                String[] emails = email_id.split(",");
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,emails);
                intent.putExtra(Intent.EXTRA_SUBJECT,"");
                intent.putExtra(Intent.EXTRA_TEXT,"");
                intent.setType("message/rfc822");
                context.startActivity(Intent.createChooser(intent,"Choose An Email"));
            }
        });

        holder.child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Details_Activity.class);
                intent.putExtra("EMP_ID",models.get(position).getId().toString());

                context.startActivity(intent);
            }
        });
    }

    public String ChangeToCamelCase(String name) {
        String[] words = name.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }

    @Override
    public int getItemCount() {
        return (models!=null)?models.size() :0;
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder{
        public TextView Name;
        public TextView email;
        public LinearLayout child;
        public RecyclerHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            email = (TextView) itemView.findViewById(R.id.email);
            Name = (TextView) itemView.findViewById(R.id.Name);
            child = (LinearLayout) itemView.findViewById(R.id.child);
        }
    }
}
