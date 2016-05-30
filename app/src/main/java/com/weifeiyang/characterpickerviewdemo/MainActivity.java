package com.weifeiyang.characterpickerviewdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.fly.widget.pickerview.CharacterPickerWindow;
import com.fly.widget.pickerview.LocationInfo;
import com.fly.widget.pickerview.OptionsWindowHelper;

public class MainActivity extends AppCompatActivity {

    private CharacterPickerWindow window;//地址控件
    private Button btn;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClick();
            }
        });
    }

    private void btnClick() {
        OptionsWindowHelper.setStartTime("");
        OptionsWindowHelper.setEndTime("");
        window = OptionsWindowHelper.builder(mContext, "", "time", new OptionsWindowHelper.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(LocationInfo year, LocationInfo month, LocationInfo day) {
                //showTime(year, month, day);
            }
        });
        window.showAtLocation(findViewById(R.id.rl_layout), Gravity.BOTTOM, 0, 0);
        window.setOutsideTouchable(true);
    }
}
