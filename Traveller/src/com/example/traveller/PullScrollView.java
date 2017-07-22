package com.example.traveller;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;


/**
 * 鑷畾涔塖crollView
 *
 * @author markmjw
 * @date 2013-09-13
 */
public class PullScrollView extends ScrollView {
    /** 闃诲凹绯绘暟,瓒婂皬闃诲姏灏辫秺澶�. */
    private static final float SCROLL_RATIO = 0.5f;

    /** 婊戝姩鑷崇炕杞殑璺濈. */
    private static final int TURN_DISTANCE = 100;

    /** 澶撮儴view. */
    private View mHeader;

    /** 澶撮儴view楂樺害. */
    private int mHeaderHeight;

    /** 澶撮儴view鏄剧ず楂樺害. */
    private int mHeaderVisibleHeight;

    /** ScrollView鐨刢ontent view. */
    private View mContentView;

    /** ScrollView鐨刢ontent view鐭╁舰. */
    private Rect mContentRect = new Rect();

    /** 棣栨鐐瑰嚮鐨刌鍧愭爣. */
    private float mTouchDownY;

    /** 鏄惁鍏抽棴ScrollView鐨勬粦鍔�. */
    private boolean mEnableTouch = false;

    /** 鏄惁寮�濮嬬Щ鍔�. */
    private boolean isMoving = false;

    /** 鏄惁绉诲姩鍒伴《閮ㄤ綅缃�. */
    private boolean isTop = false;

    /** 澶撮儴鍥剧墖鍒濆椤堕儴鍜屽簳閮�. */
    private int mInitTop, mInitBottom;

    /** 澶撮儴鍥剧墖鎷栧姩鏃堕《閮ㄥ拰搴曢儴. */
    private int mCurrentTop, mCurrentBottom;

    /** 鐘舵�佸彉鍖栨椂鐨勭洃鍚櫒. */
    private OnTurnListener mOnTurnListener;

    private enum State {
        /**椤堕儴*/
        UP,
        /**搴曢儴*/
        DOWN,
        /**姝ｅ父*/
        NORMAL
    }

    /** 鐘舵��. */
    private State mState = State.NORMAL;

