package com.zjmxdz.server.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.wyc.common.annotation.Auth;
import com.wyc.common.context.UserContext;
import com.wyc.common.domain.Client;
import com.wyc.common.service.ClientService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.util.ExcelUtil;
import com.zjmxdz.dao.TappOrderDao;
import com.zjmxdz.domain.TappImportTask;
import com.zjmxdz.domain.TappOrder;
import com.zjmxdz.domain.TbaseResource;
import com.zjmxdz.domain.TbaseUserinfo;
import com.zjmxdz.domain.dto.TappOrderDto;
import com.zjmxdz.domain.dto.TbaseUserinfoDto;
import com.zjmxdz.domain.vo.*;
import com.zjmxdz.service.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("api")
public class MainApi {

    @Autowired
    TbaseUserinfoService tbaseUserinfoService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TappPurchaseRecordService tappPurchaseRecordService;

    @Autowired
    private TappSubordinateService tappSubordinateService;

    @Autowired
    private ExecuterService executerService;

    @Autowired
    private TappImportTaskService tappImportTaskService;

    @Autowired
    private TappOrderService tappOrderService;

    @Autowired
    private TbaseResourceService tbaseResourceService;

    @Value("${usercenter.token.expires}")
    private Long expires;

    @Value("${fileDir}")
    private String fileDir;

    @RequestMapping("importTasks")
    @ResponseBody
    @Transient
    public Object importTasks()throws Exception{
        try {
            executerService.importAllTask();
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            return map;
        }catch (Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            return map;
        }
    }


    @RequestMapping("uploadFile")
    @ResponseBody
    @Auth(mustAuthentication = false)
    @Transient
    public Object uploadFile(@RequestParam("file")MultipartFile multipartFile)throws Exception{
        InputStream inputStream = multipartFile.getInputStream();
        String fileName = multipartFile.getOriginalFilename();
        File dir = new File(fileDir,fileName);
        if(!dir.exists()){
            dir.mkdirs();
        }

        File file = new File(dir,UUID.randomUUID().toString());

        IOUtils.copy(inputStream,new FileOutputStream(file));

        TbaseResource tbaseResource = new TbaseResource();
        tbaseResource.setName(fileName);
        tbaseResource.setFilePath(file.getPath());
        tbaseResourceService.add(tbaseResource);

        Map<String,Object> data = new HashMap<>();
        data.put("success",true);
        data.put("data",tbaseResource);
        return data;
    }

    @RequestMapping("uploadUserFile")
    @ResponseBody
    @Transient
    public Object uploadUserFile(HttpServletRequest httpServletRequest)throws Exception{
        String resourceId = httpServletRequest.getParameter("resourceId");
        TbaseResource tbaseResource = tbaseResourceService.findOne(resourceId);
        executerService.uploadUserTask(tbaseResource.getFilePath(),tbaseResource.getName());
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }

    @RequestMapping("uploadOrderFile")
    @ResponseBody
    @Transient
    public Object uploadOrderFile(HttpServletRequest httpServletRequest)throws Exception{
        String resourceId = httpServletRequest.getParameter("resourceId");
        TbaseResource tbaseResource = tbaseResourceService.findOne(resourceId);
        executerService.uploadOrderTask(tbaseResource.getFilePath(),tbaseResource.getName());
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }

    @RequestMapping("importTask")
    @ResponseBody
    @Transient
    public Object importTask(HttpServletRequest httpServletRequest)throws Exception{
        String taskId = httpServletRequest.getParameter("taskId");
        executerService.importTask(taskId);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;

    }

    @RequestMapping("taskUserinfoDetailByResourceId")
    @ResponseBody
    @Transient
    public Object taskUserinfoDetailByResourceId(HttpServletRequest httpServletRequest)throws Exception{

        String resourceId = httpServletRequest.getParameter("resourceId");
        TbaseResource tbaseResource = tbaseResourceService.findOne(resourceId);
        String filePath = tbaseResource.getFilePath();
        FileInputStream fileInputStream = new FileInputStream(filePath);
        List<ImportUserData> importUserDataList = ExcelUtil.read(ImportUserData.class,fileInputStream,0,10);

        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("data",importUserDataList);

        return map;
    }

    @RequestMapping("taskOrderDetailByResourceId")
    @ResponseBody
    @Transient
    public Object taskOrderDetailByResourceId(HttpServletRequest httpServletRequest)throws Exception{

        String resourceId = httpServletRequest.getParameter("resourceId");
        TbaseResource tbaseResource = tbaseResourceService.findOne(resourceId);
        String filePath = tbaseResource.getFilePath();
        FileInputStream fileInputStream = new FileInputStream(filePath);
        List<ImportOrderData> importUserDataList = ExcelUtil.read(ImportOrderData.class,fileInputStream,0,10);

        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("data",importUserDataList);

        return map;
    }


    @RequestMapping("orderTaskInfo")
    @ResponseBody
    public Object orderTaskInfo(HttpServletRequest httpServletRequest){
        String taskId = httpServletRequest.getParameter("taskId");
        TappImportTask tappImportTask = tappImportTaskService.findOne(taskId);
        if(CommonUtil.isEmpty(tappImportTask)){
            Map<String,Object> data = new HashMap<>();
            data.put("success",false);
            return data;
        }else{
            TappOrderDto tappOrderDto = new TappOrderDto();
            tappOrderDto.setTaskId(taskId);
            List<TappOrder> tappOrders = tappOrderService.findAll(tappOrderDto);
            Map<String,Object> data = new HashMap<>();
            data.put("success",false);
            data.put("data",tappOrders);
            return data;
        }
    }

