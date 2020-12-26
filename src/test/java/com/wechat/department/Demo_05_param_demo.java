package com.wechat.department;

import com.wechat.apiobject.DepartMentObject;
import com.wechat.apiobject.TokenHelper;
import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author guji
 * @description：
 * 1.基础脚本，分别执行了，创建，修改，查询，删除接口并进行了校验
 * 2.进行了优化，方法之间进行了解耦，每个方法可独立运行
 * 3.进行了优化，使用时间戳命名法避免入参重复造成的报错
 * 4.进行了优化，每次方法执行前后都要对历史数据进行清理，确保每次执行脚本数据环境一致
 * 5.进行了优化，对脚本进行了分层，减少了重复代码，提高了代码复用性，减少维护成本
 * 6.进行了优化，因为要覆盖不同的入参，以数据驱动的方式将入参从代码剥离。
 * @date 2020/12/20 7:11
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_05_param_demo {
    private static  final Logger logger = LoggerFactory.getLogger(Demo_05_param_demo.class);
    static String accessToken;
    @BeforeAll
    public static void getAccessToken(){
         accessToken = TokenHelper.getAccessToken();
         logger.info(accessToken);
    }

//    @AfterEach
//    @BeforeEach
    void clearDepartment(){
        Response listResponse =DepartMentObject.listDepartment("1",accessToken);
        ArrayList<Integer> departmentIdList = listResponse.path("department.id");
        for(int departmentId : departmentIdList){
            if(1==departmentId){
                continue;
            }
            Response DelResponse = DepartMentObject.deleteDepartment(departmentId+"",accessToken);
        }
    }

    @DisplayName("创建部门")
    @ParameterizedTest
    @CsvFileSource(resources="/data/createDepartment.csv",numLinesToSkip=1)
    void creatDepartment(String createName,String createEnName,String returnCode){
        Response createResponse = DepartMentObject.createDepartment(createName,createEnName,accessToken);
        assertEquals(returnCode,createResponse.path("errcode").toString());
    }

    @DisplayName("修改部门")
    @Order(2)
    @Test
    void updateDepartment(){
        String updateName = "name" + FakerUtils.getTimeStamp();
        String updateEnName = "en_name" + FakerUtils.getTimeStamp();
        String departmentId=DepartMentObject.createDepartment(accessToken);
        Response updateResponse = DepartMentObject.updateDepartment(updateName,updateEnName,departmentId+"",accessToken);
        assertEquals("0",updateResponse.path("errcode").toString());

    }

    @DisplayName("查询部门")
    @Order(3)
    @Test
    void listDepartment(){
        String createName = "name" + FakerUtils.getTimeStamp();
        String createEnName = "en_name" + FakerUtils.getTimeStamp();
        Response createResponse = DepartMentObject.createDepartment(createName,createEnName,accessToken);

        String departmentId= createResponse.path("id")!=null?createResponse.path("id").toString():null;
        Response listResponse = DepartMentObject.listDepartment(departmentId,accessToken);
        assertEquals("0",listResponse.path("errcode").toString());
        assertEquals(departmentId,listResponse.path("department.id[0]").toString());
        assertEquals(createName,listResponse.path("department.name[0]").toString());
        assertEquals(createEnName,listResponse.path("department.name_en[0]").toString());

    }

    @DisplayName("删除部门")
    @Order(4)
    @Test
    void deleteDepartment(){
        String departmentId=DepartMentObject.createDepartment(accessToken);
        Response deleteResponse = DepartMentObject.deleteDepartment(departmentId,accessToken);
        assertEquals("0",deleteResponse.path("errcode").toString());

    }



}
