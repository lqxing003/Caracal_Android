package com.patrick.caracal;

import com.patrick.caracal.util.Encrypt;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.patrick.caracal.util.Encrypt.urlEncoder;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private OkHttpClient client = new OkHttpClient();


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void queryExpress() throws Exception {
//        String expCode = "666570740477";        //天天快递
//        String expNo = "HHTT";
        String expCode = "210001633605";
        String expNo = "ANE";
        String requestData = "{'OrderCode':'','ShipperCode':'" + expNo + "','LogisticCode':'" + expCode + "'}";


        Map<String, String> params = new HashMap<>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", String.valueOf(1258729));
        params.put("RequestType", "1002");
        String dataSign = Encrypt.encrypt(requestData, "15505857-ae48-4e44-b104-522cf8512a14", "UTF-8");
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
            //System.out.println(entry.getKey()+":"+entry.getValue());
        }


        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, bodyParam.toString());

        Request request = new Request.Builder()
                .post(body)
                .url("http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx")
                .build();


        try {
            Response response = client.newCall(request).execute();
            print(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void print(String s) {
        System.out.print(s);
    }

//    /**
//     * Json方式 查询订单物流轨迹
//     * @throws Exception
//     */
//    public String getOrderTracesByJson(String expCode, String expNo) throws Exception{
//        String requestData= "{'OrderCode':'','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
//        params.put("EBusinessID", EBusinessID);
//        params.put("RequestType", "1002");
//        String dataSign=encrypt(requestData, AppKey, "UTF-8");
//        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
//        params.put("DataType", "2");
//
//        String result=sendPost(ReqURL, params);
//
//        //根据公司业务处理返回的信息......
//
//        return result;
//    }
}