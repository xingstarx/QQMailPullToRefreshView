package com.xingstarx.refreshview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xingstarx.refreshview.view.QQMailRefreshView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mStartView;
    private Button mStopView;
    private QQMailRefreshView mMQailRefreshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartView = (Button) findViewById(R.id.start);
        mStopView = (Button) findViewById(R.id.stop);
        mMQailRefreshView = (QQMailRefreshView) findViewById(R.id.refresh_view);

        mStartView.setOnClickListener(this);
        mStopView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                mMQailRefreshView.start();
                break;
            case R.id.stop:
                mMQailRefreshView.stop();
                break;
        }
    }
}
