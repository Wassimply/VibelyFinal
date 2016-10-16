package com.appteve.ViberlyApp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appteve.ViberlyApp.R;
import com.appteve.ViberlyApp.constant.Constant;
import com.appteve.ViberlyApp.model.RadioItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by appteve on 14/08/16.
 */
public class RadioAdapter extends ArrayAdapter<RadioItem> implements Constant {

    private Context context;
    private  int layoutResourceId;
    private ArrayList<RadioItem> dataArray = new ArrayList<RadioItem>();

    public RadioAdapter(Context context, int layoutResourceId, ArrayList<RadioItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.dataArray = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        RadioItem item = dataArray.get(position);
        holder.imageTitle.setText(item.getNameStyle());

        String imgUrl = BASE_AMAZON_ENDPOINT + BASE_BACKEND_AMAZON_SIMAGEF + item.getUrlImage();

        Glide.with(getContext())
                .load(imgUrl)
                .placeholder(R.drawable.defim)
                .dontAnimate()
                .centerCrop()
                .into(holder.image);
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }

}
