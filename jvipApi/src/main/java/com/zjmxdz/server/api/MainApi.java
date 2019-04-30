package com.zjmxdz.server.api;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.wyc.common.annotation.Auth;
import com.wyc.common.context.UserContext;
import com.wyc.common.domain.Client;
import com.wyc.common.service.ClientService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.util.ExcelUtil;
import com.zjmxdz.domain.*;
import com.zjmxdz.domain.dto.TappOrderDto;
import com.zjmxdz.domain.dto.TbaseUserinfoDto;
import com.zjmxdz.domain.vo.*;
import com.zjmxdz.service.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    private TbasePurchaseConfigService tbasePurchaseConfigService;

    @Autowired
    private TbaseIntegralConfigService tbaseIntegralConfigService;

    @Autowired
    private TbaseGradeConfigService tbaseGradeConfigService;

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


    @RequestMapping("purchaseConfigs")
    @ResponseBody
    @Transient
    public Object purchaseConfigs(HttpServletRequest httpServletRequest){
        List<TbasePurchaseConfig> tbasePurchaseConfigs = tbasePurchaseConfigService.findAll();
        return tbasePurchaseConfigs;
    }

    @RequestMapping("integralConfigs")
    @ResponseBody
    @Transient
    public Object integralConfigs(HttpServletRequest httpServletRequest){
        List<TbaseIntegralConfig> tbaseIntegralConfigs = tbaseIntegralConfigService.findAll();
        return tbaseIntegralConfigs;
    }

    @RequestMapping("gradeConfig")
    @ResponseBody
    @Transient
    public Object gradeConfig(HttpServletRequest httpServletRequest){
        List<TbaseGradeConfig> tbaseGradeConfigs = tbaseGradeConfigService.findAll();
        return tbaseGradeConfigs;
    }

    @RequestMapping("flushPurchaseConfig")
    @ResponseBody
    @Transient
    public Object flushPurchaseConfig(@RequestBody List<TbasePurchaseConfigVo> purchaseConfigs)throws Exception{
        for(TbasePurchaseConfigVo tbasePurchaseConfigVo:purchaseConfigs){
            TbasePurchaseConfig tbasePurchaseConfig = tbasePurchaseConfigService.findOne(tbasePurchaseConfigVo.getId());
            //tbasePurchaseConfig.setConditionGrade(tbasePurchaseConfig.getConditionGrade());
            //tbasePurchaseConfig.setConditionLimitAmount(tbasePurchaseConfigVo.getConditionLimitAmount());
            //tbasePurchaseConfig.setConditionMaxAmount(tbasePurchaseConfigVo.getConditionMaxAmount());
            tbasePurchaseConfig.setRewardIntegral(tbasePurchaseConfigVo.getRewardIntegral());
            //tbasePurchaseConfig.setRewardLevel(tbasePurchaseConfigVo.getRewardLevel());
            tbasePurchaseConfig.setRewardMoney(tbasePurchaseConfigVo.getRewardMoney());
            //tbasePurchaseConfig.setRewardPeas(tbasePurchaseConfigVo.getRewardPeas());
            tbasePurchaseConfigService.update(tbasePurchaseConfig);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }


    @RequestMapping("flushIntegralConfig")
    @ResponseBody
    @Transient
    public Object flushIntegralConfig(@RequestBody List<TbaseIntegralConfigVo> tbaseIntegralConfigs)throws Exception{
        for(TbaseIntegralConfigVo tbaseIntegralConfigVo:tbaseIntegralConfigs){
            TbaseIntegralConfig tbaseIntegralConfig = tbaseIntegralConfigService.findOne(tbaseIntegralConfigVo.getId());
            //tbaseIntegralConfig.setAmount(tbaseIntegralConfigVo.getAmount());
            //tbaseIntegralConfig.setGradle(tbaseIntegralConfigVo.getGradle());
            tbaseIntegralConfig.setIntegral(tbaseIntegralConfigVo.getIntegral());
            tbaseIntegralConfigService.update(tbaseIntegralConfig);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }


    @RequestMapping("flushGradeConfig")
    @ResponseBody
    @Transient
    public Object flushGradeConfig(@RequestBody List<TbaseGradeConfigVo> tbaseGradeConfigVos)throws Exception{
        for(TbaseGradeConfigVo tbaseGradeConfigVo:tbaseGradeConfigVos){
            TbaseGradeConfig tbaseGradeConfig = tbaseGradeConfigService.findOne(tbaseGradeConfigVo.getId());
            //tbaseGradeConfig.setGrade(tbaseGradeConfigVo.getGrade());
            tbaseGradeConfig.setIntegral(tbaseGradeConfigVo.getIntegral());
            tbaseGradeConfigService.update(tbaseGradeConfig);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
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

    @RequestMapping("orderList")
    @ResponseBody
    public Object orders(HttpServletRequest httpServletRequest){
        Client client = UserContext.get();
        TbaseUserinfo userInfo = tbaseUserinfoService.findOne(client.getUserId());
        Integer role = 0;
        if(CommonUtil.isNotEmpty(userInfo.getRole())){
            role = userInfo.getRole();
        }

        if(role.intValue()==1){
            StringBuffer sb = new StringBuffer();
            sb.append("select order_id as id,order_name as name,order_amount as amount,order_account as account from tapp_order  limit 0 ,100");
            return tappOrderService.findAll(OrderVo.class,sb.toString());
        }else {
            return tappImportTaskService.findAllOrders(userInfo.getUsername(), 0, 100);
        }
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
        TbaseUserinfo userInfo = tbaseUserinfoService.findOne(client.getUserId());
        Integer role = 0;
        if(CommonUtil.isNotEmpty(userInfo.getRole())){
            role = userInfo.getRole();
        }

        List<SubordinateVo> subordinates = tappSubordinateService.subordinates(client.getUserId());
        Map<String,Object> response = new HashMap<>();
        response.put("success",true);
        response.put("data",subordinates);
        return response;

    }


    @RequestMapping("userinfos")
    @ResponseBody
    public Object userinfos(HttpServletRequest httpServletRequest){
        String name = httpServletRequest.getParameter("name");
        String phonenumber = httpServletRequest.getParameter("phonenumber");
        String size = httpServletRequest.getParameter("size");
        String page = httpServletRequest.getParameter("page");

        Integer sizeInt = 0;
        Integer pageInt = 100;

        if(CommonUtil.isNotEmpty(size)){
            sizeInt = Integer.valueOf(size);
        }

        if(CommonUtil.isNotEmpty(page)){
            pageInt = Integer.valueOf(page);
        }
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC,"userinfo_id"));
        Pageable pageable = new PageRequest(pageInt,sizeInt);
        if(CommonUtil.isEmpty(name)&&CommonUtil.isEmpty(phonenumber)){
            Page data = tbaseUserinfoService.findAll(pageable);
            Map<String,Object> response = new HashMap<>();
            response.put("success",true);
            response.put("data",data);
            return response;
        }else {
            TbaseUserinfoDto tbaseUserinfoDto = new TbaseUserinfoDto();
            if(CommonUtil.isNotEmpty(name)) {
                tbaseUserinfoDto.setName(name);
            }

            if(CommonUtil.isNotEmpty(phonenumber)){
                tbaseUserinfoDto.setPhonenumber(phonenumber);
            }
            Page data =  tbaseUserinfoService.findAll(tbaseUserinfoDto,pageable);
            Map<String,Object> response = new HashMap<>();
            response.put("success",true);
            response.put("data",data);
            return response;
        }

    }

    @RequestMapping("updatePassword")
    @ResponseBody
    public Object updatePassword(HttpServletRequest httpServletRequest){
        String newPassword = httpServletRequest.getParameter("new_password");
        String oldPassword = httpServletRequest.getParameter("old_password");
        Client client = UserContext.get();
        TbaseUserinfo tbaseUserinfo = tbaseUserinfoService.findOne(client.getUserId());
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
