package com.wechat.department;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author guji
 * @description：
 * 1.基础脚本，分别执行了，创建，修改，查询，删除接口并进行了校验
 * 2.进行了优化，方法之间进行了解耦，每个方法可独立运行
 * 3.进行了优化，使用时间戳命名法避免入参重复造成的报错
 * 4.进行了优化，每次方法执行前后都要对历史数据进行清理，确保每次执行脚本数据环境一致
 * @date 2020/12/20 7:11
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_03_02_repeat_evnclear {
    private static  final Logger logger = LoggerFactory.getLogger(Demo_03_02_repeat_evnclear.class);
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
         logger.info(accessToken);
    }

    @DisplayName("创建部门")
    @Order(1)
    @Test
    void creatDepartment(){
        String createBody="{\n" +
                "   \"name\": \"广州研发中心3\",\n" +
                "   \"parentid\": 1\n" +
                "}";
        Response createResponse = given()
                .when().contentType("application/json").body(createBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().all().extract().response();
        departmentId=createResponse.path("id")!=null ? createResponse.path("id").toString():null;
    }

    @DisplayName("修改部门")
    @Order(2)
    @Test
    void updateDepartment(){
        String createBody="{\n" +
                "   \"name\": \"广州研发中心3\",\n" +
                "   \"parentid\": 1\n" +
                "}";
        Response createResponse = given()
                .when().contentType("application/json").body(createBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().body().extract().response();
        departmentId=createResponse.path("id")!=null ? createResponse.path("id").toString():null;
        String updateBody="{\n" +
                "   \"id\": "+departmentId+"\n" +
                "}";
        Response updateResponse = given()
                .when().contentType("application/json").body(updateBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token="+accessToken)
                .then().log().all().extract().response();
        assertEquals("0",updateResponse.path("errcode").toString());

    }

    @DisplayName("查询部门")
    @Order(3)
    @Test
    void listDepartment(){
        String createBody="{\n" +
                "   \"name\": \"广州研发中心3\",\n" +
                "   \"parentid\": 1\n" +
                "}";
        Response createResponse = given()
                .when().contentType("application/json").body(createBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().body().extract().response();
        departmentId=createResponse.path("id")!=null ? createResponse.path("id").toString():null;

        Response listResponse = given()
                .when().log().all()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+accessToken+"&id="+departmentId)
                .then().log().all().extract().response();
        assertEquals("0",listResponse.path("errcode").toString());
        assertEquals(departmentId,listResponse.path("department.id[0]").toString());

    }

    @DisplayName("删除部门")
    @Order(4)
    @Test
    void deleteDepartment(){
       String createBody="{\n" +
                "   \"name\": \"广州研发中心3\",\n" +
                "   \"parentid\": 1\n" +
                "}";
        Response createResponse = given()
                .when().contentType("application/json").body(createBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().body().extract().response();
        departmentId=createResponse.path("id")!=null ? createResponse.path("id").toString():null;

        Response deleteResponse = given()
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token="+accessToken+"&id="+departmentId)
                .then().log().all().extract().response();
        assertEquals("0",deleteResponse.path("errcode").toString());

    }


    @AfterEach
    @BeforeEach
    void clearDepartment(){
        Response listResponse = given().log().all()
                .when()
                .param("id",1)
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + accessToken)
                .then().log().all().extract().response();
        ArrayList<Integer> departmentList = listResponse.path("department.id");
        for (int departmentId:departmentList){
            if (departmentId==1){
                continue;
            }
            Response deleteResponse = given()
                    .when()
                    .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token="+accessToken+"&id="+departmentId)
                    .then().log().all().extract().response();
            assertEquals("0",deleteResponse.path("errcode").toString());
        }
    }



}
