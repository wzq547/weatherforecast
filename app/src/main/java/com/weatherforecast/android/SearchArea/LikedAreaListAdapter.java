package com.weatherforecast.android.SearchArea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weatherforecast.android.R;

import java.util.List;

/**
 * Created by wzq547 on 2019/2/27.
 */

public class LikedAreaListAdapter extends ArrayAdapter<LikedAreaList> {

    private int resourceID;

    public LikedAreaListAdapter(Context context, int textViewResourceID, List<LikedAreaList> objects){

        super(context,textViewResourceID,objects);

        resourceID = textViewResourceID;
    }

    class ViewHolder{

        TextView adminArea;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LikedAreaList likedAreaList = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.adminArea = (TextView) view.findViewById(R.id.list_liked_area_adapter_adminArea);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.adminArea.setText(likedAreaList.getAdmin_area()+"，"+likedAreaList.getParent_city()+"，"+likedAreaList.getLocation());
        return view;
    }
}
