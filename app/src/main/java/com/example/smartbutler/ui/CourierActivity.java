package com.example.smartbutler.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.smartbutler.R;
import com.example.smartbutler.adapter.CourierAdapter;
import com.example.smartbutler.entity.CourierData;
import com.example.smartbutler.utils.L;
import com.example.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourierActivity extends BaseActivity implements View.OnClickListener{
    private EditText et_name;
    private EditText et_number;
    private Button btn_get_courier;
    private ListView mListView;
    private List<CourierData> mList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        initView();
    }

    private void initView() {
        et_name=findViewById(R.id.et_name);
        et_number=findViewById(R.id.et_number);
        btn_get_courier=findViewById(R.id.btn_get_courier);
        btn_get_courier.setOnClickListener(this);
        mListView=findViewById(R.id.mListView);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_get_courier:
                //1.获取输入框的内容
                //5.listview适配器
                //6.实体类
                //7.设置显示
                String name=et_name.getText().toString().trim();
                String number=et_number.getText().toString().trim();
                //拼接url
                String url="http://v.juhe.cn/exp/index?key="+ StaticClass.COURIER_KEY
                        +"&com="+name
                        +"&no="+number;
                // 2.判断是否为空
                if(!TextUtils.isEmpty(name)&!TextUtils.isEmpty(number)){
                    // 3.通过聚合查询快递信息
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            //Toast.makeText(CourierActivity.this,t,Toast.LENGTH_SHORT).show();
                            //L.i("Json"+t);
                            // 4.解析JSON数据
                            parsingJson(t);
                        }
                    });


                }else{
                    Toast.makeText(this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //解析数据
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONObject jsonResult=jsonObject.getJSONObject("result");
            JSONArray jsonArray=jsonResult.getJSONArray("list");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject json=jsonArray.getJSONObject(i);
                CourierData data=new CourierData();
                data.setRemark(json.getString("remark"));
                data.setZone(json.getString("zone"));
                data.setDatetime(json.getString("datetime"));
                mList.add(data);
            }
            //倒序
            Collections.reverse(mList);
            CourierAdapter adapter = new CourierAdapter(this,mList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
