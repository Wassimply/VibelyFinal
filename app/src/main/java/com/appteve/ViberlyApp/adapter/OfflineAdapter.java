package com.appteve.ViberlyApp.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appteve.ViberlyApp.R;
import com.appteve.ViberlyApp.model.OfflineItem;

import java.util.ArrayList;

/**
 * Created by appteve on 19/08/16.
 */
public class OfflineAdapter  extends ArrayAdapter {


    private final ArrayList<OfflineItem> offlineArrays;
    private final Context context;

    public OfflineAdapter(Context context, ArrayList<OfflineItem> offlineArrays) {
        super(context, R.layout.offlinetrack_item, offlineArrays);

        this.offlineArrays = offlineArrays;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.offlinetrack_item, parent, false);
        TextView categoryName = (TextView) item.findViewById(R.id.offtrackNameText);
        String maintxt = offlineArrays.get(position).getOfftrack_name();
        String txtOut = maintxt.replaceFirst("\\.mp3$", "");
        categoryName.setText(txtOut);


        return item;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }
}
