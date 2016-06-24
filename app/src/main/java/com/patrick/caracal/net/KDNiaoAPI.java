package com.patrick.caracal.net;

import com.patrick.caracal.util.Encrypt;

import java.io.UnsupportedEncodingException;
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
    //在线下单
    private static final String TYPE_ORDER_ONLINE = "1001";
    /**
     * 查询快递
     * @param expNo 快递单
     * @param expCode 快递公司编码
     * @param callback 回调
     */
    public static void queryExp(String expCode, String expNo, Callback callback) throws Exception{
        String requestData = "{'OrderCode':'','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";

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

    /**
     * 在线下单
     * @param payType 邮费支付方式:1-现付，2-到付
     * @param orderCode 订单编码(随机生成)
     * @param shipperCode 快递公司编码
     * @param toName 收货人姓名
     * @param toAddressArea 收货详细地址
     * @param toTel 收件人电话(电话和手机至少要一个)
     * @param toMobile 收件人电话(电话和手机至少要一个)
     * @param toProvinceID 收件省（如广东省，最好不要缺少“省”）
     * @param toCityID 收件市（如深圳市，最好不要缺少“市”）
     * @param toExpAreaID 收件区（如福田区，最好不要缺少“区”或“县”）
     * @param fromName 发货人姓名
     * @param fromAddressArea 发货详细地址
     * @param fromTel 发件人电话(电话和手机至少要一个)
     * @param fromMobile 发件人电话(电话和手机至少要一个)
     * @param fromProvinceID 发件省（如广东省，最好不要缺少“省”）
     * @param fromCityID 	发件市（如深圳市，最好不要缺少“市”）
     * @param fromExpAreaID 发件区（如福田区，最好不要缺少“区”或“县”）
     * @param callback 回调
     */
    public static void orderOnline(int payType,
                                   String orderCode,
                                   String shipperCode,
                                   String toName,
                                   String toAddressArea,
                                   String toTel,
                                   String toMobile,
                                   String toProvinceID,
                                   String toCityID,
                                   String toExpAreaID,
                                   String fromName,
                                   String fromAddressArea,
                                   String fromTel,
                                   String fromMobile,
                                   String fromProvinceID,
                                   String fromCityID,
                                   String fromExpAreaID,
                                   Callback callback) throws Exception {

        String requestData =
                "{'PayType':"+ payType +"," +
                //是否代收货款：1-是，2-否 不设置代收运费
                "'IsNeedPay':2," +
                "'OrderCode':'" + orderCode + "'," +
                "'ShipperCode':'" + shipperCode + "'," +
                "'ToName':'" + toName + "'," +
                "'ToAddressArea':'" + toAddressArea + "'," +
                "'ToTel':'" + toTel + "'," +
                "'ToMobile':'" + toMobile + "'," +
                //运单类型：2-纸质运单
                "'OrderType':2," +
                "'ToProvinceID':'" + toProvinceID + "'," +
                "'ToCityID':'" + toCityID + "'," +
                "'ToExpAreaID':'" + toExpAreaID + "'," +
                "'FromName':'" + fromName + "'," +
                "'FromAddressArea':'" + fromAddressArea + "'," +
                "'FromTel':'" + fromTel + "'," +
                "'FromMobile':'" + fromMobile + "'," +
                "'FromProvinceID':'" + fromProvinceID + "'," +
                "'FromCityID':'" + fromCityID + "'," +
                "'FromExpAreaID':'" + fromExpAreaID + "'" +
                "}";

        String bodyContent = generateParamsBodyStr(requestData,TYPE_ORDER_ONLINE);
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, bodyContent);
        Request request = new Request.Builder()
                .post(body)
                .url(QUERY_API)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
