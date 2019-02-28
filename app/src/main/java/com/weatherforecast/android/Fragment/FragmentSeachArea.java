package com.weatherforecast.android.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weatherforecast.android.Activity.ActivityChooseArea;
import com.weatherforecast.android.LogUtil;
import com.weatherforecast.android.R;
import com.weatherforecast.android.SearchArea.SearchAreaList;
import com.weatherforecast.android.SearchArea.SearchAreaListAdapter;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

/**
 * Created by wzq547 on 2019/2/26.
 */

public class FragmentSeachArea extends Fragment {

    private static final String TAG = "FragmentSeachArea";

    private EditText seachEdit;
    private Button backButton;
    private ListView listView;
    private SearchAreaListAdapter adapter;
    private List<Basic> basicList = new ArrayList<>();
    private List<SearchAreaList> dataList = new ArrayList<>();

    private FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seach_area,container,false);
        seachEdit = (EditText) view.findViewById(R.id.fragment_seach_area_title_edittext);
        backButton = (Button) view.findViewById(R.id.fragment_seach_area_title_back);
        listView = (ListView) view.findViewById(R.id.fragment_seach_area_title_list);
        adapter = new SearchAreaListAdapter(getContext(),R.layout.list_search_area_adapter,dataList);
        listView.setAdapter(adapter);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fragment_seach_area_floatingActionButton);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mFragmentManager = getFragmentManager();
                mFragmentManager.popBackStack();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mFragmentManager = getFragmentManager();
                mFragmentManager.popBackStack();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchAreaList searchAreaList = dataList.get(position);
                ActivityChooseArea activitychoosearea = (ActivityChooseArea) getActivity();
                activitychoosearea.ReBackCountyNameToActivityWeather(searchAreaList.getCid());
            }
        });
        seachEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ) {
                    LogUtil.i(TAG, "onEditorAction: "+seachEdit.getText().toString());
                    String searchName = seachEdit.getText().toString();
                    if (searchName.isEmpty()){
                        Toast.makeText(getActivity(),"请输入要搜索的地名",Toast.LENGTH_SHORT).show();
                        return true;
                    }else {
                        HeWeather.getSearch(getActivity(), searchName,"CN", 20, Lang.CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener() {
                            @Override
                            public void onError(Throwable throwable) {
                                Toast.makeText(getActivity(),"您输入的地名可能有误",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(Search search) {
                                basicList = search.getBasic();
                                if (basicList.size() > 0 ){
                                    dataList.clear();
                                    for (Basic basic : basicList){
                                        dataList.add(new SearchAreaList(basic.getCid(),basic.getAdmin_area(),basic.getParent_city(),basic.getLocation()));
                                    }
                                    adapter.notifyDataSetChanged();
                                    listView.setSelection(0);
                                }else {
                                    Toast.makeText(getActivity(),"您输入的地名可能有误",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        return false;
                    }
                }
                return false;
            }
        });
    }
}
