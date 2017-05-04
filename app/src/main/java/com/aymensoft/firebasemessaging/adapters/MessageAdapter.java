package com.aymensoft.firebasemessaging.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aymensoft.firebasemessaging.R;
import com.aymensoft.firebasemessaging.model.Message;
import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message>{

    Context context;
    int resource;

    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder = new Holder();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
            holder.photo=(ImageView)view.findViewById(R.id.photoImageView);
            holder.text=(TextView)view.findViewById(R.id.messageTextView);
            holder.author=(TextView)view.findViewById(R.id.nameTextView);
            view.setTag(holder);
        }else {
            holder=(Holder) view.getTag();
        }
        holder.author.setText(getItem(position).getName());
        boolean isPhoto = getItem(position).getPhotoUrl() != null;
        if (isPhoto) {
            holder.text.setVisibility(View.GONE);
            holder.photo.setVisibility(View.VISIBLE);
            Glide.with(holder.photo.getContext())
                    .load(getItem(position).getPhotoUrl())
                    .into(holder.photo);
        }else {
            holder.photo.setVisibility(View.GONE);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(getItem(position).getText());
        }
        return view;
    }

    class Holder{
        ImageView photo;
        TextView text, author;
    }

}
