package com.appteve.ViberlyApp.playerMenuHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appteve.ViberlyApp.R;

import java.util.List;

/**
 * Created by appteve on 15/08/16.
 */


public class PopMenuAdapter extends ArrayAdapter<PopMenuOptions> {

    public PopMenuAdapter(Context context, List<PopMenuOptions> donationOptions) {
        super(context, 0, donationOptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_popmenu_options, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        PopMenuOptions option = getItem(position);

        vh.description.setText(option.description);
        vh.amount.setText(option.amount);

        return convertView;
    }

    private static final class ViewHolder {
        TextView description;
        TextView amount;

        public ViewHolder(View v) {
            description = (TextView) v.findViewById(R.id.description);
            amount = (TextView) v.findViewById(R.id.amount);
        }
    }
}