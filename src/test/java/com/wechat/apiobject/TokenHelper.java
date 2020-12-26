package com.wechat.apiobject;

import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

/**
 * @author guji
 * @description
 * @date 2020/12/22 13:21
 */
public class TokenHelper {
    public static String  getAccessToken(){
            String accessToken = given().log().all()
                    .when()
                    .param("corpid", "ww08701ecf7ef7ffc5")
                    .param("corpsecret", "9BBxJMslOULZsy5xrjwo6JC8jjQ7PzmC2w5Q1CwbIok")
                    .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                    .then().log().body()
                    .extract().response().path("access_token");
            return accessToken;
    }



}
