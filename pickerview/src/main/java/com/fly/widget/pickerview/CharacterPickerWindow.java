package com.fly.widget.pickerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import cn.jeesoft.widget.pickerview.R;


/**
 * @version 0.1 king 2015-11
 */
public class CharacterPickerWindow extends PopupWindow implements View.OnClickListener {
    private View rootView; // 总的布局
    private View btnSubmit, btnCancel;
    private CharacterPickerView pickerView;
    private OnOptionsSelectListener optionsSelectListener;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";

    public CharacterPickerWindow(Context context) {
        super(context);

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#B4212121"));
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.j_picker_dialog, null);
        setContentView(rootView);
        // -----确定和取消按钮
        btnSubmit = rootView.findViewById(R.id.j_btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = rootView.findViewById(R.id.j_btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        childAnmation(context, R.id.ll_window);
        // ----转轮
        pickerView = (CharacterPickerView) rootView.findViewById(R.id.j_optionspicker);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = rootView.findViewById(R.id.ll_window).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    }

    int delay = 0;

    /**
     * popwindow子层的动画
     *
     * @param id
     */
    private void childAnmation(Context mContext, int id) {

        Animation mAnimation = (Animation) AnimationUtils.loadAnimation(
                mContext, R.anim.translate_bottom_menu_in);
        View view = (View) rootView.findViewById(id);
        if (delay == 0) {
            delay = 1;
            view.startAnimation(mAnimation);
        }
        mAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                delay = 0;
            }
        });

    }

    public CharacterPickerView getPickerView() {
        return pickerView;
    }

//    public void setPicker(List<String> optionsItems) {
//        pickerView.setPicker(optionsItems, null, null);
//    }
//
//    public void setPicker(List<String> options1Items,
//                          List<List<String>> options2Items) {
//        pickerView.setPicker(options1Items, options2Items, null);
//    }
//
//    public void setPicker(List<String> options1Items,
//                          List<List<String>> options2Items,
//                          List<List<List<String>>> options3Items) {
//        pickerView.setPicker(options1Items, options2Items, options3Items);
//    }

    /**
     * 设置选中的item位置
     *
     * @param option1
     */
    public void setSelectOptions(int option1) {
        pickerView.setCurrentItems(option1, 0, 0);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1
     * @param option2
     */
    public void setSelectOptions(int option1, int option2) {
        pickerView.setCurrentItems(option1, option2, 0);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1
     * @param option2
     * @param option3
     */
    public void setSelectOptions(int option1, int option2, int option3) {
        pickerView.setCurrentItems(option1, option2, option3);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        pickerView.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {

            dismiss();
            return;
        } else {
            if (optionsSelectListener != null) {
                int[] optionsCurrentItems = pickerView.getCurrentItems();
                optionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2]);
            }
            dismiss();
            return;
        }
    }

    public interface OnOptionsSelectListener {
        public void onOptionsSelect(int options1, int option2, int options3);
    }

    public void setOnoptionsSelectListener(
            OnOptionsSelectListener optionsSelectListener) {
        this.optionsSelectListener = optionsSelectListener;
    }


}
