package com.sostvcn.api;

import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.BaseObjectResponse;
import com.sostvcn.model.SosAudioCatesInfo;
import com.sostvcn.model.SosAudioList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/5/18.
 */
public interface AudioPageApi {


    @GET(NetWorkApi.loadAudioCatesInfo)
    Observable<BaseListResponse<SosAudioCatesInfo>> loadCatesinfo();

    @GET(NetWorkApi.loadAudioList)
    Observable<BaseObjectResponse<SosAudioList>> loadAudioList(@Query("cate_id")int cate_id, @Query("playlist_id") int playlist_id);

}
