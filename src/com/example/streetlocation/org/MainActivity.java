package com.example.streetlocation.org;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


public class MainActivity extends TabActivity {
	TabHost tabHost;
/** Called when the activity is first created. */


public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	tabHost = getTabHost();
	setTabs();
}
private void setTabs()
{
	addTab("��ͼ", R.drawable.tab_home, HomeActivity.class);
	addTab("ȫ��", R.drawable.tab_search, TestFragmentActivity.class);
	addTab("����", R.drawable.tab_search, SearchActivity.class);
	addTab("����", R.drawable.tab_home, RouteSearch.class);
	addTab("����", R.drawable.tab_search, SearchActivity.class);
}
private void addTab(String labelId, int drawableId, Class<?> c)
{
	Intent intent = new Intent(this, c);
	TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
	
	View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
	TextView title = (TextView) tabIndicator.findViewById(R.id.title);
	title.setText(labelId);
	ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
	icon.setImageResource(drawableId);		
	spec.setIndicator(tabIndicator);
	spec.setContent(intent);
	tabHost.addTab(spec);
}
public void openCameraActivity(View b)
{
	Intent intent = new Intent(this, CameraActivity.class);
	startActivity(intent);
}

}
