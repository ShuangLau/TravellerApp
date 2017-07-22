package myActivity;

import com.example.traveller.R;
import com.example.traveller.R.id;
import com.example.traveller.R.layout;
import com.example.traveller.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class My_Personal_Design extends Activity {

	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my__personal__design);
		webView = (WebView)findViewById(R.id.webView1);
		String url = "http://www.weiailx.com/";
		initWebView(url);
	}

	void initWebView(String url)
	{
		WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);    //启用JS脚本
		
        webView.setWebViewClient(new WebViewClient() {

            //当点击链接时,希望覆盖而不是打开新窗口

            @Override

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);  //加载新的url

                return true;    //返回true,代表事件已处理,事件流到此终止

            }
        });
        
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)

        webView.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {

                        webView.goBack();   //后退

                        return true;    //已处理

                    }

                }

                return false;

            }
        });

           
        
		webView.loadUrl(url);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my__personal__design, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.removeAllViews();
		webView.destroy();		
		
	}
	
	

}
