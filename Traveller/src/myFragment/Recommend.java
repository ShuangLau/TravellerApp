package myFragment;

import java.util.ArrayList;
import java.util.List;

import myActivity.ScenryDetail;

import adapterHdy.ScenryAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveller.R;
import com.example.traveller.Scenery;
import com.example.traveller.ScenryDownload;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)public class Recommend extends Fragment
					implements OnItemClickListener,OnItemLongClickListener{

	private View view = null;
	private ListView listView;
	private List<Scenery> myScenry = new ArrayList<Scenery>();
	private WebView webview;
	private Button back;
	int click = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fg1_content, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        webview = (WebView)view.findViewById(R.id.fg1_web);
        
        View header = inflater.inflate(R.layout.list_item_header_footer, null);
        View footer = inflater.inflate(R.layout.list_item_header_footer, null);
        back = (Button)view.findViewById(R.id.back);
        back.setBackgroundColor(Color.WHITE);
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) { 
				disappear_web();
				//webview.destroy();
				back.setVisibility(View.GONE);
			}
		});
        
        
        TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
        TextView txtFooterTitle =  (TextView) footer.findViewById(R.id.txt_title);
        txtHeaderTitle.setText("精彩游记和专题");
        txtFooterTitle.setText("爱旅游, 爱生活");

        listView.addHeaderView(header);
        listView.addFooterView(footer);
        
        // TODO
        
        myScenry.add(new Scenery("TheFirst","http://42.96.208.106/test.jpg","Good!"));
        
        // zai sample_list_item
        final ScenryAdapter adapter = new ScenryAdapter(getActivity(), R.layout.list_item_sample,myScenry);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        
        // 开子线程，下载景点推荐信息
        Log.e("FirstFragment", "StartThread");
        ScenryDownload scenryDownload = new ScenryDownload();
        new Thread(scenryDownload).start();
        
        return view;
	}
	
	void show_web(String url)
	{
		back.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		webview.setVisibility(View.VISIBLE);
		WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        webview.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);  //加载新的url
                return true;    //返回true,代表事件已处理,事件流到此终止
            }
        });
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
                        webview.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
		webview.loadUrl(url);
		
	}
	void disappear_web()
	{
		webview.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
	}
	
	
	
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        
    	if(position!=0)
    	{
    		//TODO
	    	Toast.makeText(getActivity(), "WebView" + position, Toast.LENGTH_SHORT)
			.show();  
    		String url = "http://travel.qunar.com/p-gj300527-ruishi";
    		show_web(url);    		
    	}

    }

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,
			long id) {
		shareData();


		return true;
	}
	


	// 分享内容
	private void shareData () {
	    Intent mSharingIntent = new Intent(Intent.ACTION_SEND);
	    String mShareBody = new StringBuilder()
	            .append("My pedometer data\n")
	            .append("Steps: ").append("10000").append("\n")
	            .append("Calories: ").append("99999").append(" kJ\n")
	            .append("Distance: ").append("10").append(" km\n")
	            .append("Current Speed: ").append("10").append(" km/h\n")
	            .append("Pace: ").append("3").append(" steps/min\n")
	            .toString();
	    mSharingIntent.setType("text/plain");
	    mSharingIntent.putExtra(
	            android.content.Intent.EXTRA_SUBJECT, "Pedometer");
	    mSharingIntent.putExtra(
	            android.content.Intent.EXTRA_TEXT, mShareBody);
	    startActivity(Intent.createChooser(mSharingIntent, "Share via"));
	    
	    
	    
	}
	
    private void share(String content, Uri uri){
    	Intent shareIntent = new Intent(Intent.ACTION_SEND); 
    	if(uri!=null){
    		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
    		shareIntent.setType("image/*"); 
    		//当用户选择短信时使用sms_body取得文字
    		shareIntent.putExtra("sms_body", content);
    	}else{
    		shareIntent.setType("text/plain"); 
    	}
    	shareIntent.putExtra(Intent.EXTRA_TEXT, content);
    	//自定义选择框的标题
    	//startActivity(Intent.createChooser(shareIntent, "邀请好友"));
    	//系统默认标题
    	startActivity(shareIntent);
    }

}
