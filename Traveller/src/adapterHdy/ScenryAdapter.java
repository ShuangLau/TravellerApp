package adapterHdy;

import java.util.List;

import myActivity.ScenryDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveller.R;
import com.example.traveller.Scenery;

public class ScenryAdapter extends ArrayAdapter<Scenery> {
    private static final String TAG = "ScenryAdapter";
    private Context myContext;
    static class ViewHolder {
        ImageView sceneryImage;
        TextView comment;
    }

    private final LayoutInflater mLayoutInflater;

    public ScenryAdapter(final Context context, final int layoutResourceId, 
    		final List<Scenery> scenery) {
        super(context, layoutResourceId,scenery);
        myContext = context;
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
    	
    	// 获得当前的scenery信息
    	Scenery scenery = getItem(position);
        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.sceneryImage = (ImageView) convertView.findViewById(R.id.scenImage);
            vh.comment = (TextView) convertView.findViewById(R.id.comment);
            vh.comment.setText("   " +scenery.getComment());
            convertView.setTag(vh);
            vh.sceneryImage.setImageBitmap(scenery.getBitmap());
            vh.sceneryImage.setScaleType(ScaleType.CENTER_INSIDE );
            
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }


        Log.d(TAG, "getView position:" + position + " h:" );

        return convertView;
    }




}

