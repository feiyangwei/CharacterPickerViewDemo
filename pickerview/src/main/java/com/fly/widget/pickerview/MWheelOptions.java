package com.fly.widget.pickerview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import cn.jeesoft.widget.pickerview.R;


/**
 * @version 0.1 king 2015-11
 */
final class MWheelOptions {
    private CharacterPickerView view;
    private LoopView wv_option1;
    private LoopView wv_option2;
    private LoopView wv_option3;

    private CharacterPickerView.OnOptionChangedListener mOnOptionChangedListener;


    ArrayList<LocationInfo> mOptions1Items = null;
    ArrayList<LocationInfo> mOptions2Items = null;
    ArrayList<LocationInfo> mOptions3Items = null;

    private boolean isFirst = true;//初始化数据标志

    public View getView() {
        return view;
    }

    public MWheelOptions(CharacterPickerView view) {
        super();
        this.view = view;
    }

    public void setOnOptionChangedListener(
            CharacterPickerView.OnOptionChangedListener listener) {
        this.mOnOptionChangedListener = listener;
    }

    public void setPicker(final ArrayList<LocationInfo> options1Items,
                          ArrayList<LocationInfo> options2Items,
                          ArrayList<LocationInfo> options3Items, Context mContext) {

        this.mOptions1Items = (options1Items == null ? new ArrayList<LocationInfo>() : options1Items);
        this.mOptions2Items = (options2Items == null ? new ArrayList<LocationInfo>() : options2Items);
        this.mOptions3Items = (options3Items == null ? new ArrayList<LocationInfo>() : options3Items);
        // 选项1
        wv_option1 = (LoopView) view.findViewById(R.id.j_options1);
        // 选项2
        wv_option2 = (LoopView) view.findViewById(R.id.j_options2);
        // 选项3
        wv_option3 = (LoopView) view.findViewById(R.id.j_options3);

        wv_option1.setArrayList(mOptions1Items);// 设置显示数据

        setCurrentItemFirst();//初始化数据

        //设置是否循环播放
        wv_option1.setCyclic(false);
        wv_option2.setCyclic(false);
        wv_option3.setCyclic(false);

        //滚动监听
        wv_option1.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                if (!isFirst) {
                    if (mOptions2Items.size() > 0) {
                        mOptions2Items = OptionsWindowHelper.getMonth(wv_option1.getCurrentItem() + OptionsWindowHelper.startInt[0]);
                    }
                    if (!mOptions2Items.isEmpty()) {
                        wv_option2.setArrayList(mOptions2Items);
                        wv_option2.setCurrentItem(0);
                    }
                    if (!mOptions3Items.isEmpty()) {
                        wv_option3.setArrayList(mOptions3Items);
                        wv_option3.setCurrentItem(0);
                    } else {
                        doItemChange();
                    }
                }

            }
        });

        // 选项2
        if (!mOptions2Items.isEmpty()) {
            wv_option2.setArrayList(mOptions2Items);// 设置显示数据
            //设置是否循环播放
            //wv_option2.setCyclic(true);
            //滚动监听
            wv_option2.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    if (!isFirst) {
                        if (mOptions3Items.size() > 0) {
                            mOptions3Items = OptionsWindowHelper.getDay(wv_option1.getCurrentItem() + OptionsWindowHelper.startInt[0], wv_option2.getCurrentItem() + OptionsWindowHelper.startInt[1]);
                        }
                        if (!mOptions3Items.isEmpty()) {
                            wv_option3.setArrayList(mOptions3Items);
                            wv_option3.setCurrentItem(0);
                        } else {
                            doItemChange();
                        }
                    }

                }
            });
        }

        // 选项3
        if (!mOptions3Items.isEmpty()) {
            wv_option3.setArrayList(mOptions3Items);
            //设置是否循环播放
            wv_option3.setNotLoop();
            //滚动监听
            wv_option3.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    if (!isFirst) {
                        doItemChange();
                    }

                }
            });
        }

        if (mOptions2Items.isEmpty())
            view.findViewById(R.id.j_layout2).setVisibility(View.GONE);
        if (mOptions3Items.isEmpty())
            view.findViewById(R.id.j_layout3).setVisibility(View.GONE);

    }

    /**
     * 选中项改变
     */
    private void doItemChange() {
        if (mOnOptionChangedListener != null) {
            int option1 = wv_option1.getCurrentItem();
            int option2 = wv_option2.getCurrentItem();
            int option3 = wv_option3.getCurrentItem();
            mOnOptionChangedListener.onOptionChanged(view, option1, option2, option3);
        }
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_option1.setCyclic(cyclic);
        wv_option2.setCyclic(cyclic);
        wv_option3.setCyclic(cyclic);
    }

    /**
     * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
     *
     * @return
     */
    public int[] getCurrentItems() {
        int[] currentItems = new int[3];
        currentItems[0] = wv_option1.getCurrentItem();
        currentItems[1] = wv_option2.getCurrentItem();
        currentItems[2] = wv_option3.getCurrentItem();
        return currentItems;
    }

    public void setCurrentItems(int option1, int option2, int option3) {
        wv_option1.setCurrentItem(option1);
        wv_option2.setCurrentItem(option2);
        wv_option3.setCurrentItem(option3);
    }

    /**
     * 初始化数据-显示当前日期
     */
    private void setCurrentItemFirst() {
        wv_option1.setCurrentItem(mOptions1Items.size() - 1);// 初始化时显示的数据
        if (mOptions2Items.size() > 0) {
            mOptions2Items = OptionsWindowHelper.getMonth(mOptions1Items.size() - 1 +OptionsWindowHelper.startInt[0]);
        }
        wv_option2.setCurrentItem(mOptions2Items.size() - 1);// 初始化时显示的数据
        if (mOptions3Items.size() > 0) {
            mOptions3Items = OptionsWindowHelper.getDay(mOptions1Items.size() - 1 + OptionsWindowHelper.startInt[0], mOptions2Items.size() - 1 + OptionsWindowHelper.startInt[1]);
        }
        wv_option3.setCurrentItem(mOptions3Items.size() - 1);// 初始化时显示的数据


        wv_option1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFirst = false;
                return false;
            }
        });

        wv_option2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFirst = false;
                return false;
            }
        });

        wv_option3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFirst = false;
                return false;
            }
        });
    }
}
