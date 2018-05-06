package com.jet.slaveofflower.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jet.slaveofflower.R;
import com.jet.slaveofflower.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import butterknife.ButterKnife;

/**
 * Created by shenqianqian on 2017/5/7.
 */
public class Sunflower_Fragment extends BaseFragment {
    public ImageButton mButton;
    public ImageView mSunflower;
    public TextView my_tree;
    public Button btn_temp;
    public TextView my_temp;
    public int day;
    public int nowaday;
    public int tree;
    public int messgaeId=R.string.sunflower;

    String my_data;
    String my_temp_s;
    public static final int SHOW_RESPONSE = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    // 在这里进行UI操作，将结果显示到界面上
                    my_temp.setText(my_data+" "+my_temp_s);
                    //----------------------------------
            }
        }

    };

    @Override
    public boolean hasMultiFragment() {
        return true;
    }
    @Override
    protected String setFragmentName() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nowaday = 0;
        day = 0;
        tree = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sun, container, false);
        Log.i("SunFlower", nowaday + "");
        mButton=(ImageButton)view.findViewById(R.id.water_Button);
        mSunflower=(ImageView)view.findViewById(R.id.sun_View);
        my_tree=(TextView)view.findViewById(R.id.my_tree);
        btn_temp=(Button)view.findViewById(R.id.btn_temp);
        my_temp=(TextView)view.findViewById(R.id.my_temp);
        my_tree.setText("当前养成:"+Integer.valueOf(tree));
        btn_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithHttpURLConnection();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day+=1;
                nowaday=day%10;
                if(nowaday==0){
                    mSunflower.setBackgroundResource(R.drawable.f_0);
                }else if(nowaday==1){
                    mSunflower.setBackgroundResource(R.drawable.f_1);
                }else if(nowaday==2){
                    mSunflower.setBackgroundResource(R.drawable.f_2);
                }else if(nowaday==3){
                    mSunflower.setBackgroundResource(R.drawable.f_3);
                }else if(nowaday==4){
                    mSunflower.setBackgroundResource(R.drawable.f_4);
                }else if(nowaday==5){
                    mSunflower.setBackgroundResource(R.drawable.f_5);
                }else if(nowaday==6){
                    mSunflower.setBackgroundResource(R.drawable.f_6);
                }else if(nowaday==7){
                    mSunflower.setBackgroundResource(R.drawable.f_7);
                }else if(nowaday==8){
                    mSunflower.setBackgroundResource(R.drawable.f_8);
                }else if (nowaday==9){
                    mSunflower.setBackgroundResource(R.drawable.f_9);
                    tree+=1;
                    my_tree.setText("当前养成:"+Integer.valueOf(tree));
                    Toast.makeText(getActivity(),messgaeId,Toast.LENGTH_SHORT).show();
                }
            }
        });

        ButterKnife.bind(this, view);
        return view;
    }
    public static Sunflower_Fragment newInstance(){
        Bundle args=new Bundle();
        Sunflower_Fragment fragment=new Sunflower_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    protected void sendRequestWithHttpURLConnection() {
        new Thread() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection connection = null;
                try {
                    String cityName = URLEncoder.encode("杭州", "utf-8");
                    url = new URL(
                            "http://v.juhe.cn/weather/index?format=2&cityname="
                                    + cityName
                                    + "&key=b9cc5b7d762f7f8b34cd957f33b53d85");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("response=" + response.toString());
                    //parseWithJSON(response.toString());
                    parseWeatherWithJSON(response.toString());
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    // 将服务器返回的结果存放到Message中
                    message.obj = response.toString();
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }.start();
    }

    protected void parseWeatherWithJSON(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            String resultcode=jsonObject.getString("resultcode");
            if(resultcode.equals("200")){
                JSONObject resultObject=jsonObject.getJSONObject("result");
                JSONObject todayObject=resultObject.getJSONObject("today");
                String date_y=todayObject.getString("date_y");
                my_data=date_y;
                String week=todayObject.getString("week");
                String temperature=todayObject.getString("temperature");
                my_temp_s=temperature;
                Log.d("MainActivity", "date_y="+date_y+"week="+week+"temp="+temperature);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
