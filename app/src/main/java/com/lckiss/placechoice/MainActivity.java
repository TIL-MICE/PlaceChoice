package com.lckiss.placechoice;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lckiss.placechoice.Dao.LocationDao;
import com.lckiss.placechoice.Model.Location;
import com.lckiss.placechoice.Util.MyAdapter;
import com.lckiss.placechoice.R;
import com.lckiss.placechoice.Util.SettingSave;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<Location> outCity =new ArrayList<Location>();
    private List<Location> outCounty =new ArrayList<Location>();
    private List<Location> outProvince =new ArrayList<Location>();

    private LocationDao locationDao;
    private ListView listView;

    //省市县
    private static int cityCode;
    private static String cityName;
    private static int countyCode;
    private static String countyName;
    private static int provinceCode;
    private static String provinceName;

    //默认显示省
    private static int DEFAULT=1;
    private static final int FIRST=1;
    private static final int SECOND=2;
    private static final int NONE=3;

    private MyAdapter myAdapter;
    private TextView toolbarTitle;
    private ImageView backImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//toolbar
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle =(TextView) findViewById(R.id.toolbar_title);
        backImage=(ImageView)findViewById(R.id.toolbar_image);
        backImage.setVisibility(View.INVISIBLE);

        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listview);
//      初始化数据和写入数据库
        checkFirst();

    }

    private void checkFirst(){
        String M = "CheckFirst";
        SharedPreferences setting = getSharedPreferences(M, 0);
        Boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {//第一次
            setting.edit().putBoolean("FIRST", false).commit();
            timeConsuming();
            Toast.makeText(this,"正在初始化数据,请稍后",Toast.LENGTH_SHORT);
            toolbarTitle.setText("选择省份");
            show(outProvince);
        }else{
            locationDao=new LocationDao(this);
            outProvince=locationDao.findProvince();
            toolbarTitle.setText("选择省份");
            //不是第一次从数据库查询数据
            show(outProvince);
        }
    }
    public  void show(List datas){
        myAdapter=new MyAdapter(MainActivity.this,datas);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                switch (DEFAULT){
                    case FIRST:
                        //第一次点击,拿省级code并查出下属市
                        provinceCode=outProvince.get(position).getCode();
                        provinceName=outProvince.get(position).getName();
                        //储存偏好
                        SettingSave.saveSelectProvince(MainActivity.this,provinceCode);

                        outCity=locationDao.findCity(provinceCode);
                        //刷新列表
                        show(outCity);
                        toolbarTitle.setText("选择市区");
                        backImage.setVisibility(View.VISIBLE);
                        myAdapter.notifyDataSetChanged();
                        DEFAULT=SECOND;
                        break;
                    case SECOND:
                        //第二次点击,拿市级code,并查出下属县
                        cityCode=outCity.get(position).getCode();
                        cityName=outCity.get(position).getName();
                        //储存市级偏好
                        SettingSave.saveSelectCity(MainActivity.this,cityCode);

                        outCounty=locationDao.findCounty(cityCode);
                        toolbarTitle.setText("选择县城");
                        show(outCounty);
                        myAdapter.notifyDataSetChanged();
                        DEFAULT=NONE;
                        break;
                    case NONE:
                        countyName=outCounty.get(position).getName();
                        countyCode=outCounty.get(position).getCode();
                        SettingSave.saveSelectCounty(MainActivity.this,countyCode);

                        Toast.makeText(MainActivity.this,provinceName+"省"+cityName+"市"+countyName+"县",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    public void backwards(View v){
        switch (DEFAULT){
            case FIRST:
                backImage.setVisibility(View.INVISIBLE);
                break;
            case SECOND:
                show(outProvince);
                toolbarTitle.setText("选择省份");
                backImage.setVisibility(View.INVISIBLE);
                myAdapter.notifyDataSetChanged();

                DEFAULT=FIRST;
                break;
            case NONE:
                show(outCity);
                toolbarTitle.setText("选择市区");

                myAdapter.notifyDataSetChanged();
                DEFAULT=SECOND;
                break;
        }

    }


    //该处代码在主线程中运行，供子线程来调用，接收子线程的工作成果
    private void handlerData(final String data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,data,Toast.LENGTH_SHORT).show();
            }
        });
    }
//子线程执行耗时操作
    private void timeConsuming(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //初始化数据
                queryXMl();
                //将数据插入数据库
                insertDb();
                handlerData("读取数据成功");
            }
        }).start();
    }

    public void insertDb(){

        locationDao=new LocationDao(this);

        for (Location province : outProvince) {
            locationDao.addProvince(province);
//            Log.i("myinfo : ", "province Name :" +province.getName() + " Code :" + province.getCode());
        }

        for (Location city : outCity) {
            locationDao.addCity(city);
//            Log.i("myinfo : ", "city Name :" +city.getName() + " city Code :" + city.getCode()+" ProvinceCode :"+city.getHigherCode());
        }

        for (Location county : outCounty) {
            locationDao.addCounty(county);
//            Log.i("myinfo : ", "county Name :" + county.getName() + " county Code :" + county.getWeatherId()+" CityCode:"+county.getHigherCode());
        }

    }

    public void queryXMl() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            InputStream is =  getAssets().open("weather_area.xml");
            xmlPullParser.setInput(is, "UTF-8");
            int eventType = xmlPullParser.getEventType();
            int provinceCode=0;
            int cityCode=0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        Location province = new Location();
                        Location city = new Location();
                        Location county = new Location();
                        String tagName = xmlPullParser.getName();
                        if ("province".equals(tagName)) {
                            province.setName(xmlPullParser.getAttributeValue(0));
                            province.setCode(Integer.valueOf(xmlPullParser.getAttributeValue(1)));
                            provinceCode=Integer.valueOf(xmlPullParser.getAttributeValue(1));
                            outProvince.add(province);
                        } else if ("city".equals(tagName)) {
                            city.setName(xmlPullParser.getAttributeValue(0));
                            city.setCode(Integer.valueOf(xmlPullParser.getAttributeValue(1)));
                            city.setHigherCode(provinceCode);
                            cityCode=Integer.valueOf(xmlPullParser.getAttributeValue(1));
                            outCity.add(city);
                        } else if ("county".equals(tagName)) {
                            county.setName(xmlPullParser.getAttributeValue(0));
                            county.setWeatherId(xmlPullParser.getAttributeValue(1));
                            county.setHigherCode(cityCode);
                            outCounty.add(county);
                        }
                        break;
                    case XmlPullParser.END_TAG:{
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            Toast.makeText(this, "读取成功", Toast.LENGTH_LONG).show();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
