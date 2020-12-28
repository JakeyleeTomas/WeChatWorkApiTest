package com.wechat.member;

import com.wechat.apiobject.TokenHelper;
import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author guji
 * @description
 * @date 2020/12/28 6:50
 */
public class Demo_03_02_repeat_evnclear {
    private static  final Logger logger = LoggerFactory.getLogger(Demo_03_01_repeat_timestamp.class);
    static String accessToken;
    @BeforeAll
    public static void setUp(){
        accessToken= TokenHelper.getAccessToken();
        logger.info(accessToken);
    }
    @Order(1)
    @Test
    @DisplayName("创建成员")
    void createMember(){
        String userid = "userid"+ FakerUtils.getTimeStamp();
        String name = "name" + FakerUtils.getTimeStamp();
        String mobile = "131" + FakerUtils.getTimeStamp();
        String body = "{\n" +
                "   \"userid\": \""+ userid +"\",\n" +
                "   \"name\": \""+ name +"\",\n" +
                "   \"mobile\": \""+ mobile +"\",\n" +
                "   \"department\": [2],\n" +
                "   \"main_department\": 2\n" +
                "}";
        Response response = given().log().all().contentType("application/json").when().body(body).post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken).then().log().all().extract().response();
        assertEquals("0",response.path("errcode").toString());
    }
    @Order(2)
    @Test
    @DisplayName("读取成员")
    void findMember() {
        String userid = "userid" + FakerUtils.getTimeStamp();
        String name = "name" + FakerUtils.getTimeStamp();
        String mobile = "132" + FakerUtils.getTimeStamp();
        String body = "{\n" +
                "   \"userid\": \"" + userid + "\",\n" +
                "   \"name\": \"" + name + "\",\n" +
                "   \"mobile\": \"" + mobile + "\",\n" +
                "   \"department\": [2],\n" +
                "   \"main_department\": 2\n" +
                "}";
        given().log().all().contentType("application/json").when().body(body).post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken).then().log().all().extract().response();
        Response response = given().log().all().when().get("https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken + "&userid=" + userid).then().log().all().extract().response();
        assertEquals("0", response.path("errcode").toString());
    }

    @Order(3)
    @Test
    @DisplayName("更新成员")
    void updateMember(){
        String userid = "userid" + FakerUtils.getTimeStamp();
        String name = "name" + FakerUtils.getTimeStamp();
        String mobile = "133" + FakerUtils.getTimeStamp();
        String body = "{\n" +
                "   \"userid\": \"" + userid + "\",\n" +
                "   \"name\": \"" + name + "\",\n" +
                "   \"mobile\": \"" + mobile + "\",\n" +
                "   \"department\": [2],\n" +
                "   \"main_department\": 2\n" +
                "}";
        given().log().all().contentType("application/json").when().body(body).post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken).then().log().all().extract().response();
        String updateBody ="{\n" +
                "    \"userid\": \""+userid+"\",\n" +
                "    \"name\": \""+name+"\",\n" +
                "    \"mobile\": \"+86 13800000000\",\n" +
                "    \"department\": 2,\n" +
                "    \"main_department\": 2\n" +
                "}";
        Response response = given().contentType("application/json").when().body(updateBody).post("https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token="+accessToken).then().log().all().extract().response();
        assertEquals("0",response.path("errcode").toString());
    }

    @Order(4)
    @Test
    @DisplayName("删除成员")
    void deleteMember(){
        String userid = "userid" + FakerUtils.getTimeStamp();
        String name = "name" + FakerUtils.getTimeStamp();
        String mobile = "134" + FakerUtils.getTimeStamp();
        String body = "{\n" +
                "   \"userid\": \"" + userid + "\",\n" +
                "   \"name\": \"" + name + "\",\n" +
                "   \"mobile\": \"" + mobile + "\",\n" +
                "   \"department\": [2],\n" +
                "   \"main_department\": 2\n" +
                "}";
        given().log().all().contentType("application/json").when().body(body).post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken).then().log().all().extract().response();
        Response response = given().log().all().when().get("https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token="+accessToken+"&userid="+userid).then().log().all().extract().response();
        assertEquals("0",response.path("errcode").toString());
    }

    @DisplayName("获取部门成员")
    @Order(5)
    @Test
    void getDepMember(){
        Response getDepMemberResponse = given().when().get("https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=" + accessToken + "&department_id=2")
                .then().log().all().extract().response();
        assertEquals("0",getDepMemberResponse.path("errcode").toString());

    }

    @BeforeEach
    void envClear(){
//        批量删除成员
    }

}
