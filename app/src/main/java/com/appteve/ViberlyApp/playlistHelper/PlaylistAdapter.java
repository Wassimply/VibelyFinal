package com.appteve.ViberlyApp.playlistHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appteve.ViberlyApp.R;

import java.util.ArrayList;

/**
 * Created by appteve on 15/08/16.
 */


public class PlaylistAdapter extends ArrayAdapter<PlaylistOptions> {

    private final ArrayList<PlaylistOptions> playlistOptions;
    private final Context context;

    public PlaylistAdapter(Context context, ArrayList<PlaylistOptions> playlistOptions) {
        super(context,R.layout.item_playlistmenu_options, playlistOptions);
        this.playlistOptions = playlistOptions;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_playlistmenu_options, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        vh.namePlaylist.setText(playlistOptions.get(position).getPlaylistName());

        return convertView;
    }

    private static final class ViewHolder {
        TextView namePlaylist;

        public ViewHolder(View v) {
            namePlaylist = (TextView) v.findViewById(R.id.nameplaylist);

        }
    }

}

