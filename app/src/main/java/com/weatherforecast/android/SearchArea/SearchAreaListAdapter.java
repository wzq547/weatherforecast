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

public class SearchAreaListAdapter extends ArrayAdapter<com.weatherforecast.android.SearchArea.SearchAreaList> {

    private int resourceID;

    public SearchAreaListAdapter (Context context, int textViewResourceID, List<SearchAreaList> objects){

        super(context,textViewResourceID,objects);

        resourceID = textViewResourceID;
    }

    class ViewHolder{

        TextView adminArea;

        TextView parentCity;

        TextView location;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchAreaList searchAreaList = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.adminArea = (TextView) view.findViewById(R.id.list_search_area_adapter_adminArea);
            viewHolder.parentCity = (TextView) view.findViewById(R.id.list_search_area_adapter_parentCity);
            viewHolder.location = (TextView) view.findViewById(R.id.list_search_area_adapter_location);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.adminArea.setText(searchAreaList.getAdmin_area());
        viewHolder.parentCity.setText(searchAreaList.getParent_city());
        viewHolder.location.setText(searchAreaList.getLocation());
        return view;
    }
}
