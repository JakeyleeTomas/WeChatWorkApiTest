package com.wechat.department;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author guji
 * @description ：
 * 1.基础脚本，分别执行了，创建，修改，查询，删除接口并进行了校验
 * @date 2020/12/20 7:11
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_01_base {
    private static  final Logger logger = LoggerFactory.getLogger(Demo_01_base.class);
    static String accessToken;
    static String departmentId;
    @BeforeAll
    public static void getAccessToken(){
         accessToken = given().log().all()
                .when()
                .param("corpid", "ww08701ecf7ef7ffc5")
                .param("corpsecret", "9BBxJMslOULZsy5xrjwo6JC8jjQ7PzmC2w5Q1CwbIok")
                .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then()
                .log().all().extract().response().path("access_token");
    }

    @DisplayName("创建部门")
    @Order(1)
    @Test
    void creatDepartment(){
        logger.info(accessToken);
        String body="{\n" +
                "   \"name\": \"广州研发中心3\",\n" +
                "   \"name_en\": \"RDGZ3\",\n" +
                "   \"parentid\": 1,\n" +
                "   \"order\": 1\n" +
                "}";
        Response response = given()
                .when().contentType("application/json").body(body)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().all().extract().response();
        departmentId = response.path("id").toString();
    }

    @DisplayName("修改部门")
    @Order(2)
    @Test
    void updateDepartment(){
        logger.info(accessToken);
        String body="{\n" +
                "   \"name\": \"深圳研发中心3\",\n" +
                "   \"name_en\": \"RDSZ3\",\n" +
                "   \"parentid\": 1,\n" +
                "   \"order\": 1,\n" +
                "   \"id\": 3\n" +
                "}";
        Response response = given()
                .when().contentType("application/json").body(body)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token="+accessToken)
                .then().log().all().extract().response();
        assertEquals("0",response.path("errcode").toString());

    }

    @DisplayName("查询部门")
    @Order(3)
    @Test
    void findDepartment(){
        logger.info(accessToken);

        Response response = given()
                .when().log().all()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+accessToken+"&id="+departmentId)
                .then().log().all().extract().response();
        assertEquals("0",response.path("errcode").toString());
        assertEquals(departmentId,response.path("department.id[0]").toString());

    }

    @DisplayName("删除部门")
    @Order(4)
    @Test
    void deleteDepartment(){
        logger.info(accessToken);
        Response response = given()
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token="+accessToken+"&id="+departmentId)
                .then().log().all().extract().response();
        assertEquals("0",response.path("errcode").toString());

    }



}
