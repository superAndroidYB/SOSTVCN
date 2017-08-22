package com.sostvcn.api;

import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosBookCates;
import com.sostvcn.model.SosBookInfo;
import com.sostvcn.model.SosMagazines;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/23.
 */
public interface BookPageApi {


    @GET(NetWorkApi.loadBookCates)
    Observable<BaseListResponse<SosBookCates>> loadCatesinfo();

    @GET(NetWorkApi.loadMagazines)
    Observable<BaseListResponse<SosMagazines>> loadMagazines(@Query("cate_id") int cate_id);

    @GET(NetWorkApi.loadBookInfo)
    Observable<BaseListResponse<SosBookInfo>> loadBookInfo(@Query("cate_id") int cate_id);
}
