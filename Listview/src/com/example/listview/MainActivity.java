package com.example.listview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

public class MainActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String[] g_titles = {"犬","猫","ぬ"};
		String[][][] c_titles={{{"ドッグ","犬です。"},{"キャット","かわいい"},{"ぬー","ぬーーー"}},
				{{"テスト犬","テストー"},{"テスト猫","テストさん"},{"ぬーテスト","ぬー"}},
				{{"テスト犬","テストー"},{"テスト猫","テストさん"},{"ぬーテスト","ぬー"}}};
		
		ExpandableListView elv = (ExpandableListView)findViewById(R.id.elv);
		ArrayList<Map<String,String>> g_list = new ArrayList<Map<String,String>>();
		ArrayList<List<Map<String,String>>> c_list = new ArrayList<List<Map<String,String>>>();
		
		for(int i=0; i< g_titles.length; i++){
			HashMap<String, String> group = new HashMap<String, String>();
			group.put("group_title",g_titles[i]);
			
			g_list.add(group);
			ArrayList<Map<String,String>> childs = new ArrayList<Map<String,String>>();
			
			for(int j= 0; j<c_titles.length; j++){
				HashMap<String,String> child = new HashMap<String,String>();
				child.put("child_title", c_titles[i][j][0]);
				child.put("child_text", c_titles[i][j][1]);
				childs.add(child);
			}
			c_list.add(childs);
		}
		
		SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
				this,
				g_list,android.R.layout.simple_expandable_list_item_1,
				new String []{"group_title"},
				new int []{android.R.id.text1}, 
				c_list, android.R.layout.simple_expandable_list_item_2,
				new String []{"child_title","child_text"},
				new int []{android.R.id.text1, android.R.id.text2}
		);
		elv.setAdapter(adapter);
		
		elv.setOnChildClickListener(new OnChildClickListener(){
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id){
				TextView txt = (TextView)((TwoLineListItem)v).findViewById(android.R.id.text1);
				Toast.makeText(MainActivity.this, txt.getText(), Toast.LENGTH_LONG).show();
				return false;
			}
		});
		
	}
}