package com.wechat.member;

import com.wechat.apiobject.TokenHelper;
import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import javax.print.DocFlavor;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author guji
 * @description
 * 创建成员必须要有5个入参，api文档上显示必填参数只有2个是错误的，估计没有更新导致的，注意json格式中的值是要包含引号的
 * 1.基础脚本，分别执行了，创建，修改，查询，删除接口并进行了校验
 * 2.进行了优化，方法之间进行了解耦，每个方法可独立运行
 * 3.进行了优化，使用时间戳命名法避免入参重复造成的报错
 * 4.进行了优化，每次方法执行前后都要对历史数据进行清理，确保每次执行脚本数据环境一致
 * 5.进行了优化，对脚本进行了分层，减少了重复代码，提高了代码复用性，减少维护成本
 *
 * @date 2020/12/25 0:12
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Member {
    static String accessToken;
    @BeforeAll
    public static void setUp(){
        accessToken= TokenHelper.getAccessToken();
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
                "   \"department\": [1],\n" +
                "   \"main_department\": 1\n" +
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
                "   \"department\": [1],\n" +
                "   \"main_department\": 1\n" +
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
                "   \"department\": [1],\n" +
                "   \"main_department\": 1\n" +
                "}";
        given().log().all().contentType("application/json").when().body(body).post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken).then().log().all().extract().response();
        String updateBody ="{\n" +
                "    \"userid\": \""+userid+"\",\n" +
                "    \"name\": \""+name+"\",\n" +
                "    \"mobile\": \"+86 13800000000\",\n" +
                "    \"department\": 1,\n" +
                "    \"main_department\": 1\n" +
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
                "   \"department\": [1],\n" +
                "   \"main_department\": 1\n" +
                "}";
        given().log().all().contentType("application/json").when().body(body).post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken).then().log().all().extract().response();
        Response response = given().log().all().when().get("https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token="+accessToken+"&userid="+userid).then().log().all().extract().response();
        assertEquals("0",response.path("errcode").toString());
    }

}
