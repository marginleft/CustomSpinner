package com.android.margintop.customspinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.margintop.library_customspinner.widget.CustomSpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CustomSpinner.OnSpinnerListener {

    private CustomSpinner mCsSpinner1;
    private CustomSpinner mCsSpinner2;
    private List<String> mStringList1 = new ArrayList<>();
    private List<String> mStringList2 = new ArrayList<>();
    private List<String> mStringList3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        mCsSpinner1 = (CustomSpinner) findViewById(R.id.cs_spinner1);
        mCsSpinner2 = (CustomSpinner) findViewById(R.id.cs_spinner2);

        mCsSpinner1.setOnSpinnerListener(this);
        mCsSpinner2.setOnSpinnerListener(this);
    }


    private void initData() {
        for (int i = 0; i < 7; i++) {
            mStringList1.add("条目" + i);
        }
        for (int i = 0; i < 2; i++) {
            mStringList2.add("条目" + i);
        }
        for (int i = 0; i < 1; i++) {
            mStringList3.add("条目" + i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                mCsSpinner1.setResource(mStringList1, null);
                mCsSpinner2.setResource(mStringList1, null);
                break;
            case R.id.btn_2:
                mCsSpinner1.setResource(mStringList2, null);
                mCsSpinner2.setResource(mStringList2, null);
                break;
            case R.id.btn_3:
                mCsSpinner1.setResource(mStringList3, null);
                mCsSpinner2.setResource(mStringList3, null);
                break;
        }
    }

    @Override
    public void onSpinnerItemSelected(String selected, View view) {
        switch (view.getId()) {
            case R.id.cs_spinner1:
                Toast.makeText(MainActivity.this, "选择了：" + selected, Toast.LENGTH_SHORT).show();
                System.out.println("选择了：" + selected);
                break;
            case R.id.cs_spinner2:
                break;
            default:
                break;
        }
    }

    @Override
    public void onSpinnerItemChanged(String changed, View view) {
        switch (view.getId()) {
            case R.id.cs_spinner1:
                Toast.makeText(MainActivity.this, "改成了：" + changed, Toast.LENGTH_SHORT).show();
                System.out.println("改成了：" + changed);
                break;
            case R.id.cs_spinner2:
                break;
            default:
                break;
        }
    }
}
