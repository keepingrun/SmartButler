package com.example.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.smartbutler.R;
import com.example.smartbutler.entity.GirlData;
import com.example.smartbutler.utils.PicassoUtils;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private List<GirlData> mList;
    private LayoutInflater inflater;
    private GirlData data;
    private WindowManager wm;
    private int width;
    public GridAdapter(Context mContext,List<GirlData> mList){
        this.mContext=mContext;
        this.mList=mList;
        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm= (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width=wm.getDefaultDisplay().getWidth();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.girl_item,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        data=mList.get(i);
        //解析图片
        String url = data.getImgUrl();
        PicassoUtils.loadImageViewSize(mContext,url,width/2,500,viewHolder.imageView);
        return convertView;
    }
    class ViewHolder{
        private ImageView imageView;
    }
}
