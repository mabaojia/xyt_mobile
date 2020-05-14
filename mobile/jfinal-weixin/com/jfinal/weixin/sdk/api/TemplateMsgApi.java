package com.jfinal.weixin.sdk.api;

import com.jfinal.kit.HttpKit;

public class TemplateMsgApi
{
     
    private static String sendTemplateMsg = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
 
    public static ApiResult sendTemplateMsg(String jsonStr) {
        String jsonResult = HttpKit.post(sendTemplateMsg+AccessTokenApi.getAccessToken().getAccessToken(), jsonStr);
        return new ApiResult(jsonResult);
    }
}
