package com.casper.testdrivendevelopment.data;

import android.os.Handler;
import android.util.Log;

import com.casper.testdrivendevelopment.data.mode.Shop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ShopLoader {
    public ArrayList<Shop> getShops() {
        return shops;
    }

    private ArrayList<Shop> shops=new ArrayList<>();

    public String download(String urlString) {
        HttpURLConnection connection = null;
        try {
            // 调用URL对象的openConnection方法获取HttpURLConnection的实例
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            // 设置请求方式，GET或POST
            connection.setRequestMethod("GET");
            // 设置连接超时、读取超时的时间，单位为毫秒（ms）
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            // 设置是否使用缓存  默认是true
            connection.setUseCaches(false);
            //设置请求头里面的属性
            //connection.setRequestProperty();
            // 开始连接
            Log.i("HttpURLConnection.GET", "开始连接");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                Log.i("HttpURLConnection.GET", "请求成功");
                InputStream in = connection.getInputStream();
                // 使用BufferedReader对象读取返回的数据流
                // 按行读取，存储在StringBuider对象response中
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // 此处省略处理数据的代码,通过handler直接将返回的结果消息发送给UI线程列队
                return response.toString();
            }else{
                Log.i("HttpURLConnection.GET", "请求失败");
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (connection != null){
                // 结束后，关闭连接
                connection.disconnect();
            }
        }
        return "";
    }

    public void parseJson(String text)
    {
        shops.clear();
        try {
            //第一步：将从网络字符串jsonData字符串装入JSONObject，即JSONObject
            JSONObject jsonObject = new JSONObject(text);
            //第二步：因为多条数据，所以将"取出来的、要遍历的"字段装入JSONArray（这里要遍历data字段）
            JSONArray jsonArray=jsonObject.getJSONArray("shops");
            //第三步：循环遍历，依次取出JSONObject对象
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Shop shop=new Shop();
                shop.setName(jsonObject2.getString("name"));
                shop.setLatitude(jsonObject2.getDouble("latitude"));
                shop.setLongitude(jsonObject2.getDouble("longitude"));
                shop.setMemo(jsonObject2.getString("memo"));
                shops.add(shop);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
