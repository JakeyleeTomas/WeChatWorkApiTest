package com.wechat.member;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author guji
 * @description
 * 1.基础脚本，分别执行了，创建，修改，查询，删除接口并进行了校验
 * 2.进行了优化，方法之间进行了解耦，每个方法可独立运行
 * @date 2020/12/27 9:38
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_02_separate {
    private static  final Logger logger = LoggerFactory.getLogger(Demo_02_separate.class);

    static String accessToken;
    @BeforeAll
    public static void setup(){
        String corpid = "ww08701ecf7ef7ffc5";
        String corpsecret = "9BBxJMslOULZsy5xrjwo6JC8jjQ7PzmC2w5Q1CwbIok";
        accessToken = given().log().all().when()
            .param("corpid",corpid)
            .param("corpsecret",corpsecret).get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
            .then().log().all().extract().response().path("access_token");
        logger.info(accessToken);
    }

    @Test
    @Order(1)
    @DisplayName("创建成员")
    void createMenber(){
            String createBody = "{\n" +
                    "   \"userid\": \"zhangsan5\",\n" +
                    "   \"name\": \"张三5\",\n" +
                    "   \"mobile\": \"+86 13800000001\",\n" +
                    "   \"department\": [1],\n" +
                    "   \"main_department\": 1\n" +
                    "}";
            Response createResponse = given().log().all().contentType("application/json").when().body(createBody)
                    .post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken)
                    .then().log().all().extract().response();
            assertEquals("0",createResponse.path("errcode").toString());

    }

    @Test
    @Order(2)
    @DisplayName("查找成员")
    void findMember(){
        String userid = "zhangsan6";
        String createBody = "{\n" +
                "   \"userid\": \""+ userid +"\",\n" +
                "   \"name\": \"张三6\",\n" +
                "   \"mobile\": \"+86 13800000006\",\n" +
                "   \"department\": [1],\n" +
                "   \"main_department\": 1\n" +
                "}";
        given().log().all().contentType("application/json").when().body(createBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken)
                .then().log().all();
        Response findResponse = given().log().all().contentType("application/json").when()
                .param("access_token",accessToken)
                .param("userid",userid)
                .get("https://qyapi.weixin.qq.com/cgi-bin/user/get")
                .then().log().all().extract().response();
        assertEquals("0",findResponse.path("errcode").toString());
        assertEquals(userid,findResponse.path("userid").toString());

    }

    @Test
    @Order(3)
    @DisplayName("修改成员")
    void updateMember(){
        String userid = "zhangsan7";
        String createBody = "{\n" +
                "   \"userid\": \""+ userid +"\",\n" +
                "   \"name\": \"张三7\",\n" +
                "   \"mobile\": \"+86 13800000007\",\n" +
                "   \"department\": [1],\n" +
                "   \"main_department\": 1\n" +
                "}";
        given().log().all().contentType("application/json").when().body(createBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken)
                .then().log().all();
        String updateBody="{\n" +
                "   \"userid\": \"zhangsan7\",\n" +
                "   \"name\": \"张三7\",\n" +
                "   \"mobile\": \"+86 13877777777\",\n" +
                "   \"department\": [1],\n" +
                "   \"main_department\": 1\n" +
                "}";
        Response updateResponse = given().log().all().contentType("application/json").when()
                .body(updateBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token="+accessToken)
                .then().log().all().extract().response();
        assertEquals("0",updateResponse.path("errcode").toString());

    }

    @Test
    @Order(4)
    @DisplayName("删除成员")
    void deleteMember(){
        String userid = "zhangsan8";
        String createBody = "{\n" +
                "   \"userid\": \""+ userid +"\",\n" +
                "   \"name\": \"张三8\",\n" +
                "   \"mobile\": \"+86 13800000008\",\n" +
                "   \"department\": [1],\n" +
                "   \"main_department\": 1\n" +
                "}";
        given().log().all().contentType("application/json").when().body(createBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken)
                .then().log().all();
        Response deleteResponse = given().log().all().when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token="+accessToken+"&userid="+userid)
                .then().log().all().extract().response();
        assertEquals("0",deleteResponse.path("errcode").toString());

    }
}
