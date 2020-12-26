package com.wechat.apiobject;

import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;


/**
 * @author guji
 * @description
 * @date 2020/12/22 23:56
 */
public class DepartMentObject {
    public static Response createDepartment(String createName,String createEnName,String accessToken){
        String createBody="{\n" +
                "   \"name\": \"" + createName + "\",\n" +
                "   \"name_en\": \"" + createEnName + "\",\n" +
                "   \"parentid\": 1\n" +
                "}";
        Response createResponse = given().log().all()
                .when().contentType("application/json")
                .body(createBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().all().extract().response();
        return createResponse;
    }

    public static String createDepartment(String accessToken){
        String createName = "name" + FakerUtils.getTimeStamp();
        String createEnName = "en_name" + FakerUtils.getTimeStamp();
        Response createResponse = createDepartment(createName,createEnName,accessToken);
        String departmentId= createResponse.path("id")!=null ? createResponse.path("id").toString():null;
        return departmentId;
    }

    public static String createDepartmentByRandomInt(String accessToken){
        String creatName= "name"+ FakerUtils.getRandomInt(1000);
        String creatEnName="en_name"+ FakerUtils.getRandomInt(1000);
        String creatBody ="{\n" +
                "   \"name\": \""+creatName+"\",\n" +
                "   \"name_en\": \""+creatEnName+"\",\n" +
                "   \"parentid\": 1}";
        Response creatResponse=given().log().all()
                .contentType("application/json")
                .body(creatBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken+"")
                .then()
                .log().body()
                .extract()
                .response()
                ;
        String departmentId= creatResponse.path("id")!=null ? creatResponse.path("id").toString():null;
        return departmentId;
    }

    public static Response updateDepartment(String updateName,String updateEnName,String departmentId,String accessToken){
        String updateBody ="{\n" +
                "   \"id\": "+departmentId+",\n" +
                "   \"name\": \""+updateName+"\",\n" +
                "   \"name_en\": \""+updateEnName+"\",\n" +
                "   \"order\": 1\n" +
                "}\n";
        Response updateResponse=given().log().all()
                .contentType("application/json")
                .body(updateBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token="+accessToken+"")
                .then()
                .log().body()
                .extract().response();
        return updateResponse;
    }

    public static Response listDepartment(String departmentId,String accessToken){
        Response listResponse = given().log().all()
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+accessToken+"&id="+departmentId)
                .then().log().body().extract().response();
        return listResponse;
    }

    public static Response deleteDepartment(String departmentId,String accessToken){
        Response deleteResponse = given().log().all()
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token="+accessToken+"&id="+departmentId)
                .then().log().body().extract().response();
        return deleteResponse;
    }



}



