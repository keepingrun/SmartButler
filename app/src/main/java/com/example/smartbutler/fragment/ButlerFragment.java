package com.example.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.smartbutler.R;
import com.example.smartbutler.adapter.ChatListAdapter;
import com.example.smartbutler.entity.ChatListData;
import com.example.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//导入包

/**
 * Created by Administrator on 2019/5/3 0003.
 * 管家服务
 */

public class ButlerFragment extends Fragment implements View.OnClickListener {
    private ListView mChatListView;
    private List<ChatListData> mList=new ArrayList<>();
    private ChatListAdapter adapter;
    //输入框
    private EditText et_text;
    //发送按钮
    private Button btn_send;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_butler,null);
        findView(view);
        return view;
    }

    private void findView(View view) {


        mChatListView=view.findViewById(R.id.mChatListView);
        et_text=view.findViewById(R.id.et_text);
        btn_send=view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        adapter=new ChatListAdapter(getActivity(),mList);
        mChatListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_send:
                //1.获取输入框的内容
                String text=et_text.getText().toString();
                //2.判断是否为空
                if(!TextUtils.isEmpty(text)){
                    //3.判断长度不能大于30
                    if(text.length()>30){
                        Toast.makeText(getActivity(),"输入长度超出限制",Toast.LENGTH_SHORT).show();
                    }else{
                        //4.清空当前输入框
                        et_text.setText("");
                        //5.添加你输入内容到right
                        addRightItem(text);
                        //6.发送给机器人请求返回内容
                        String url="http://op.juhe.cn/robot/index?info="+text+"&key="+ StaticClass.CHAT_LIST_KEY;
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                parsingJson(t);
                            }
                        });
                    }
                }else{
                    Toast.makeText(getActivity(),"输入框不能为空",Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONObject jsonResult=jsonObject.getJSONObject("result");
            //拿到返回值
            String text=jsonResult.getString("text");
            //7.拿到机器人的返回值之后添加到left
            addLeftItem(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftItem(String text){
        ChatListData data=new ChatListData();
        data.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }
    private void addRightItem(String text){
        ChatListData data=new ChatListData();
        data.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
        }
}
