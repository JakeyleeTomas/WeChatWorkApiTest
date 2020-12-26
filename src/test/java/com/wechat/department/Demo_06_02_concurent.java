package com.wechat.department;

import com.wechat.apiobject.DepartMentObject;
import com.wechat.apiobject.TokenHelper;
import com.wechat.task.EvnHelperTask;
import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author guji
 * @description：
 * 1.基础脚本，分别执行了，创建，修改，查询，删除接口并进行了校验
 * 2.进行了优化，方法之间进行了解耦，每个方法可独立运行
 * 3.进行了优化，使用时间戳命名法避免入参重复造成的报错
 * 4.进行了优化，每次方法执行前后都要对历史数据进行清理，确保每次执行脚本数据环境一致
 * 5.进行了优化，对脚本进行了分层，减少了重复代码，提高了代码复用性，减少维护成本
 * @date 2020/12/20 7:11
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_06_02_concurent {
    private static  final Logger logger = LoggerFactory.getLogger(Demo_06_02_concurent.class);
    static String accessToken;
    @BeforeAll
    public static void getAccessToken(){
         accessToken = TokenHelper.getAccessToken();
         logger.info(accessToken);
    }

    @AfterEach
    @BeforeEach
    void clearDepartment(){
        EvnHelperTask.clearDpartMentTask(accessToken);
       /* Response listResponse =DepartMentObject.listDepartment("1",accessToken);
        ArrayList<Integer> departmentIdList = listResponse.path("department.id");
        for(int departmentId : departmentIdList){
            if(1==departmentId){
                continue;
            }
            Response DelResponse = DepartMentObject.deleteDepartment(departmentId+"",accessToken);
        }*/
    }

    @DisplayName("创建部门")
    @Test
    @RepeatedTest(10)
    void creatDepartment(){
        String backendStr = Thread.currentThread().getId()+FakerUtils.getTimeStamp();
        String createName = "name" + backendStr;
        String createEnName = "en_name" + backendStr;
        Response createResponse = DepartMentObject.createDepartment(createName,createEnName,accessToken);
        assertEquals("0",createResponse.path("errcode").toString());
    }

    @DisplayName("修改部门")
    @Test
    void updateDepartment(){
        String backendStr = Thread.currentThread().getId()+FakerUtils.getTimeStamp();
        String createName = "name" + backendStr;
        String createEnName = "en_name" + backendStr;
        Response createResponse = DepartMentObject.createDepartment(createName,createEnName,accessToken);
        String departmentId= createResponse.path("id")!=null ? createResponse.path("id").toString():null;
        String updateName = "name" + backendStr;
        String updateEnName = "en_name" + backendStr;
        Response updateResponse = DepartMentObject.updateDepartment(updateName,updateEnName,departmentId+"",accessToken);
        assertEquals("0",updateResponse.path("errcode").toString());

    }





}
