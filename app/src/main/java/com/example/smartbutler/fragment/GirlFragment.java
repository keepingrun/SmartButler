package com.example.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.smartbutler.R;
import com.example.smartbutler.adapter.GridAdapter;
import com.example.smartbutler.entity.GirlData;
import com.example.smartbutler.utils.L;
import com.example.smartbutler.utils.PicassoUtils;
import com.example.smartbutler.utils.StaticClass;
import com.example.smartbutler.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2019/5/3 0003.
 * 美女精选
 * GridView,GridViewAdapter
 */

public class GirlFragment extends Fragment {
    private GridView mGridView;
    //数据
    private List<GirlData> mList=new ArrayList<>();
    //适配器
    private GridAdapter mAdapter;
    //图片地址的数据
    private List<String> mListUrl = new ArrayList<>();
    //提示框
    private CustomDialog dialog;
    //预览图片
    private ImageView iv_img;
    //PhotoView
    private PhotoViewAttacher mAttacher;

    /**
     * 1.监听点击事件
     * 2.提示框
     * 3.加载图片
     * 4.PhotoView缩放图片
     *
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_girl,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mGridView=view.findViewById(R.id.mGridView);
        //初始化提示框
        dialog=new CustomDialog(getActivity()
                , LinearLayout.LayoutParams.MATCH_PARENT
                ,LinearLayout.LayoutParams.MATCH_PARENT
                ,R.layout.dialog_girl
                ,R.style.Theme_dialog, Gravity.CENTER
                ,R.style.pop_anim_style);
        dialog.setCanceledOnTouchOutside(true);
        iv_img=dialog.findViewById(R.id.iv_img);
        RxVolley.get(StaticClass.GIRL_KEY, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("Girl Json:" + t);
                parsingJson(t);
            }
        });
        //监听GridView的点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //解析图片
                PicassoUtils.loadImageView(getActivity(),mListUrl.get(i),iv_img);
                //缩放
                mAttacher = new PhotoViewAttacher(iv_img);
                //刷新
                mAttacher.update();
                dialog.show();
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject json= (JSONObject) jsonArray.get(i);
                String url=json.getString("url");
                mListUrl.add(url);
                GirlData data=new GirlData();
                data.setImgUrl(url);
                mList.add(data);
            }
            mAdapter=new GridAdapter(getActivity(),mList);
            //设置适配器
            mGridView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
