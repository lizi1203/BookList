package com.casper.testdrivendevelopment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.casper.testdrivendevelopment.data.ShopLoader;
import com.casper.testdrivendevelopment.data.mode.Shop;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapViewFragment extends Fragment {

    private MapView mapView=null;
    public MapViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_map_view, container, false);
       mapView=view.findViewById(R.id.map_View);

       //修改百度地图的初始位置
        BaiduMap baiduMap=mapView.getMap();
        LatLng centerPoint=new LatLng(22.255925,113.541112);
        MapStatus mMapStatus = new MapStatus.Builder().target(centerPoint).zoom(17).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        baiduMap.setMapStatus(mMapStatusUpdate);

        //添加标记点
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.book_icon);
        MarkerOptions markerOption = new MarkerOptions().icon(bitmap).position(centerPoint);
        Marker marker = (Marker) baiduMap.addOverlay(markerOption);

        //添加文字
        OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(50)
                .fontColor(0xFFFF00FF).text("暨南大学珠海").rotate(0).position(centerPoint);
        baiduMap.addOverlay(textOption);

        //响应事件
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                Toast.makeText(getContext(), "Marker被点击了！", Toast.LENGTH_SHORT).show();
                return false;
            }
    });
        final ShopLoader shopLoader=new ShopLoader();
        Handler handler=new Handler(){
            public void handleMessage(Message message){
                shopLoader.getShops();
            };
        };
        downloadAndDrawShops(baiduMap);

        return view;
    }

    private void downloadAndDrawShops(final BaiduMap baiduMap) {
        final ShopLoader shopLoader=new ShopLoader();
        final Handler handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                for(int i=0;i<shopLoader.getShops().size();++i)
                {
                    Shop shop=shopLoader.getShops().get(i);
                    LatLng point = new LatLng(shop.getLatitude(), shop.getLongitude());

                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.book_icon);
                    MarkerOptions markerOption = new MarkerOptions().icon(bitmap).position(point);
                    Marker marker = (Marker) baiduMap.addOverlay(markerOption);
                    //添加文字
                    OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(50)
                            .fontColor(0xFFFF00FF).text(shop.getName()).rotate(0).position(point);
                    baiduMap.addOverlay(textOption);
                }

            }
        };
        Runnable run=new Runnable() {
            @Override
            public void run() {
                String data=shopLoader.download("http://file.nidama.net/class/mobile_develop/data/bookstore.json");
                shopLoader.parseJson(data);
                handler.sendEmptyMessage(1);
            }
        };
        new Thread(run).start();
    }
   private MapView mMapView=null;
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if(mapView!=null)
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if(mapView!=null)
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(mapView!=null)
        mapView.onDestroy();
    }
}
