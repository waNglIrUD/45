package com.example.administrator.hctk.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.hctk.Fragment.Home_Fragment.HC_HomeFragment;
import com.example.administrator.hctk.Fragment.MyAccount_Fragment.HC_MyAccountFragment;
import com.example.administrator.hctk.Fragment.ShareGoods_Fragment.HC_ShareGoodsFragment;
import com.example.administrator.hctk.Fragment.ShoppingCart_Fragment.HC_ShoppingCartFragment;
import com.example.administrator.hctk.R;


public class HC_MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private FragmentManager mFragmentManager;

    private Fragment mPre_fragment;

    private RadioGroup mRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        mRadioGroup = (RadioGroup) findViewById(R.id.rg_moduleOptions);
        mRadioGroup.setOnCheckedChangeListener(this);

            //默认选中第一个选项按钮
        ((RadioButton)findViewById(R.id.rb_home)).setChecked(true);

            //获取fragment管理器
    }


        //单选按钮事件监听接口重写方法
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

            //根据点击的单选按钮给tag赋相应的值
        String tag = "home";

        switch (checkedId){

            case R.id.rb_home:
                tag = "home";
                break;
            case R.id.rb_shareGoods:
                tag= "sharegoods";
                break;
            case R.id.rb_shoppingCart:
                tag= "shoppingcart";
                break;
            case R.id.rb_myAccount:
                tag= "myaccount";
                break;
        }

        Fragment fragment = mFragmentManager.findFragmentByTag(tag);

        if(fragment == null){
            if (tag.equals("home")){
                fragment = new HC_HomeFragment();
            }else if (tag.equals("sharegoods")){
                fragment = new HC_ShareGoodsFragment();
            }else if (tag.equals("shoppingcart")){
                fragment = new HC_ShoppingCartFragment();
            }else if (tag.equals("myaccount")){
                fragment = new HC_MyAccountFragment();
            }

            //把fragment放入manager中
            mFragmentManager.beginTransaction().add(R.id.fragment_placeHolder,fragment,tag).commit();
        }

        if (mPre_fragment !=null){
            mFragmentManager.beginTransaction().hide(mPre_fragment).commit();
        }

        mFragmentManager.beginTransaction().show(fragment).commit();
        mPre_fragment = fragment;
    }
}





