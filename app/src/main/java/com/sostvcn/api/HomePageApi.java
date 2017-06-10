package com.sostvcn.api;

import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosAudioRecommend;
import com.sostvcn.model.SosBookRecommend;
import com.sostvcn.model.SosSlideImage;
import com.sostvcn.model.SosVideoRecommend;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 首页API调用
 * Created by Administrator on 2017/4/22.
 */
public interface HomePageApi {

    @GET(NetWorkApi.loadHomeSlideData)
    Observable<BaseListResponse<SosSlideImage>> loadHomeSlideData();

    @GET(NetWorkApi.loadVideoRecommend)
    Observable<BaseListResponse<SosVideoRecommend>> loadVideoRecommend(@Query("page") int page);

    @GET(NetWorkApi.loadBookRecommend)
    Observable<BaseListResponse<SosBookRecommend>> loadBookRecommend(@Query("page") int page);

    @GET(NetWorkApi.loadAudioRecommend)
    Observable<BaseListResponse<SosAudioRecommend>> loadAudioRecommend(@Query("page") int page);


}
