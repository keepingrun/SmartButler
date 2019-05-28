package com.example.smartbutler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartbutler.R;
import com.example.smartbutler.entity.WeChatData;
import com.example.smartbutler.utils.L;
import com.example.smartbutler.utils.PicassoUtils;

import java.util.List;

public class WeChatAdapter extends BaseAdapter {
    private Context mContext;
    private List<WeChatData> mList;
    private LayoutInflater inflater;
    private WeChatData data;
    private int width,height;
    private WindowManager wm;

    public WeChatAdapter(Context mContext,List<WeChatData> mList){
        this.mContext=mContext;
        this.mList=mList;
        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm= (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width=wm.getDefaultDisplay().getWidth();
        height=wm.getDefaultDisplay().getHeight();
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
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.wechat_item,null);
            viewHolder.iv_img=convertView.findViewById(R.id.iv_img);
            viewHolder.tv_title=convertView.findViewById(R.id.tv_title);
            viewHolder.tv_source=convertView.findViewById(R.id.tv_source);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        data=mList.get(position);
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_source.setText(data.getSource());
        if(!TextUtils.isEmpty(data.getImgUrl())){
            //加载图片
            PicassoUtils.loadImageViewSize(mContext,data.getImgUrl(), width/3, 250, viewHolder.iv_img);

        }


        return convertView;
    }


    class ViewHolder{
        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_source;
    }

}
