package com.aymensoft.firebasemessaging.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aymensoft.firebasemessaging.R;
import com.aymensoft.firebasemessaging.model.Message;
import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message>{

    Context context;
    int resource;

    //SharedPreferences
    SharedPreferences sharedpreferences;
    public static final String UserPREFERENCES = "UserSession" ;
    public static final String shareduserid = "shareduserid";
    public static String UserID;

    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        //get UserId
        sharedpreferences = this.getContext().getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        UserID=sharedpreferences.getString(shareduserid, "0");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder = new Holder();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
            holder.mylayout=(RelativeLayout)view.findViewById(R.id.rl_my_message);
            holder.yourlayout=(RelativeLayout)view.findViewById(R.id.rl_your_message);
            holder.mypicture=(ImageView)view.findViewById(R.id.img_my_picture);
            holder.yourpicture=(ImageView)view.findViewById(R.id.img_your_picture);
            holder.mymessage=(TextView)view.findViewById(R.id.tv_my_mesaage);
            holder.yourmessage=(TextView)view.findViewById(R.id.tv_your_mesaage);
            view.setTag(holder);
        }else {
            holder=(Holder) view.getTag();
        }

        String fireUserId = getItem(position).getUserid();
        if (fireUserId.equals(UserID)) {
            holder.yourlayout.setVisibility(View.GONE);
            holder.mylayout.setVisibility(View.VISIBLE);
            holder.mymessage.setText(getItem(position).getText());
            Glide.with(holder.mypicture.getContext())
                    .load(getItem(position).getPhotoUrl())
                    .into(holder.mypicture);
        }else {
            holder.yourlayout.setVisibility(View.VISIBLE);
            holder.mylayout.setVisibility(View.GONE);
            holder.yourmessage.setText(getItem(position).getText());
            Glide.with(holder.yourpicture.getContext())
                    .load(getItem(position).getPhotoUrl())
                    .into(holder.yourpicture);
        }

        return view;
    }

    class Holder{
        RelativeLayout mylayout, yourlayout;
        ImageView mypicture, yourpicture;
        TextView mymessage, yourmessage;
    }

}
