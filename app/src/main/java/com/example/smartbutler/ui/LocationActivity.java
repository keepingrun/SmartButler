package com.example.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.smartbutler.R;

/**
 * com.baidu.mapapi.map.MapView--得到-->BaiduMap
 * mLocationClient(设置监听器MyLocationListener，启动该对象监听)
 * (初始化mLocationClient)initLocation()
 * 监听器MyLocationListener获取定位信息
 * mBaiduMap.setMyLocationData(locData);
 */
public class LocationActivity extends BaseActivity {
    private com.baidu.mapapi.map.MapView bmapView;
    private BaiduMap mBaiduMap;
    //定位
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initView();
    }

    private void initView() {
        bmapView = findViewById(R.id.bmapView);
        mBaiduMap=bmapView.getMap();
        //定位
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
        mLocationClient.start();
        //mBaiduMap是地图控制器对象
        mBaiduMap.setMyLocationEnabled(true);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //移动到我的位置
            //设置缩放，确保屏幕内有我
            MapStatusUpdate mapUpdate = MapStatusUpdateFactory.zoomTo(18);
            mBaiduMap.setMapStatus(mapUpdate);

            //开始移动
            MapStatusUpdate mapLatlng = MapStatusUpdateFactory.
                    newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
            mBaiduMap.setMapStatus(mapLatlng);

            //绘制图层
            //定义Maker坐标点
            LatLng point = new LatLng(location.getLatitude(),location.getLongitude());
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
    }

        @Override
        protected void onResume() {
            super.onResume();
            //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
            bmapView.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
            bmapView.onPause();
        }

        @Override
        protected void onDestroy() {
            mLocationClient.stop();
            //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
            mBaiduMap.setMyLocationEnabled(false);
            bmapView.onDestroy();
            bmapView = null;
            super.onDestroy();

        }

}
