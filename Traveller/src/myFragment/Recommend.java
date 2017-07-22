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
        txtHeaderTitle.setText("�����μǺ�ר��");
        txtFooterTitle.setText("������, ������");

        listView.addHeaderView(header);
        listView.addFooterView(footer);
        
        // TODO
        
        myScenry.add(new Scenery("TheFirst","http://42.96.208.106/test.jpg","Good!"));
        
        // zai sample_list_item
        final ScenryAdapter adapter = new ScenryAdapter(getActivity(), R.layout.list_item_sample,myScenry);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        
        // �����̣߳����ؾ����Ƽ���Ϣ
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
        settings.setJavaScriptEnabled(true);    //����JS�ű�
        webview.setWebViewClient(new WebViewClient() {
            //���������ʱ,ϣ�����Ƕ����Ǵ��´���
            @Override

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);  //�����µ�url
                return true;    //����true,�����¼��Ѵ���,�¼���������ֹ
            }
        });
        //������˰�ť,��WebView����һҳ(Ҳ���Ը�дActivity��onKeyDown����)
        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
                        webview.goBack();   //����
                        return true;    //�Ѵ���
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
	


	// ��������
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
    		//���û�ѡ�����ʱʹ��sms_bodyȡ������
    		shareIntent.putExtra("sms_body", content);
    	}else{
    		shareIntent.setType("text/plain"); 
    	}
    	shareIntent.putExtra(Intent.EXTRA_TEXT, content);
    	//�Զ���ѡ���ı���
    	//startActivity(Intent.createChooser(shareIntent, "�������"));
    	//ϵͳĬ�ϱ���
    	startActivity(shareIntent);
    }

}
