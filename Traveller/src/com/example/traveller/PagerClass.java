package com.example.traveller;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PagerClass extends PagerAdapter {
    private ArrayList<View> viewLists;
   // public boolean viewSkip = false;

    public PagerClass() {
    	
    }

    public PagerClass(ArrayList<View> viewLists) {
        super();
        this.viewLists = viewLists;
    }

    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 涓烘瘡椤垫坊鍔犵偣鍑荤洃鍚紝鍒濆鍖栭煶涔愬苟鐐瑰嚮鏃舵挱鏀撅紝骞朵繚璇佹瘡娆＄偣鍑婚兘鍙互閲嶆柊鎾斁
        /*switch (position) {
            case 3:
                viewLists.get(3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("log", "浣犲綋鍓嶉�夋嫨鐨勬槸 dog ");
                        viewSkip = true;
                    }
                });
                break;
            }*/
        container.addView(viewLists.get(position));
        return viewLists.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewLists.get(position));
    }
}
