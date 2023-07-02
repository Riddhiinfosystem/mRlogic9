package com.riddhi.r_logig_olpl.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    @GET("RSuiteAPI/Config/GetCompanyIp")
    Call<ResponseBody> GetCompanyIp(@Query("ccode") String ccode);
}
