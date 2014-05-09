package com.example.streetlocation.org;

import java.util.ArrayList;
import java.util.List;


import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.Overlay;
import com.tencent.tencentmap.mapsdk.map.PoiOverlay;
import com.tencent.tencentmap.mapsdk.map.Projection;
import com.tencent.tencentmap.mapsdk.search.PoiItem;
import com.tencent.tencentmap.mapsdk.search.PoiResults;
import com.tencent.tencentmap.mapsdk.search.PoiSearch;
import com.tencent.tencentmap.streetviewsdk.StreetViewListener;
import com.tencent.tencentmap.streetviewsdk.StreetViewShow;

import com.tencent.tencentmap.streetviewsdk.map.basemap.GeoPoint;
import com.tencent.tencentmap.streetviewsdk.overlay.ItemizedOverlay;

import android.app.Activity;    
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import app.tabsample.HomeActivity.SimulateLocationOverlay;
import app.tabsample.StreetView.LocListener;


public class TestFragmentActivity extends FragmentActivity implements StreetViewListener  {    
	/**
     * View Container
     */
    private ViewGroup mContainer;
    private Handler mHandler;
    private View mStreetView2;
    Handler handler=new Handler();
    Runnable r;
    
    com.tencent.tencentmap.mapsdk.map.GeoPoint isdraw=new com.tencent.tencentmap.mapsdk.map.GeoPoint(0,0);
    /**
     * 街景View
     */
    private View mStreetView;
 //
    int mReqType, mReqGeoType, mReqLevel;
    LocListener mListener; // 接受回调信息
    boolean longClick=false;//长按标志位
    
    GeoPoint center=new GeoPoint((int)(30.519922 * 1E6), (int)(114.397054 * 1E6));

    /** Called when the activity is first created. */    
    public void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);    
        setContentView(R.layout.navigation);    
        
        
        //定点
        
        FirstFragment fragment = (FirstFragment)getSupportFragmentManager().findFragmentById(R.id.firstFragment);
        
        
        fragment.mMapController.animateTo(new com.tencent.tencentmap.mapsdk.map.GeoPoint((int)(30.519922 * 1E6), (int)(114.397054 * 1E6)));
        
        //1111
        mContainer = (LinearLayout)findViewById(R.id.layout2);
        StreetViewShow.getInstance().showStreetView(this, center, 100, this, -170, 0);
            