    @RequestMapping("userTaskInfo")
    @ResponseBody
    public Object userTaskInfo(HttpServletRequest httpServletRequest){
        String taskId = httpServletRequest.getParameter("taskId");
        TappImportTask tappImportTask = tappImportTaskService.findOne(taskId);
        if(CommonUtil.isEmpty(tappImportTask)){
            Map<String,Object> data = new HashMap<>();
            data.put("success",false);
            return data;
        }else{
            TbaseUserinfoDto tbaseUserinfoDto = new TbaseUserinfoDto();
            tbaseUserinfoDto.setTaskId(taskId);
            List<TbaseUserinfo> tbaseUserinfos = tbaseUserinfoService.findAll(tbaseUserinfoDto);
            Map<String,Object> data = new HashMap<>();
            data.put("success",false);
            data.put("data",tbaseUserinfos);
            return data;
        }
    }

    @RequestMapping("taskList")
    @ResponseBody
    public Object tasks(HttpServletRequest httpServletRequest){
        return tappImportTaskService.findAllTasks(0,100);
    }


    @RequestMapping("countGrade")
    @ResponseBody
    public Object countGrade(HttpServletRequest httpServletRequest){
        Client client = UserContext.get();
        TbaseUserinfo userInfo = tbaseUserinfoService.findOne(client.getUserId());
        if(CommonUtil.isNotEmpty(userInfo)) {
            executerService.countGrade(userInfo);
            Map<String,Object> data = new HashMap<>();
            data.put("success",true);
            return data;
        }else{
            Map<String,Object> data = new HashMap<>();
            data.put("success",false);
            return data;
        }
    }


    @RequestMapping("userInfo")
    @ResponseBody
    public Object userInfo(HttpServletRequest httpServletRequest){
        Client client = UserContext.get();
        UserInfoVo userInfo = tbaseUserinfoService.userInfo(client.getUserId());
        Map<String,Object> response = new HashMap<>();
        response.put("success",true);
        response.put("data",userInfo);
        return response;
    }

    @RequestMapping("subordinate")
    @ResponseBody
    public Object userinfoSubordinate(HttpServletRequest httpServletRequest){
        Client client = UserContext.get();
        List<SubordinateVo> subordinates = tappSubordinateService.subordinates(client.getUserId());
        Map<String,Object> response = new HashMap<>();
        response.put("success",true);
        response.put("data",subordinates);
        return response;
    }

    @RequestMapping("updatePassword")
    @ResponseBody
    public Object updatePassword(HttpServletRequest httpServletRequest){
        String newPassword = httpServletRequest.getParameter("new_password");
        String oldPassword = httpServletRequest.getParameter("old_password");
        String account = httpServletRequest.getParameter("account");
        TbaseUserinfoDto tbaseUserinfoDto = new TbaseUserinfoDto();
        tbaseUserinfoDto.setUsername(account);
        TbaseUserinfo tbaseUserinfo = tbaseUserinfoService.findOne(tbaseUserinfoDto);
        if(CommonUtil.isEmpty(tbaseUserinfo)){
            Map<String,Object> data = new HashMap<>();
            data.put("success",false);
            return data;
        }else{
            String password = tbaseUserinfo.getPassword();
            if(password.equals(oldPassword)){
                tbaseUserinfo.setPassword(newPassword);
                try {
                    tbaseUserinfoService.update(tbaseUserinfo);
                    Map<String,Object> data = new HashMap<>();
                    data.put("success",true);
                    return data;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{

            }

            Map<String,Object> data = new HashMap<>();
            data.put("success",false);
            return data;
        }
    }

    @RequestMapping("records")
    @ResponseBody
    public Object records(HttpServletRequest httpServletRequest){
        Client client = UserContext.get();
        List<PurchaseRecordVo> purchaseRecordVos = tappPurchaseRecordService.records(client.getUserId());
        Map<String,Object> response = new HashMap<>();
        response.put("success",true);
        response.put("data",purchaseRecordVos);
        return response;

    }



    @RequestMapping("login")
    @ResponseBody
    @Auth(mustAuthentication = false)
    public Object login(HttpServletRequest httpServletRequest){
       String username = httpServletRequest.getParameter("username");
       String password = httpServletRequest.getParameter("password");

       TbaseUserinfoDto tbaseUserinfoDto = new TbaseUserinfoDto();
       tbaseUserinfoDto.setUsername(username);
       tbaseUserinfoDto.setPassword(password);
       TbaseUserinfo tbaseUserinfo = tbaseUserinfoService.findOne(tbaseUserinfoDto);

       Map<String,Object> response = new HashMap<>();
       if(CommonUtil.isNotEmpty(tbaseUserinfo)){
            response.put("success",true);
            response.put("data",tbaseUserinfo);
           JWTCreator.Builder builder = JWT.create().withIssuedAt(new Date())
                   .withExpiresAt(new Date(System.currentTimeMillis() + expires))
                   .withSubject(tbaseUserinfo.getId()).withIssuer("u").withClaim("username",
                           tbaseUserinfo.getUsername());
           String token = builder.sign(UserContext.getDefaultAlgorithm());
           response.put("token",token);
           Client clientDto = new Client();
           clientDto.setUserId(tbaseUserinfo.getId());
           Client client = clientService.findOne(clientDto);
           if(CommonUtil.isNotEmpty(client)){
           }else {
               client = new Client();
               client.setToken(token);
               client.setUserId(tbaseUserinfo.getId());
               clientService.add(client);
           }
           UserContext.set(client);
       }else{
           response.put("success",false);
       }

       return response;
    }

}
