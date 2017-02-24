package ppg.com.yanlibrary.utils.net;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.orhanobut.logger.Logger;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import myView.ToastProgress;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;
import ppg.com.yanlibrary.okhttp.OkHttpUtils;
import ppg.com.yanlibrary.okhttp.callback.Callback;
import ppg.com.yanlibrary.okhttp.https.HttpsUtils;
import ppg.com.yanlibrary.okhttp.request.CountingRequestBody;
import ppg.com.yanlibrary.utils.ToastUtil;
import ppg.com.yanlibrary.utils.StringUtil;
import ppg.com.yanlibrary.utils.json.JsonBaseBean;
import ppg.com.yanlibrary.utils.json.JsonFileCache;
import xprogressdialog.XProgressDialog;

/**
 * Created by ppg on 2016/4/14.
 */
public abstract class OkHttpCallBack {
    /**
     * 解析为JsonBaseBean,回调给调用者,post方式,会打印参数
     */
    public static abstract class PostTaskCallBack extends Callback<JsonBaseBean> {

        Context mContent;
        private XProgressDialog toastProgress;
        private boolean isShowProgress = true;
        private String progressMsg;
        public String URL;

        /**
         * @param Content 上下文
         * @param Msg     进度条的信息,没有则用""表示不开启进度条
         */
        public PostTaskCallBack(Context Content, String Msg) {
            this.mContent = Content;
            this.progressMsg = Msg;
            if (mContent != null)
                toastProgress = new XProgressDialog(mContent,2);
        }

        @Override
        public JsonBaseBean parseNetworkResponse(Response response) throws Exception {
            JsonBaseBean object = new JsonBaseBean();
            //response.body().string()只能调用一次,调用了response.body().string()方法之后，response中的流会被关闭
            if (((CountingRequestBody) response.request().body()).getRequestBody() instanceof MultipartBody) {
                String content = response.body().string();
                object.analysisJson(content);
                Logger.init("okdebug");
                Logger.json(content);
            } else {
                String content = response.body().string();
                object.analysisJson(content);
                Logger.init("okdebug");
                Logger.json(content);
                String url = response.request().url().toString();
                FormBody formBody = (FormBody) ((CountingRequestBody) response.request().body()).getRequestBody();
                String mkey = url + fromBodyLog2(formBody);
                //以url+post的参数为KEY,把成功获取到的数据转成String存入share
                if (!StringUtil.isEmpty(content)) {
                    JsonFileCache.storeData(mContent, mkey, content);
                }
            }
            return object;
        }




        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
            //获取frombody,这个一个保存post参数的类
            if (((CountingRequestBody) request.body()).getRequestBody() instanceof MultipartBody) {

            } else {
                FormBody formBody = (FormBody) ((CountingRequestBody) request.body()).getRequestBody();
                this.URL = request.url().toString() + fromBodyLog2(formBody);
                //通过logger打印求的参数
                Logger.init("okdebug");
                Logger.d("方式 POST   " + request.url() + "\n参数:\n" + fromBodyLog(formBody));
            }
            //开启进度条
            if (mContent != null) {
                this.isShowProgress = true;
                if (StringUtil.isEmpty(progressMsg))
                    isShowProgress = false;
                if (isShowProgress) {
                    progressStart(progressMsg);
                }
            }
        }

        @Override
        public void onAfter() {
            super.onAfter();
            progressClose();
        }

        @Override
        public String getURL() {
            if (!StringUtil.isEmpty(URL)) {
                return URL;
            } else return null;
        }

        @Override
        public void onError(Call call, Exception e) {
            Logger.init("okdebug");
            Logger.d("onError:  " + e.getMessage());
            ToastUtil.toast2_bottom(mContent, "网络连接异常");
        }

        /**
         * 简单的进度启动与关闭
         *
         * @param message
         */
        protected void progressStart(String message) {
            if (null == toastProgress)
                return;
            toastProgress.setMessage(message);
            if (!toastProgress.isShowing()) {
                toastProgress.show();
                //((Activity) progressDialog.getContext()).getWindow().setLayout(50, 50);
            }
        }