    public PullScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD) private void init(Context context, AttributeSet attrs) {
        // set scroll mode
        setOverScrollMode(OVER_SCROLL_NEVER);

        if (null != attrs) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PullScrollView);

            if (ta != null) {
                mHeaderHeight = (int) ta.getDimension(R.styleable.PullScrollView_headerHeight, -1);
                mHeaderVisibleHeight = (int) ta.getDimension(R.styleable
                        .PullScrollView_headerVisibleHeight, -1);
                ta.recycle();
            }
        }
    }

    /**
     * 璁剧疆Header
     *
     * @param view
     */
    public void setHeader(View view) {
        mHeader = view;
    }

    /**
     * 璁剧疆鐘舵�佹敼鍙樻椂鐨勭洃鍚櫒
     *
     * @param turnListener
     */
    public void setOnTurnListener(OnTurnListener turnListener) {
        mOnTurnListener = turnListener;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mContentView = getChildAt(0);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (getScrollY() == 0) {
            isTop = true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchDownY = ev.getY();
            mCurrentTop = mInitTop = mHeader.getTop();
            mCurrentBottom = mInitBottom = mHeader.getBottom();
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mContentView != null) {
            doTouchEvent(ev);
        }

        // 绂佹鎺т欢鏈韩鐨勬粦鍔�.
        return mEnableTouch || super.onTouchEvent(ev);
    }

    /**
     * 瑙︽懜浜嬩欢澶勭悊
     *
     * @param event
     */
    private void doTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                doActionMove(event);
                break;

            case MotionEvent.ACTION_UP:
                // 鍥炴粴鍔ㄧ敾
                if (isNeedAnimation()) {
                    rollBackAnimation();
                }

                if (getScrollY() == 0) {
                    mState = State.NORMAL;
                }

                isMoving = false;
                mEnableTouch = false;
                break;

            default:
                break;
        }
    }

    /**
     * 鎵ц绉诲姩鍔ㄧ敾
     *
     * @param event
     */
    private void doActionMove(MotionEvent event) {
        // 褰撴粴鍔ㄥ埌椤堕儴鏃讹紝灏嗙姸鎬佽缃负姝ｅ父锛岄伩鍏嶅厛鍚戜笂鎷栧姩鍐嶅悜涓嬫嫋鍔ㄥ埌椤剁鍚庨娆¤Е鎽镐笉鍝嶅簲鐨勯棶棰�
        if (getScrollY() == 0) {
            mState = State.NORMAL;

            // 婊戝姩缁忚繃椤堕儴鍒濆浣嶇疆鏃讹紝淇Touch down鐨勫潗鏍囦负褰撳墠Touch鐐圭殑鍧愭爣
            if (isTop) {
                isTop = false;
                mTouchDownY = event.getY();
            }
        }

        float deltaY = event.getY() - mTouchDownY;

        // 瀵逛簬棣栨Touch鎿嶄綔瑕佸垽鏂柟浣嶏細UP OR DOWN
        if (deltaY < 0 && mState == State.NORMAL) {
            mState = State.UP;
        } else if (deltaY > 0 && mState == State.NORMAL) {
            mState = State.DOWN;
        }

        if (mState == State.UP) {
            deltaY = deltaY < 0 ? deltaY : 0;

            isMoving = false;
            mEnableTouch = false;

        } else if (mState == State.DOWN) {
            if (getScrollY() <= deltaY) {
                mEnableTouch = true;
                isMoving = true;
            }
            deltaY = deltaY < 0 ? 0 : deltaY;
        }

        if (isMoving) {
            // 鍒濆鍖朿ontent view鐭╁舰
            if (mContentRect.isEmpty()) {
                // 淇濆瓨姝ｅ父鐨勫竷灞�浣嶇疆
                mContentRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(),
                        mContentView.getBottom());
            }

            // 璁＄畻header绉诲姩璺濈(鎵嬪娍绉诲姩鐨勮窛绂�*闃诲凹绯绘暟*0.5)
            float headerMoveHeight = deltaY * 0.5f * SCROLL_RATIO;
            mCurrentTop = (int) (mInitTop + headerMoveHeight);
            mCurrentBottom = (int) (mInitBottom + headerMoveHeight);

            // 璁＄畻content绉诲姩璺濈(鎵嬪娍绉诲姩鐨勮窛绂�*闃诲凹绯绘暟)
            float contentMoveHeight = deltaY * SCROLL_RATIO;

            // 淇content绉诲姩鐨勮窛绂伙紝閬垮厤瓒呰繃header鐨勫簳杈圭紭
            int headerBottom = mCurrentBottom - mHeaderVisibleHeight;
            int top = (int) (mContentRect.top + contentMoveHeight);
            int bottom = (int) (mContentRect.bottom + contentMoveHeight);

            if (top <= headerBottom) {
                // 绉诲姩content view
                mContentView.layout(mContentRect.left, top, mContentRect.right, bottom);

                // 绉诲姩header view
                mHeader.layout(mHeader.getLeft(), mCurrentTop, mHeader.getRight(), mCurrentBottom);
            }
        }
    }

    private void rollBackAnimation() {
        TranslateAnimation tranAnim = new TranslateAnimation(0, 0,
                Math.abs(mInitTop - mCurrentTop), 0);
        tranAnim.setDuration(200);
        mHeader.startAnimation(tranAnim);

        mHeader.layout(mHeader.getLeft(), mInitTop, mHeader.getRight(), mInitBottom);

        // 寮�鍚Щ鍔ㄥ姩鐢�
        TranslateAnimation innerAnim = new TranslateAnimation(0, 0, mContentView.getTop(), mContentRect.top);
        innerAnim.setDuration(200);
        mContentView.startAnimation(innerAnim);
        mContentView.layout(mContentRect.left, mContentRect.top, mContentRect.right, mContentRect.bottom);

        mContentRect.setEmpty();

        // 鍥炶皟鐩戝惉鍣�
        if (mCurrentTop > mInitTop + TURN_DISTANCE && mOnTurnListener != null){
            mOnTurnListener.onTurn();
        }
    }

    /**
     * 鏄惁闇�瑕佸紑鍚姩鐢�
     */
    private boolean isNeedAnimation() {
        return !mContentRect.isEmpty() && isMoving;
    }

    /**
     * 缈昏浆浜嬩欢鐩戝惉鍣�
     *
     * @author markmjw
     */
    public interface OnTurnListener {
        /**
         * 缈昏浆鍥炶皟鏂规硶
         */
        public void onTurn();
    }
}
