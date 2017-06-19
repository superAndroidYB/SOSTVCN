package com.sostvcn;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.antfortune.freeline.FreelineCore;
import com.danikula.videocache.HttpProxyCacheServer;
import com.sostvcn.api.NetWorkApi;
import com.sostvcn.gateway.config.NetWorkConfiguration;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.utils.SDCardUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/4/22.
 */
public class SostvApplication extends Application {

    private static Context mContext;
    private HttpProxyCacheServer proxyCacheServer;
    public static Typeface typefacePingf;

    //各平台分享的id和key
    {
        PlatformConfig.setWeixin("wx94f3c66792c96e9f", "8126577fbf6007010baace3ede3ede42");
        PlatformConfig.setQQZone("1105441735", "TCS0zGR5SdsdvkJ3");
        PlatformConfig.setSinaWeibo("351068525", "1e24b228bf3dd7d3c4da36656872b7bf", "http://www.sostvcn.com");
        PlatformConfig.setAlipay("2017060407417390");
    }

    //应用程序的入口
    @Override
    public void onCreate() {
        super.onCreate();
        //应用程序的上下文
        mContext = getApplicationContext();

        FreelineCore.init(this);
        initOkHttpUtils();

        Config.DEBUG = true;
        UMShareAPI.get(this);
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, "576e988c67e58e9040003afd",
                "Wandoujia", MobclickAgent.EScenarioType.E_UM_NORMAL));


        initFont();
    }

    /**
     * 设置全局字体为苹方
     */
    private void initFont(){
        typefacePingf = Typeface.createFromAsset(getAssets(),"fonts/pingfang.ttf");
        try
        {
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null, typefacePingf);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    private void initOkHttpUtils() {
        /**
         *  网络配置
         */
        NetWorkConfiguration configuration = new NetWorkConfiguration(this)
                .baseUrl(NetWorkApi.BASE_URL)
                .isCache(true)
                .isDiskCache(true)
                .isMemoryCache(true);
        HttpUtils.setConFiguration(configuration);

    }

    public static Context getContext() {
        return mContext;
    }

    public static HttpProxyCacheServer getProxy() {
        SostvApplication sostvApplication = (SostvApplication) mContext.getApplicationContext();
        return sostvApplication.proxyCacheServer == null ? (sostvApplication.proxyCacheServer = sostvApplication.newProxy()) : sostvApplication.proxyCacheServer;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this).maxCacheFilesCount(20)
                .cacheDirectory(new File(SDCardUtils.getSDCardRootDir() + "/sostv_video_cache")).build();
    }

}
