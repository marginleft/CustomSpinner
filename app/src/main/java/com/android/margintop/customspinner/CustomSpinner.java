package com.android.margintop.customspinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by L on 2017/3/11.
 *
 * @描述 类似Spinner样式的选择框。
 */

public class CustomSpinner extends FrameLayout implements View.OnClickListener {

    private final int ITEMNUM = 3;     // 显示几个条目
    private List<String> mDataList = new ArrayList<>();
    private TextView mTvSelected;
    private ImageView mIvIndicate;
    private int mWidth;
    private int mTextSize;
    private int mLeftMargin;
    private int mRightMargin;
    private int mItemHeight;
    private int mTextColor;
    private RelativeLayout mRlSpinner;
    private CustomPopupwindow mCustomPopupwindow;
    private OnSpinnerListener mOnSpinnerListener;
    private String mDefaultStr;
    private boolean mIsUpdated;

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
            mWidth = getWidth();
            mCustomPopupwindow = new CustomPopupwindow(getContext(), mWidth, 0, this, mOnSpinnerListener);
        }
        if (mCustomPopupwindow.getPpNum() == 0) {
            if (mIsUpdated) {
                mIsUpdated = false;
                mCustomPopupwindow.setResource(mDataList, mDefaultStr);
            }
            if (mCustomPopupwindow.getItemSize() < ITEMNUM) {
                mCustomPopupwindow.setHeight(mItemHeight * mCustomPopupwindow.getItemSize());
            } else {
                mCustomPopupwindow.setHeight(mItemHeight * ITEMNUM);
            }
            mCustomPopupwindow.updatePpData();
            mCustomPopupwindow.showItemByAnimate();
        }
    }

    public void setResource(List<String> dataList, String defaultStr) {
        mIsUpdated = true;
        mDataList.clear();
        mDataList.addAll(dataList);
        mDefaultStr = defaultStr;
    }

    public interface OnSpinnerListener {
        void onSpinnerItemSelected(String selected);

        void onSpinnerItemChanged(String changed);
    }

    public void setOnSpinnerListener(OnSpinnerListener onSpinnerListener) {
        mOnSpinnerListener = onSpinnerListener;
    }

    public TextView getTvSelected() {
        return mTvSelected;
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

    public RelativeLayout getRlSpinner() {
        return mRlSpinner;
    }

    public int getItemHeight() {
        return mItemHeight;
    }
}
