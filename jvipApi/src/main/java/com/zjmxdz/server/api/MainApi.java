package com.zjmxdz.server.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.wyc.common.annotation.Auth;
import com.wyc.common.context.UserContext;
import com.wyc.common.domain.Client;
import com.wyc.common.service.ClientService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.util.ExcelUtil;
import com.zjmxdz.domain.TappImportTask;
import com.zjmxdz.domain.TbaseUserinfo;
import com.zjmxdz.domain.dto.TbaseUserinfoDto;
import com.zjmxdz.domain.vo.PurchaseRecordVo;
import com.zjmxdz.domain.vo.SubordinateVo;
import com.zjmxdz.domain.vo.UserInfoVo;
import com.zjmxdz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${usercenter.token.expires}")
    private Long expires;

    @RequestMapping("importTasks")
    @ResponseBody
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

    @RequestMapping("taskList")
    @ResponseBody
    public Object tasks(HttpServletRequest httpServletRequest){
        return tappImportTaskService.findAllTasks();
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
