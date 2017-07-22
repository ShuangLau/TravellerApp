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
        settings.setJavaScriptEnabled(true);    //����JS�ű�
		
        webView.setWebViewClient(new WebViewClient() {

            //���������ʱ,ϣ�����Ƕ����Ǵ��´���

            @Override

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);  //�����µ�url

                return true;    //����true,�����¼��Ѵ���,�¼���������ֹ

            }
        });
        
        //������˰�ť,��WebView����һҳ(Ҳ���Ը�дActivity��onKeyDown����)

        webView.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {

                        webView.goBack();   //����

                        return true;    //�Ѵ���

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
