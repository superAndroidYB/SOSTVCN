package com.sostvcn.api;

import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.BaseObjectResponse;
import com.sostvcn.model.RecentlyVideo;
import com.sostvcn.model.SosCateVideo;
import com.sostvcn.model.SosCatesInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/5/1.
 */
public interface VideoPageApi {

    @GET(NetWorkApi.loadCatesInfo)
    Observable<BaseListResponse<SosCatesInfo>> loadCatesInfo();

    @GET(NetWorkApi.loadVideoInfo)
    Observable<BaseObjectResponse<SosCateVideo>> loadVideoInfo(@Query("video_id") int video_id);

    @GET(NetWorkApi.loadRecentlyVideo)
    Observable<BaseListResponse<RecentlyVideo>> loadRecentlyVideo(@Query("cate_id") int cate_id);

    @GET(NetWorkApi.loadAllVideo)
    Observable<BaseListResponse<SosCateVideo.Video>> loadAllVideo(@Query("cate_id") int cate_id);

    @GET(NetWorkApi.loadVideoListByCateId)
    Observable<BaseObjectResponse<SosCateVideo>> loadVideoListByCateId(@Query("cate_id") int cate_id);
}
