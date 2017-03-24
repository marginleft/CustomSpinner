package com.android.margintop.customspinner;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class FinalAdapter<T> extends BaseAdapter {

    private List<T> mShowItems = new ArrayList<T>();
    private int mLayoutId;

    public FinalAdapter(int layoutId, FinalAdapterListener finalAdapterListener) {
        this.mLayoutId = layoutId;
        this.mFinalAdapterListener = finalAdapterListener;
    }

    public void setItems(List<T> showItems) {
        this.mShowItems = showItems;
        notifyDataSetChanged();
    }

    public void clear() {
        mShowItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mShowItems == null)
            return 0;
        return mShowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mShowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), mLayoutId, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //赋值
        bindView(position, viewHolder);

        return convertView;
    }

    //把绑定的操作让外部处理
    private void bindView(int position, ViewHolder viewHolder) {

        mFinalAdapterListener.bindView(position, viewHolder);
    }

    public interface FinalAdapterListener {
        void bindView(int position, ViewHolder viewHolder);
    }


    private FinalAdapterListener mFinalAdapterListener;


    public static class ViewHolder {
        private View mView;

        public ViewHolder(View view) {
            this.mView = view;
        }

        private SparseArray<View> mSparseArray = new SparseArray<View>();

        public View getViewById(int id) {
            View view = mSparseArray.get(id);
            if (view == null) {
                view = mView.findViewById(id);
                mSparseArray.put(id, view);
            }
            return view;
        }

        public <T> T getView(int id, Class<T> T) {
            T view = (T) getViewById(id);
            return view;
        }
    }
}
