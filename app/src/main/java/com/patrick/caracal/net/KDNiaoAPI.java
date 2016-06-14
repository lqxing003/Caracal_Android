package com.patrick.caracal.net;

import com.patrick.caracal.util.Encrypt;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.patrick.caracal.util.Encrypt.urlEncoder;

/**
 * Created by Patrick on 16/6/9.
 *
 * 快递鸟接口
 */

public class KDNiaoAPI {

    private static final String QUERY_API = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";
    private static final String APPKEY = "15505857-ae48-4e44-b104-522cf8512a14";
    private static final String E_BUSINESS_ID = "1258729";

    private static final OkHttpClient client = new OkHttpClient();

    //查询快递
    private static final String TYPE_QUERY = "1002";
    /**
     * 查询快递
     * @param expNo 快递单
     * @param expCode 快递公司编码
     * @param callback 回调
     */
    public static void queryExp(String expNo, String expCode, Callback callback) throws Exception{
        String requestData = "{'OrderCode':'','ShipperCode':'" + expNo + "','LogisticCode':'" + expCode + "'}";

        String bodyContent = generateParamsBodyStr(requestData,TYPE_QUERY);
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, bodyContent);
        Request request = new Request.Builder()
                .post(body)
                .url(QUERY_API)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //TODO 在线下单接口

    /**
     * 生成 post body的内容
     * @param requestData 一个string形式的json
     *                    ex: "{'OrderCode':'','ShipperCode':'" + expNo + "','LogisticCode':'" + expCode + "'}"
     * @return
     * @throws Exception 生成post body失败
     */
    private static String generateParamsBodyStr(String requestData,String funcType) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", E_BUSINESS_ID);
        params.put("RequestType", funcType);
        String dataSign = Encrypt.encrypt(requestData, APPKEY, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        StringBuilder bodyParam = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (bodyParam.length() > 0) {
                bodyParam.append("&");
            }
            bodyParam.append(entry.getKey());
            bodyParam.append("=");
            bodyParam.append(entry.getValue());
        }

        return bodyParam.toString();
    }
}
