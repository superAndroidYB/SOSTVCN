package com.sostvcn.api;

import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosBookCates;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/23.
 */
public interface BookPageApi {


    @GET(NetWorkApi.loadBookCates)
    Observable<BaseListResponse<SosBookCates>> loadCatesinfo();
}
