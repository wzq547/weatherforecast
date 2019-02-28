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

public class ChooseAreaListAdapter extends ArrayAdapter<String> {

    private int resourceID;

    public ChooseAreaListAdapter(Context context, int textViewResourceID, List<String> objects){

        super(context,textViewResourceID,objects);

        resourceID = textViewResourceID;
    }

    class ViewHolder{

        TextView areaName;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String areaName = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.areaName = (TextView) view.findViewById(R.id.list_choose_area_adapter_text);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.areaName.setText(areaName);
        return view;
    }
}