//        FirstFragment firstFragment=new FirstFragment();    
//        //在Activity中通过这个与Fragment通讯    
//        getFragmentManager().beginTransaction().add(android.R.id.content, firstFragment).commit();    
            
    //    FragmentManager fm = getSupportFragmentManager();    
     //  addShowHideListener(R.id.btn_1, fm.findFragmentById(R.id.firstFragment));    
       //addShowHideListener(R.id.btn_2, fm.findFragmentById(R.id.secondFragment));   
        
     //显示第一个fragment
        if (findViewById(R.id.firstFragment) != null) {
        	if (savedInstanceState != null) {
                return;
            }
        	//FirstFragment firstFragment = new FirstFragment();
        	// firstFragment.setArguments(getIntent().getExtras());
        	// getSupportFragmentManager().beginTransaction().add(R.id.firstFragment, firstFragment).commit();
        	 
        }
       
        // 显示全景当前所在
        
         r=new Runnable(){
            public void run() {
                 // TODO Auto-generated method stub
                //要做的事情，这里可以再次调用此Runnable对象，在线程中设定2秒后再执行自己，从而实现每两秒实现一次的定时器操作
            	int latitude=StreetViewShow.getInstance().getStreetStatus(). latitudeE6;
                int langtitude=StreetViewShow.getInstance().getStreetStatus(). longitudeE6;
                String s =
                    String. format("lon=%f,lat=%f", latitude * 1E-6, langtitude * 1E-6);
                Log. d("全景坐标", s);
                
                //发送当前坐标给地图
                Message msg = new Message();  
                Bundle b = new Bundle();  
                TestPoint send=new TestPoint();
                send.altitude=latitude;
                send.latitude=langtitude;
               
                FirstFragment fragment = (FirstFragment)getSupportFragmentManager().findFragmentById(R.id.firstFragment);
                fragment.mMapController.animateTo(new com.tencent.tencentmap.mapsdk.map.GeoPoint((int)(latitude ), (int)(langtitude )));
                
                if(!isdraw.equals(new com.tencent.tencentmap.mapsdk.map.GeoPoint((int)(latitude ), (int)(langtitude ))))
                {
                Bitmap bmpMarker=null;
        		Resources res=TestFragmentActivity.this.getResources();
        		bmpMarker=BitmapFactory.decodeResource(res, R.drawable.mark_location);
        		
            	SimulateLocationOverlay simuOvelay=new SimulateLocationOverlay(bmpMarker);
            	fragment.mMapView.addOverlay(simuOvelay);
        		
            	com.tencent.tencentmap.mapsdk.map.GeoPoint geoSimulateLocation=new com.tencent.tencentmap.mapsdk.map.GeoPoint((int)(latitude ), (int)(langtitude ));
        		simuOvelay.setGeoCoords(geoSimulateLocation);
        		simuOvelay.setAccuracy(5000);
        		
        		isdraw=geoSimulateLocation;
                }
                handler.postDelayed(this, 5000);
             }
            
       };
       
       handler.postDelayed(r, 5000);
       
        
        Button but1 = (Button)this.findViewById(R.id.imageButton); 
   	 but1.setOnClickListener(new OnClickListener() {
   		 @Override
   		    public void onClick(View v) {
   		        // TODO 隐藏 地图图层
   			// Bundle args = new Bundle();
   			// args.putInt(FirstFragment.ARG_POSITION, position);
   			// FirstFragment.setArguments(args);


   			FirstFragment firstFragment = new FirstFragment();
   			 FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); 
   			// ft.replace(R.id.firstFragment,secondFragment );
   			 ft.addToBackStack(null);
   			 ft.attach(firstFragment);
   			 ft.commit(); 
   			 Log.d("按钮", StreetViewShow.getInstance().getStreetStatus().toString());
   			 //测试全景视图
   			center=new GeoPoint((int)(30.519933 * 1E6), (int)(114.397045 * 1E6));
   			//StreetViewShow.getInstance().getStreetStatus().latitudeE6=((int)(30.519933 * 1E6));
   			//StreetViewShow.getInstance().getStreetStatus().longitudeE6=((int)(114.397045 * 1E6));
   		 StreetViewShow.getInstance().showStreetView(TestFragmentActivity.this, center, 100, TestFragmentActivity.this, -170, 0);
   		
   		 //mContainer.removeView(v);	
   		//改变当前视图位置
   		mStreetView2 = v;
   		mContainer.removeAllViews();
        mContainer.addView(mStreetView2);
   			onLoaded();
   			//mStreetView.setEnabled(false);
   			
   		    }
   		//播放声音事件
   		});
    }    
        
    void addShowHideListener(int buttonId, final Fragment fragment) {    
        final Button button = (Button)findViewById(buttonId);    
        button.setOnClickListener(new OnClickListener() {    
            public void onClick(View v) {    
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();    
                //为Fragment设置淡入淡出效果    
                ft.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);    
                            
                if (fragment.isHidden()) {    
                    ft.show(fragment);    	
                    button.setText("隐藏");    
                } else {    
                    ft.hide(fragment);    
                    button.setText("显示");    
                }    
                ft.commit();    
            }    
        });    
    }      
    
    
    @Override
    protected void onDestroy() {
    	StreetViewShow.getInstance().destory();
    	handler.removeCallbacks(r);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
    	/*DisplayMetrics metrics = new DisplayMetrics();  //获得屏幕大小
    	
    	LayoutInflater inflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View view = inflater.inflate(R.layout.streetswitchicon, null);
    	
    	LayoutParams lp = new LinearLayout.LayoutParams(  
                (int) (80),  
                LinearLayout.LayoutParams.WRAP_CONTENT);
    	 LinearLayout ll = (LinearLayout)findViewById(R.id.layout2);  
    	// ll.setLayoutParams(lp);
    	 ll. addView (view);  
    	 */
    	/* 
    	LayoutInflater inflater1=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View view = inflater1.inflate(R.layout.streetswitchicon, null);
    	LinearLayout ll = (LinearLayout)findViewById(R.id.layout2);    //得到布局
    	 //响应BUTTON
     LayoutInflater inflate2r=(LayoutInflater)this.getSystemService(streetswitch.LAYOUT_INFLATER_SERVICE);
    	 Button but1 = (Button)view.findViewById(R.id.imageButton); 
    	 but1.setOnClickListener(new OnClickListener() {
    		 @Override
    		    public void onClick(View v) {
    		        // TODO 隐藏 地图图层
    			// Bundle args = new Bundle();
    			// args.putInt(FirstFragment.ARG_POSITION, position);
    			// FirstFragment.setArguments(args);


    			 SecondFragment secondFragment = new SecondFragment();
    			 FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); 
    			// ft.replace(R.id.firstFragment,secondFragment );
    			 ft.addToBackStack(null);
    			 
    			 ft.attach(secondFragment);
    			 ft.commit();

    			 
    			 Log.d("按钮", StreetViewShow.getInstance().getStreetStatus().toString());
    		    }
    		//播放声音事件
    		});
    		
    	 ll. addView (view);
    	 
    	 */
        super.onResume();
        
        
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onViewReturn(final View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	
            	mStreetView = v;
                mContainer.addView(mStreetView);
                
                Log.d("street", StreetViewShow.getInstance().getStreetStatus().toString());
                
            }
        });
    }

    public void onNetError() {
        // 网络错误处理
    }

    public void onDataError() {
        // 解析数据错误处理
    	 Log.d("street", "此处没有全景");
    }

    CustomerOverlay overlay;
    
    public ItemizedOverlay getOverlay() {
        if (overlay == null) {
            ArrayList<CustomPoiData> pois = new ArrayList<CustomPoiData>();
            pois.add(new CustomPoiData(39984066, 116307968, getBm(R.drawable.poi_center),
                    getBm(R.drawable.poi_center_pressed), 0));
            pois.add(new CustomPoiData(39984166, 11630800, getBm(R.drawable.pin_green),
                    getBm(R.drawable.pin_green_pressed), 40));
            pois.add(new CustomPoiData(39984000, 116307968, getBm(R.drawable.pin_yellow),
                    getBm(R.drawable.pin_yellow_pressed), 80));
            pois.add(new CustomPoiData(39984066, 116308088, getBm(R.drawable.pin_red),
                    getBm(R.drawable.pin_red_pressed), 120));
            overlay = new CustomerOverlay(pois);
            overlay.populate();
        }
        return overlay;
    }

    private Bitmap getBm(int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inScaled = false;

        return BitmapFactory.decodeResource(getResources(), resId, options);
    }

	public void onLoaded() {
		runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	mStreetView.setVisibility(View.VISIBLE);
      
            }
        });
	}

    public void onAuthFail() {
        // 验证失败
    }
    
    
    
  //当前坐标点类
  	class SimulateLocationOverlay extends Overlay {
  		
  		com.tencent.tencentmap.mapsdk.map.GeoPoint geoPoint;
  		Bitmap bmpMarker;
  		float fAccuracy=0f;
  		

  		public SimulateLocationOverlay(Bitmap mMarker) {
  		    bmpMarker = mMarker;
  		}
  		
  		public void setGeoCoords(com.tencent.tencentmap.mapsdk.map.GeoPoint geoSimulateLoc)
  		{
  			if(geoPoint==null)
  			{
  				geoPoint=new com.tencent.tencentmap.mapsdk.map.GeoPoint(geoSimulateLoc.getLatitudeE6(),geoSimulateLoc.getLongitudeE6());
  			}
  			else
  			{
  				geoPoint.setLatitudeE6(geoSimulateLoc.getLatitudeE6());
  				geoPoint.setLongitudeE6(geoSimulateLoc.getLongitudeE6());
  			}
  		}
  		
  		public void setAccuracy(float fAccur)
  		{
  			fAccuracy=fAccur;
  		}

  		@Override
  		public void draw(Canvas canvas, MapView mapView) {
  			if(geoPoint==null)
  			{
  				return;
  			}
  			Projection mapProjection = mapView.getProjection();
  			Paint paint = new Paint();
  			Point ptMap = mapProjection.toPixels(geoPoint, null);
  			paint.setColor(Color.BLUE);
  			paint.setAlpha(8);
  			paint.setAntiAlias(true);

  			//float fRadius=mapProjection.metersToEquatorPixels(fAccuracy);
  			//canvas.drawCircle(ptMap.x, ptMap.y, fRadius, paint);
  			//paint.setStyle(Style.STROKE);
  			//paint.setAlpha(200);
  			//canvas.drawCircle(ptMap.x, ptMap.y, fRadius, paint);

  			if(bmpMarker!=null)
  			{
  				paint.setAlpha(255);
  				canvas.drawBitmap(bmpMarker, ptMap.x - bmpMarker.getWidth() / 2, ptMap.y
  						- bmpMarker.getHeight() / 2, paint);
  			}
  			
  			super.draw(canvas, mapView);
  		}
  	
  		
  		
  	}
    
   
}   