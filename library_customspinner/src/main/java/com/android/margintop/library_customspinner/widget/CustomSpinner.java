package com.android.margintop.library_customspinner.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.margintop.library_customspinner.utils.DensityUtils;
import com.android.margintop.library_customspinner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by L on 2017/3/11.
 *
 * @描述 类似Spinner样式的选择框。
 */

public class CustomSpinner extends FrameLayout implements View.OnClickListener {

    private List<String> mDataList = new ArrayList<>();
    private TextView mTvSelected;
    private ImageView mIvIndicate;
    private int mTextSize;
    private int mLeftMargin;
    private int mRightMargin;
    private int mItemHeight;
    private int mTextColor;
    private RelativeLayout mRlSpinner;
    private CustomPopupwindow mCustomPopupwindow;
    private OnSpinnerListener mOnSpinnerListener;
    private boolean mIsUpdated;
    private String mCurrentItem;
    private String mPreItem;

    public CustomSpinner(Context context) {
        this(context, null);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_custom_spinner, this, true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner);
        mTextSize = a.getDimensionPixelSize(R.styleable.CustomSpinner_cs_textSize, DensityUtils.dp2px(getContext(), 14));
        mLeftMargin = a.getDimensionPixelSize(R.styleable.CustomSpinner_cs_leftMargin, DensityUtils.dp2px(getContext(), 10));
        mRightMargin = a.getDimensionPixelSize(R.styleable.CustomSpinner_cs_rightMargin, DensityUtils.dp2px(getContext(), 10));
        mItemHeight = a.getDimensionPixelSize(R.styleable.CustomSpinner_cs_itemHeight, DensityUtils.dp2px(getContext(), 34));
        mTextColor = a.getColor(R.styleable.CustomSpinner_cs_textColor, Color.parseColor("#212121"));
        a.recycle();
        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCustomPopupwindow != null) {
            mCustomPopupwindow.setPpNum(0);
        }
    }

    private void initView() {
        mRlSpinner = (RelativeLayout) findViewById(R.id.rl_spinner);
        mTvSelected = (TextView) findViewById(R.id.tv_selected);
        mIvIndicate = (ImageView) findViewById(R.id.iv_indicate);

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mTvSelected.getLayoutParams();
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mIvIndicate.getLayoutParams();
        params1.leftMargin = mLeftMargin;
        params2.rightMargin = mRightMargin;

        mRlSpinner.setBackgroundResource(R.drawable.solid_tran_stroke_gray);
        mTvSelected.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mTvSelected.setTextColor(mTextColor);

        this.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mCustomPopupwindow == null) {
            int width = getWidth();
            mCustomPopupwindow = new CustomPopupwindow(getContext(), width, 0, this);
        }
        if (mCustomPopupwindow.getPpNum() == 0) {
            mCustomPopupwindow.setResource(mDataList, mIsUpdated);
            mIsUpdated = false;
        }
    }

    /**
     * 输入list数据，空list报运行时错误。
     * @param dataList
     * @param defaultStr null则默认选中第一个条目数据。
     */
    public void setResource(List<String> dataList, String defaultStr) {
        mIsUpdated = true;
        mDataList.clear();
        mDataList.addAll(dataList);
        if (mDataList != null && !mDataList.isEmpty()) {
            showResultText(defaultStr);
        } else {
            throw new RuntimeException("the list is empty.-by margintop");
        }
    }

    /**
     * 设置文本显示的内容，默认数据如果不在输入list数据中，那么报运行时错误。
     * @param defaultStr
     */
    private void showResultText(String defaultStr) {
        if (defaultStr == null) {
            mCurrentItem = mDataList.get(0);
        } else {
            if (mDataList.contains(defaultStr)) {
                mCurrentItem = defaultStr;
            } else {
                throw new RuntimeException("the list has no the default.-by margintop");
            }
        }
        showSelected();
    }

    private void showSelected() {
        mTvSelected.setText(mCurrentItem);
        if (mOnSpinnerListener != null) {
            mOnSpinnerListener.onSpinnerItemSelected(mCurrentItem, this);
            if (mPreItem != null && !TextUtils.equals(mPreItem, mCurrentItem)) {
                mOnSpinnerListener.onSpinnerItemChanged(mCurrentItem, this);
            }
        }
        mPreItem = mCurrentItem;
    }

    public void updateBackgroundResource(int resId) {
        mRlSpinner.setBackgroundResource(resId);
    }

    public interface OnSpinnerListener {
        void onSpinnerItemSelected(String selected, View view);

        void onSpinnerItemChanged(String changed, View view);
    }

    public void setOnSpinnerListener(OnSpinnerListener onSpinnerListener) {
        mOnSpinnerListener = onSpinnerListener;
    }

    public ImageView getIvIndicate() {
        return mIvIndicate;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public int getLeftMargin() {
        return mLeftMargin;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getItemHeight() {
        return mItemHeight;
    }

    public void setCurrentItem(String currentItem) {
        mCurrentItem = currentItem;
        showSelected();
    }
}
