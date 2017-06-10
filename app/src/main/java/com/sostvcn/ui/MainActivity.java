package com.sostvcn.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.fragment.AudioFragment;
import com.sostvcn.fragment.BookFragment;
import com.sostvcn.fragment.HomeFragment;
import com.sostvcn.fragment.UserFragment;
import com.sostvcn.fragment.VideoFragment;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        BookFragment.OnFragmentInteractionListener,UserFragment.OnFragmentInteractionListener{

    @ViewInject(R.id.rd_group)
    private RadioGroup rpTab;
    @ViewInject(R.id.rd_menu_home)
    private RadioButton rbHome;
    @ViewInject(R.id.rd_menu_video)
    private RadioButton rbVideo;
    @ViewInject(R.id.rd_menu_book)
    private RadioButton rbBook;
    @ViewInject(R.id.rd_menu_audio)
    private RadioButton rbAudio;
    @ViewInject(R.id.rd_menu_user)
    private RadioButton rbUser;
    private Fragment fHome,fVideo,fBook,fAudio,fUser;

    //    退出时间
    private long exitTime = 0;

    @Override
    protected void onInitView(Bundle bundle) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rpTab.setOnCheckedChangeListener(this);
        rbHome.setChecked(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFrament(transaction);
        switch (i){
            case R.id.rd_menu_home:
                if(fHome == null){
                    fHome = new HomeFragment();
                    transaction.add(R.id.fragment_container, fHome);
                }else{
                    transaction.show(fHome);
                }
                break;
            case R.id.rd_menu_video:
                if(fVideo == null){
                    fVideo = new VideoFragment();
                    transaction.add(R.id.fragment_container, fVideo);
                }else{
                    transaction.show(fVideo);
                }
                break;
            case R.id.rd_menu_audio:
                if(fAudio == null){
                    fAudio = new AudioFragment();
                    transaction.add(R.id.fragment_container, fAudio);
                }else{
                    transaction.show(fAudio);
                }
                break;
            case R.id.rd_menu_book:
                if(fBook == null) {
                    fBook = new BookFragment();
                    transaction.add(R.id.fragment_container, fBook);
                }else{
                    transaction.show(fBook);
                }
                break;
            case R.id.rd_menu_user:
                if(fUser == null){
                    fUser = new UserFragment();
                    transaction.add(R.id.fragment_container, fUser);
                }else{
                    transaction.show(fUser);
                }
                break;
        }
        transaction.commit();
    }

    private void hideAllFrament(FragmentTransaction transaction){
        if(fHome != null){
            transaction.hide(fHome);
        }
        if(fVideo != null){
            transaction.hide(fVideo);
        }
        if(fBook != null){
            transaction.hide(fBook);
        }
        if(fAudio != null){
            transaction.hide(fAudio);
        }
        if(fUser != null){
            transaction.hide(fUser);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * 拦截返回键，要求点击两次返回键才退出应用
     *
     * @param keyCode 按键代码
     * @param event   点击事件
     * @return 是否处理本次事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
