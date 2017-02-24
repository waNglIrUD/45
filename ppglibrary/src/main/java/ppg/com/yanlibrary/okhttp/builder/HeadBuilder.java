package ppg.com.yanlibrary.okhttp.builder;

import ppg.com.yanlibrary.okhttp.OkHttpUtils;
import ppg.com.yanlibrary.okhttp.request.OtherRequest;
import ppg.com.yanlibrary.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
