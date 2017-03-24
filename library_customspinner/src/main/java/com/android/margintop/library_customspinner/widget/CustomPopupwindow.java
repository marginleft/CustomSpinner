package com.android.margintop.library_customspinner.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.margintop.library_customspinner.adapter.FinalAdapter;
import com.android.margintop.library_customspinner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by L on 2017/3/22.
 *
 * @描述 弹出的PopupWindow。
 */

public class CustomPopupwindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private static int sPpNum;        // 使当前显示只有一个popupwindow
    private final int ITEMNUM = 3;     // 显示几个条目
    private List<String> mDataList = new ArrayList<>();
    private ListView mLvResource;
    private FrameLayout mFlResource;
    private CustomSpinner mCustomSpinner;
    private ObjectAnimator mOpenAnimator;
    private ObjectAnimator mCloseAnimator;
    private final FinalAdapter<String> mAdapter;
    private int mScreenHeight;

    public CustomPopupwindow(Context context, int width, int height, CustomSpinner customSpinner) {
        super(width, height);
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        mCustomSpinner = customSpinner;
        this.setTouchable(true);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        View view = LayoutInflater.from(context).inflate(R.layout.view_popup_window, null);
        mLvResource = (ListView) view.findViewById(R.id.lv_resource);
        mFlResource = (FrameLayout) view.findViewById(R.id.fl_resource);
        this.setContentView(view);

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                closeAnimate();
            }
        });

        mAdapter = new FinalAdapter<>(R.layout.view_spinner_item, new FinalAdapter.FinalAdapterListener() {
            @Override
            public void bindView(int position, FinalAdapter.ViewHolder viewHolder) {
                TextView tvResource = (TextView) viewHolder.getViewById(R.id.tv_resource);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tvResource.getLayoutParams();
                params.leftMargin = mCustomSpinner.getLeftMargin();
                params.height = mCustomSpinner.getItemHeight();
                if (mDataList != null) {
                    tvResource.setText(mDataList.get(position));
                    tvResource.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCustomSpinner.getTextSize());
                    tvResource.setTextColor(mCustomSpinner.getTextColor());
                }
            }
        });
        mLvResource.setAdapter(mAdapter);

        mLvResource.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.isShowing()) {
            this.dismiss();
        }
        mCustomSpinner.setCurrentItem(mDataList.get(position));
    }

    /**
     * 显示出来并伴随打开动画。
     */
    private void showItemsByAnimate() {
        if (!this.isShowing()) {
            doAnimate(true);
            sPpNum++;
            mCustomSpinner.updateBackgroundResource(R.drawable.solid_blue_stroke_blue);
        }
    }

    /**
     * 关闭动画。
     */
    private void closeAnimate() {
        doAnimate(false);
        sPpNum--;
        mCustomSpinner.updateBackgroundResource(R.drawable.solid_tran_stroke_gray);
    }

    /**
     * 进行动画，打开或关闭动画。
     * @param isOpenAnimate
     */
    private void doAnimate(boolean isOpenAnimate) {
        if (isOpenAnimate) {
            showItems();
            if (mOpenAnimator == null) {
                mOpenAnimator = ObjectAnimator.ofFloat(mCustomSpinner.getIvIndicate(), "rotation", 0, 180).setDuration(200);
            }
            mOpenAnimator.start();
        } else {
            if (mCloseAnimator == null) {
                mCloseAnimator = ObjectAnimator.ofFloat(mCustomSpinner.getIvIndicate(), "rotation", 180, 360).setDuration(200);
            }
            mCloseAnimator.start();
        }
    }

    /**
     * 空间足够在下面显示，否则在上面显示。
     */
    private void showItems() {
        if (isEnough()) {
            this.showAsDropDown(mCustomSpinner);
            mFlResource.setBackgroundResource(R.drawable.solid_blue_stroke_no_top);
        } else {
            this.showAsDropDown(mCustomSpinner, 0, -(mCustomSpinner.getHeight() + this.getHeight()));
            mFlResource.setBackgroundResource(R.drawable.solid_blue_stroke_no_bottom);
        }
    }

    /**
     * 判断空间是否足够。
     * @return
     */
    private boolean isEnough() {
        int[] point = new int[2];
        mCustomSpinner.getLocationOnScreen(point);
        int toY = point[1] + mCustomSpinner.getHeight() + this.getHeight() + mCustomSpinner.getItemHeight();
        return toY < mScreenHeight;
    }

    /**
     * 输入list数据，无更新数据不再重复设置数据和设置高度。
     * @param dataList
     * @param isUpdated
     */
    public void setResource(List<String> dataList, boolean isUpdated) {
        if (isUpdated) {
            mDataList = dataList;
            mAdapter.setItems(mDataList);
            setPopupWindowHeight();
        }
        showItemsByAnimate();
    }

    /**
     * 根据数据量设置高度。
     */
    private void setPopupWindowHeight() {
        if (mDataList.size() < ITEMNUM) {
            this.setHeight(mCustomSpinner.getItemHeight() * mDataList.size());
        } else {
            this.setHeight(mCustomSpinner.getItemHeight() * ITEMNUM);
        }
    }

    /**
     * 获得当前显示的PopupWindow的数量。
     * @return
     */
    public int getPpNum() {
        return sPpNum;
    }

    /**
     * 设置当前显示的PopupWindow的数量
     * @param ppNum
     */
    public void setPpNum(int ppNum) {
        sPpNum = ppNum;
    }

}
