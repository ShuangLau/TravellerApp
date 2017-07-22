package mybroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotifyBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String msg = intent.getExtras().get("acname").toString();
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		
	}

}
