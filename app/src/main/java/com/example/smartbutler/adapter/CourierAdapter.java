package com.example.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartbutler.R;
import com.example.smartbutler.entity.CourierData;

import java.util.List;

public class CourierAdapter extends BaseAdapter {
    private Context mContext;
    private List<CourierData> mList;
    //布局加载器
    private LayoutInflater inflater;
    private CourierData data;
    public CourierAdapter(Context mContext,List<CourierData> mList){
        this.mContext=mContext;
        this.mList=mList;
        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        //第一次加载
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_courier_item,null);
            viewHolder.tv_remark=convertView.findViewById(R.id.tv_remark);
            viewHolder.tv_zone=convertView.findViewById(R.id.tv_zone);
            viewHolder.tv_datetime=convertView.findViewById(R.id.tv_datetime);
            //设置缓存
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        data=mList.get(position);
        viewHolder.tv_remark.setText(data.getRemark());
        viewHolder.tv_zone.setText(data.getZone());
        viewHolder.tv_datetime.setText(data.getDatetime());

        return convertView;
    }

    //优化
    class ViewHolder{
        private TextView tv_remark;
        private TextView tv_zone;
        private TextView tv_datetime;
    }
}
