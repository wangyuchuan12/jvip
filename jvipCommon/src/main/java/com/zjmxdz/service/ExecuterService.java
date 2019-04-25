package com.zjmxdz.service;

import com.wyc.common.util.CommonUtil;
import com.wyc.common.util.ExcelUtil;
import com.zjmxdz.domain.*;
import com.zjmxdz.domain.dto.*;
import com.zjmxdz.domain.vo.ImportOrderData;
import com.zjmxdz.domain.vo.ImportUserData;
import com.zjmxdz.exception.UserInfoNullException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExecuterService {

    @Autowired
    private TappSubordinateService tappSubordinateService;

    @Autowired
    private TappOrderService tappOrderService;

    @Autowired
    private TbaseUserinfoService tbaseUserinfoService;

    @Autowired
    private TappImportTaskService tappImportTaskService;

    @Autowired
    private TbasePurchaseConfigService tbasePurchaseConfigService;

    @Autowired
    private TbaseGradeConfigService tbaseGradeConfigService;

    @Value("${userFileDir}")
    private String userFileDir;

    @Value("${orderFileDir}")
    private String orderFileDir;

    public void importAllTask(){
        TappImportTaskDto tappImportTaskDto = new TappImportTaskDto();
        tappImportTaskDto.setStatus(0);
        List<TappImportTask> tappImportTasks = tappImportTaskService.findAll(tappImportTaskDto);
        for(TappImportTask tappImportTask:tappImportTasks){
            try{
                importTask(tappImportTask);
            }catch (Exception e){
                try {
                    tappImportTask.setStatus(2);
                    tappImportTaskService.update(tappImportTask);
                }catch (Exception e2){

                }
            }
        }
    }

    public void countGrade(TbaseUserinfo tbaseUserinfo){
        TbaseGradeConfig tbaseGradeConfig = tbaseGradeConfigService.getMaxByIntegral(tbaseUserinfo.getIntegral());
        if(CommonUtil.isNotEmpty(tbaseGradeConfig)){
            Integer grade = tbaseUserinfo.getGrade();
            tbaseUserinfo.setGrade(grade);
            try {
                tbaseUserinfoService.update(tbaseUserinfo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    public void countReward(TappOrder tappOrder)throws UserInfoNullException{

        String account = tappOrder.getAccount();
        String refereeAccount = tappOrder.getRefereeAccount();
        BigDecimal amount = tappOrder.getAmount();

        TbaseUserinfoDto userinfoDto = new TbaseUserinfoDto();
        userinfoDto.setUsername(account);
        TbaseUserinfo tbaseUserinfo = tbaseUserinfoService.findOne(userinfoDto);

        if(CommonUtil.isEmpty(tbaseUserinfo)){
            throw new UserInfoNullException();
        }

        BigDecimal userAmount =tbaseUserinfo.getTotalAmount();

        if(CommonUtil.isEmpty(amount)){
            amount = new BigDecimal(0);
        }

        if(CommonUtil.isEmpty(userAmount)){
            userAmount = new BigDecimal(0);
        }

        userAmount = userAmount.add(amount);

        tbaseUserinfo.setTotalAmount(userAmount);

        try {
            tbaseUserinfoService.update(tbaseUserinfo);
        }catch (Exception e){
            e.printStackTrace();
        }



        TbasePurchaseConfigDto tbasePurchaseConfigDto = new TbasePurchaseConfigDto();

        List<Integer> conditionGradeIn = new ArrayList<>();
        conditionGradeIn.add(0);
        conditionGradeIn.add(tbaseUserinfo.getGrade());
        tbasePurchaseConfigDto.setCONDITION_GRADE_IN(conditionGradeIn);
        tbasePurchaseConfigDto.setCONDITION_LIMIT_AMOUNT(amount);
        tbasePurchaseConfigDto.setCONDITION_MAX_AMOUNT(amount);

        List<TbasePurchaseConfig> tbasePurchaseConfigs = tbasePurchaseConfigService.findAll(tbasePurchaseConfigDto);

        for(TbasePurchaseConfig tbasePurchaseConfig:tbasePurchaseConfigs){

            Integer level = tbasePurchaseConfig.getRewardLevel();

            TappSubordinateDto tappSubordinateDto = new TappSubordinateDto();
            tappSubordinateDto.setSubordinateUserid(tbaseUserinfo.getId());
            tappSubordinateDto.setLevel(level);
            List<TappSubordinate> tappSubordinates = tappSubordinateService.findAll(tappSubordinateDto);

            for(TappSubordinate tappSubordinate:tappSubordinates){
                TbaseUserinfo subordinateUserinfo = tbaseUserinfoService.findOne(tappSubordinate.getUserid());
                Integer grade = tbaseUserinfo.getGrade();
                Long integral = subordinateUserinfo.getIntegral();
                Long rewardIntegral = tbasePurchaseConfig.getRewardIntegral();
                if(CommonUtil.isEmpty(integral)){
                    integral = 0L;
                }

                if(CommonUtil.isEmpty(rewardIntegral)){
                    rewardIntegral = 0L;
                }
                subordinateUserinfo.setIntegral(integral+rewardIntegral);

                BigDecimal bigDecimal = subordinateUserinfo.getAmount();
                if(CommonUtil.isEmpty(bigDecimal)){
                    bigDecimal = new BigDecimal(0);
                }

                if(CommonUtil.isNotEmpty(tbasePurchaseConfig.getRewardMoney())) {
                    bigDecimal = bigDecimal.add(tbasePurchaseConfig.getRewardMoney());
                }

                Integer peas = subordinateUserinfo.getPeas();
                if(CommonUtil.isEmpty(peas)){
                    peas = 0;
                }

                if(CommonUtil.isNotEmpty(tbasePurchaseConfig.getRewardPeas())){
                    peas = peas+tbasePurchaseConfig.getRewardPeas();
                }

                subordinateUserinfo.setAmount(bigDecimal);
                subordinateUserinfo.setPeas(peas);

                try {
                    tbaseUserinfoService.update(subordinateUserinfo);
                    countGrade(subordinateUserinfo);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }
    }

    public void uploadUserTask(String path,String fileName) throws Exception {
        TappImportTask importTask = new TappImportTask();
        importTask.setStatus(0);
        importTask.setCreateAt(new Timestamp(new Date().getTime()));
        importTask.setFilename(fileName);
        importTask.setFilePath(path);
        importTask.setType(0);
        tappImportTaskService.add(importTask);
        importTask(importTask.getId());
    }

    public void uploadOrderTask(String path,String fileName) throws Exception {
        TappImportTask importTask = new TappImportTask();
        importTask.setStatus(0);
        importTask.setCreateAt(new Timestamp(new Date().getTime()));
        importTask.setFilename(fileName);
        importTask.setFilePath(path);
        importTask.setType(1);
        tappImportTaskService.add(importTask);
        importTask(importTask.getId());
    }

    public void importTask(String taskId)throws Exception{
        TappImportTask tappImportTask = tappImportTaskService.findOne(taskId);
        importTask(tappImportTask);
    }

    public void importTask(TappImportTask importTask)throws Exception{
        importTask.setStartTime(new Timestamp(new Date().getTime()));
        if(importTask.getType().intValue()==0){
            importUserData(importTask);
        }else if(importTask.getType().intValue()==1){
            importOrderData(importTask);
        }
        importTask.setStatus(1);
        importTask.setCompleteTime(new Timestamp(new Date().getTime()));
        tappImportTaskService.update(importTask);
    }

    public void flushSubordinate(TbaseUserinfo tbaseUserinfo,TbaseUserinfo refereeUserinfo,Integer level){
        if(CommonUtil.isEmpty(refereeUserinfo)){
            return;
        }
        TappSubordinate tappSubordinate = new TappSubordinate();
        tappSubordinate.setLevel(level);
        tappSubordinate.setSubordinateUserid(tbaseUserinfo.getId());
        tappSubordinate.setUserid(refereeUserinfo.getId());
        tappSubordinateService.add(tappSubordinate);

        if(CommonUtil.isNotEmpty(refereeUserinfo.getRefereeAccount())) {
            TbaseUserinfoDto newRefereeUserinfoDto = new TbaseUserinfoDto();
            newRefereeUserinfoDto.setUsername(refereeUserinfo.getRefereeAccount());
            TbaseUserinfo newRefereeUserinfo = tbaseUserinfoService.findOne(newRefereeUserinfoDto);
            tappSubordinate.setRefereeUserId(newRefereeUserinfo.getId());
            tappSubordinate.setRefereeName(newRefereeUserinfo.getName());
            try {
                tappSubordinateService.update(tappSubordinate);
            }catch (Exception e){
                e.printStackTrace();
            }

            if (CommonUtil.isNotEmpty(newRefereeUserinfo)) {
                level++;
                flushSubordinate(tbaseUserinfo, newRefereeUserinfo, level);
            }
        }
    }

    public void flushSubordinate(TbaseUserinfo tbaseUserinfo)throws Exception{
        TbaseUserinfoDto referencesUserinfoDto = new TbaseUserinfoDto();
        referencesUserinfoDto.setUsername(tbaseUserinfo.getRefereeAccount());
        TbaseUserinfo referencesUserinfo = tbaseUserinfoService.findOne(referencesUserinfoDto);
        flushSubordinate(tbaseUserinfo, referencesUserinfo, 1);
        tbaseUserinfo.setIsHierarchy(1);
        tbaseUserinfoService.update(tbaseUserinfo);
    }

    public void flushSubordinate(){
        TbaseUserinfoDto tbaseUserinfoDto = new TbaseUserinfoDto();
        tbaseUserinfoDto.setIsHierarchy(0);

        List<TbaseUserinfo> tbaseUserinfos = tbaseUserinfoService.findAll(tbaseUserinfoDto);

        for(TbaseUserinfo tbaseUserinfo:tbaseUserinfos){
            try {
                flushSubordinate(tbaseUserinfo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void importUserData(TappImportTask importTask)throws Exception{
        String file = importTask.getFilePath();
        FileInputStream fileInputStream = new FileInputStream(new File(file));
        List<ImportUserData> importUserDataList = ExcelUtil.read(ImportUserData.class,fileInputStream,0,10);
        for(ImportUserData importUserData:importUserDataList){
            TbaseUserinfoDto tbaseUserinfoDto = new TbaseUserinfoDto();
            tbaseUserinfoDto.setUsername(importUserData.getAccount());
            TbaseUserinfo tbaseUserinfo = tbaseUserinfoService.findOne(tbaseUserinfoDto);
            if(CommonUtil.isEmpty(tbaseUserinfo)) {
                tbaseUserinfo = new TbaseUserinfo();
                tbaseUserinfo.setUsername(importUserData.getAccount());
                tbaseUserinfo.setRefereeAccount(importUserData.getReferencesAccount());
                tbaseUserinfo.setAmount(new BigDecimal(0));
                tbaseUserinfo.setName(importUserData.getName());
                tbaseUserinfo.setIntegral(0L);
                tbaseUserinfo.setInvitationnum(0);
                tbaseUserinfo.setGrade(0);
                tbaseUserinfo.setPeas(0);
                tbaseUserinfo.setIsHierarchy(0);
                tbaseUserinfo.setPassword("1234567");
                tbaseUserinfo.setTaskId(importTask.getId());
                tbaseUserinfo.setPhonenumber(importUserData.getAccount());
                tbaseUserinfoService.add(tbaseUserinfo);
            }
        }

        flushSubordinate();
    }


    public void importOrderData(TappImportTask importTask)throws Exception{
        String file = importTask.getFilePath();
        FileInputStream fileInputStream = new FileInputStream(new File(file));
        List<ImportOrderData> importOrderDatas = new ArrayList<>();
        importOrderDatas.addAll(ExcelUtil.read(ImportOrderData.class,fileInputStream,0,5));
        for(ImportOrderData importOrderData:importOrderDatas){
            TbaseUserinfoDto tbaseUserinfoDto = new TbaseUserinfoDto();
            tbaseUserinfoDto.setUsername(importOrderData.getAccount());
            TbaseUserinfo tbaseUserinfo = tbaseUserinfoService.findOne(tbaseUserinfoDto);
            TappOrder tappOrder = new TappOrder();
            if(CommonUtil.isNotEmpty(tbaseUserinfo)){
                tappOrder.setName(tbaseUserinfo.getName());
            }
            tappOrder.setAmount(importOrderData.getAmount());
            tappOrder.setRefereeAccount(importOrderData.getReferencesId());
            tappOrder.setAccount(importOrderData.getAccount());
            tappOrder.setStatus(0);
            tappOrder.setTaskId(importTask.getId());;
            tappOrderService.add(tappOrder);
            try {
                countReward(tappOrder);
                tappOrder.setStatus(1);
            }catch (UserInfoNullException e){
                tappOrder.setStatus(2);
                tappOrder.setErrortype(0);
                tappOrder.setRemarks("没有导入用户");
            }

            tappOrderService.update(tappOrder);
        }
    }
}
