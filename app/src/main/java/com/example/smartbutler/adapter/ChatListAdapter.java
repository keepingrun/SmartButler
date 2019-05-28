package com.example.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartbutler.R;
import com.example.smartbutler.entity.ChatListData;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {
    //左边的type
    public static final int VALUE_LEFT_TEXT=1;
    //右边的tyep
    public static final int VALUE_RIGHT_TEXT=2;

    private Context mContext;
    private LayoutInflater inflater;
    private ChatListData data;
    private List<ChatListData> mList;

    public ChatListAdapter(Context mContext,List<ChatListData> mList){
        this.mContext=mContext;
        this.mList=mList;
        //获取系统服务
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
        ViewHolderLeftText viewHolderLeftText=null;
        ViewHolderRightText viewHolderRightText=null;
        //根据type区分数据的加载
        int type=getItemViewType(position);
        if(convertView==null){
            switch(type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText=new ViewHolderLeftText();
                    convertView=inflater.inflate(R.layout.left_item,null);
                    viewHolderLeftText.tv_left_text=convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeftText);
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText=new ViewHolderRightText();
                    convertView=inflater.inflate(R.layout.right_item,null);
                    viewHolderRightText.tv_right_text=convertView.findViewById(R.id.tv_right_text);
                    convertView.setTag(viewHolderRightText);
                    break;
            }
        }else{
            switch(type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText= (ViewHolderLeftText) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText= (ViewHolderRightText) convertView.getTag();
                    break;
            }
        }
        data=mList.get(position);
        switch(type){
            case VALUE_LEFT_TEXT:
                viewHolderLeftText.tv_left_text.setText(data.getText());
                break;
            case VALUE_RIGHT_TEXT:
                viewHolderRightText.tv_right_text.setText(data.getText());
                break;
        }
        return convertView;
    }

    //根据数据源的positiion来返回要显示的item
    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 3;//左右，加上本身
    }

    class ViewHolderLeftText{
        private TextView tv_left_text;
    }

    class ViewHolderRightText{
        private TextView tv_right_text;
    }
}
