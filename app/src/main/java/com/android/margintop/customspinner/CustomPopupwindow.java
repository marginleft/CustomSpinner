package com.android.margintop.customspinner;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by L on 2017/3/22.
 *
 * @描述 ${TODO}
 */

public class CustomPopupwindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private static int sPpNum;        // 使当前显示只有一个popupwindow
    private List<String> mDataList = new ArrayList<>();
    private ListView mLvResource;
    private FrameLayout mFlResource;
    private CustomSpinner mCustomSpinner;
    private CustomSpinner.OnSpinnerListener mOnSpinnerListener;
    private String mCurrentItem;
    private String mPreItem;
    private ObjectAnimator mOpenAnimator;
    private ObjectAnimator mCloseAnimator;

    public CustomPopupwindow(Context context, int width, int height, CustomSpinner customSpinner, CustomSpinner.OnSpinnerListener onSpinnerListener) {
        super(width, height);
        mCustomSpinner = customSpinner;
        mOnSpinnerListener = onSpinnerListener;
        this.setTouchable(true);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        View view = LayoutInflater.from(context).inflate(R.layout.view_popup_window, null);
        mLvResource = (ListView) view.findViewById(R.id.lv_resource);
        mFlResource = (FrameLayout) view.findViewById(R.id.fl_resource);
        this.setContentView(view);

        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                closeAnimate();
            }
        });

        mLvResource.setAdapter(new FinalAdapter<String>(R.layout.view_spinner_item, new FinalAdapter.FinalAdapterListener() {
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
        }));

        mLvResource.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.isShowing()) {
            this.dismiss();
        }
        mCurrentItem = mDataList.get(position);
        showSelected();
    }

    private void showSelected() {
        mCustomSpinner.getTvSelected().setText(mCurrentItem);
        if (mOnSpinnerListener != null) {
            mOnSpinnerListener.onSpinnerItemSelected(mCurrentItem);
            if (mPreItem != null && !TextUtils.equals(mPreItem, mCurrentItem)) {
                mOnSpinnerListener.onSpinnerItemChanged(mCurrentItem);
            }
        }
        mPreItem = mCurrentItem;
    }

    public void showItemByAnimate() {
        if (!this.isShowing()) {
            doAnimate(true);
            sPpNum++;
            if (isAboveAnchor()) {
                mFlResource.setBackgroundResource(R.drawable.solid_blue_stroke_no_bottom);
            } else {
                mFlResource.setBackgroundResource(R.drawable.solid_blue_stroke_no_top);
            }
            mCustomSpinner.getRlSpinner().setBackgroundResource(R.drawable.solid_blue_stroke_blue);
        }
    }

    private void closeAnimate() {
        doAnimate(false);
        sPpNum--;
        mCustomSpinner.getRlSpinner().setBackgroundResource(R.drawable.solid_tran_stroke_gray);
    }

    private void doAnimate(boolean isOpenAnimate) {
        if (isOpenAnimate) {
            this.showAsDropDown(mCustomSpinner);
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

    public void updatePpData() {
        if (mLvResource != null) {
            ((FinalAdapter) mLvResource.getAdapter()).setItems(mDataList);
        }
    }

    public void setResource(List<String> dataList, String defaultStr) {
        mDataList.clear();
        mDataList.addAll(dataList);
        if (mDataList != null && !mDataList.isEmpty()) {
            if (defaultStr == null) {
                mCurrentItem = mDataList.get(0);
            } else {
                mCurrentItem = defaultStr;
            }
            showSelected();
        } else {
            throw new RuntimeException("the list is empty.-by margintop");
        }
    }

    public int getItemSize() {
        return mDataList.size();
    }

    public int getPpNum() {
        return sPpNum;
    }

    public void setPpNum(int ppNum) {
        sPpNum = ppNum;
    }

}
