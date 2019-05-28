package com.example.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.smartbutler.R;
import com.example.smartbutler.adapter.WeChatAdapter;
import com.example.smartbutler.entity.WeChatData;
import com.example.smartbutler.ui.WebViewActivity;
import com.example.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/3 0003.
 * 微信精选
 */

public class WechatFragment extends Fragment {
    private ListView mListView;
    private List<WeChatData> mList=new ArrayList<>();
    //标题和URL
    private List<String> mListTitle=new ArrayList<>();
    private List<String> mListUrl=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_wechat,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mListView=view.findViewById(R.id.mListView);
        //解析http://v.juhe.cn/weixin/query?key=您申请的KEY
        String url="http://v.juhe.cn/weixin/query?key="+ StaticClass.WECHAT_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
            }
        });
        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title",mListTitle.get(i));
                intent.putExtra("url",mListUrl.get(i));
                startActivity(intent);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONObject jsonResult=jsonObject.getJSONObject("result");
            JSONArray jsonArray=jsonResult.getJSONArray("list");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject json=jsonArray.getJSONObject(i);
                WeChatData data=new WeChatData();
                data.setTitle(json.getString("title"));
                data.setSource(json.getString("source"));
                data.setImgUrl(json.getString("firstImg"));
                mList.add(data);
                mListTitle.add(json.getString("title"));
                mListUrl.add(json.getString("url"));
            }
            //新建适配器
            WeChatAdapter adapter=new WeChatAdapter(getActivity(),mList);
            //设置适配器
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