        protected void progressClose() {
            if (null == toastProgress)
                return;
            if (toastProgress.isShowing())
                toastProgress.dismiss();
        }

        //循环拼接post请求中的参数
        public StringBuffer fromBodyLog(FormBody formBody) {
            StringBuffer body = new StringBuffer();
            for (int i = 0; i < formBody.size(); i++) {
                try {
                    body.append("" + formBody.encodedName(i) + " = " + URLDecoder.decode(formBody.encodedValue(i), "UTF-8") + "\n");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return body;
        }

        //循环拼接post请求中的参数,去掉/n的方法,如果加了\n第二次取share的数据会取不到
        public StringBuffer fromBodyLog2(FormBody formBody) {
            StringBuffer body = new StringBuffer();
            for (int i = 0; i < formBody.size(); i++) {
                try {
                    body.append("" + formBody.encodedName(i) + "=" + URLDecoder.decode(formBody.encodedValue(i), "UTF-8") + "");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return body;
        }

    }


    public static abstract class GetTaskCallBack extends Callback<JsonBaseBean> {

        Context mContent;
        private XProgressDialog toastProgress;
        private boolean isShowProgress = true;
        private String progressMsg;
        public String URL;

        /**
         * @param Content 上下文
         * @param Msg     进度条的信息,没有则用""表示不开启进度条
         */
        public GetTaskCallBack(Context Content, String Msg) {
            this.mContent = Content;
            this.progressMsg = Msg;
            if (mContent != null)
                toastProgress = new XProgressDialog(mContent,2);
        }

        @Override
        public JsonBaseBean parseNetworkResponse(Response response) throws Exception {
            JsonBaseBean object = new JsonBaseBean();
            //response.body().string()只能调用一次,调用了response.body().string()方法之后，response中的流会被关闭
            String url = response.request().url().toString();
            String content = response.body().string();
            object.analysisJson(content);
            Logger.init("okdebug");
            Logger.json(content);
            String mkey = url;
            //以url+post的参数为KEY,把成功获取到的数据转成String存入share
            if (!StringUtil.isEmpty(content)) {
                JsonFileCache.storeData(mContent, mkey, content);
            }
            return object;
        }


        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
            this.URL = request.url().toString();
            Logger.d("方式:GET   " + request.url());
            //开启进度条
            if (mContent != null) {
                this.isShowProgress = true;
                if (StringUtil.isEmpty(progressMsg))
                    isShowProgress = false;
                if (isShowProgress) {
                    progressStart(progressMsg);
                }
            }
        }

        @Override
        public void onError(Call call, Exception e) {
            Logger.init("okdebug");
            Logger.d("onError:  " + e.getMessage());
            ToastUtil.toast2_bottom(mContent, "网络连接异常");
        }

        @Override
        public void onAfter() {
            super.onAfter();
            progressClose();
        }

        /**
         * 简单的进度启动与关闭
         *
         * @param message
         */
        protected void progressStart(String message) {
            if (null == toastProgress)
                return;
            toastProgress.setMessage(message);
            if (!toastProgress.isShowing()) {
                toastProgress.show();
                //((Activity) progressDialog.getContext()).getWindow().setLayout(50, 50);
            }
        }

        protected void progressClose() {
            if (null == toastProgress)
                return;
            if (toastProgress.isShowing())
                toastProgress.dismiss();
        }

        @Override
        public String getURL() {
            if (!StringUtil.isEmpty(URL)) {
                return URL;
            } else return null;
        }

    }

    /**
     * ONLY_CACHE //如果缓存存在,则使用缓存,不在则读取网络
     * ONLY_NETWORK://只读网络
     * CACHE_AND_NETWORK://如果缓存存在先读缓存,然后再读网络.
     */
    public enum CacheMode {
        ONLY_CACHE,
        CACHE_AND_NETWORK,
        ONLY_NETWORK,
        //  CYCLE_NETWORK;
    }
}
