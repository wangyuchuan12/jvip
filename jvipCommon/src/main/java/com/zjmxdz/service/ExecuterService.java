package com.zjmxdz.service;

import com.wyc.common.util.CommonUtil;
import com.wyc.common.util.ExcelUtil;
import com.zjmxdz.domain.*;
import com.zjmxdz.domain.dto.TappImportTaskDto;
import com.zjmxdz.domain.dto.TbasePurchaseConfigDto;
import com.zjmxdz.domain.dto.TbaseUserinfoDto;
import com.zjmxdz.domain.vo.ImportOrderData;
import com.zjmxdz.domain.vo.ImportUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExecuterService {
    @Autowired
    private TappPurchaseRecordService tappPurchaseRecordService;

    @Autowired
    private TappSubordinateService tappSubordinateService;

    @Autowired
    private TappOrderService tappOrderService;

    @Autowired
    private TbaseUserinfoService tbaseUserinfoService;

    @Autowired
    private TappImportTaskService tappImportTaskService;

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


    public void countReward(TappOrder tappOrder){

        String account = tappOrder.getAccount();
        String refereeAccount = tappOrder.getRefereeAccount();
        BigDecimal amount = tappOrder.getAmount();

        TbaseUserinfoDto userinfoDto = new TbaseUserinfoDto();
        userinfoDto.setUsername(account);
        TbaseUserinfo tbaseUserinfo = tbaseUserinfoService.findOne(userinfoDto);

        TbasePurchaseConfigDto tbasePurchaseConfigDto = new TbasePurchaseConfigDto();

        List<Integer> conditionGradeIn = new ArrayList<>();
        conditionGradeIn.add(0);
        conditionGradeIn.add(tbaseUserinfo.getGrade());
        tbasePurchaseConfigDto.setCONDITION_GRADE_IN(conditionGradeIn);

    }

    public void importTask(TappImportTask importTask)throws Exception{
        if(importTask.getType().intValue()==0){
            String file = importTask.getFilePath();
            FileInputStream fileInputStream = new FileInputStream(new File(file));
            importUserData(fileInputStream);
        }else if(importTask.getType().intValue()==1){
            String file = importTask.getFilePath();
            FileInputStream fileInputStream = new FileInputStream(new File(file));
            importOrderData(fileInputStream);
        }
        importTask.setStatus(1);
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

    public void importUserData(InputStream inputStream)throws Exception{
        List<ImportUserData> importUserDataList = ExcelUtil.read(ImportUserData.class,inputStream,0,10);
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
                tbaseUserinfoService.add(tbaseUserinfo);
            }
        }

        flushSubordinate();
    }


    public void importOrderData(InputStream inputStream)throws Exception{
        List<ImportOrderData> importOrderDatas = new ArrayList<>();
        importOrderDatas.addAll(ExcelUtil.read(ImportOrderData.class,inputStream,0,5));
        for(ImportOrderData importOrderData:importOrderDatas){
            TappOrder tappOrder = new TappOrder();
            tappOrder.setAmount(importOrderData.getAmount());
            tappOrder.setRefereeAccount(importOrderData.getReferencesId());
            tappOrder.setAccount(importOrderData.getAccount());
            tappOrderService.add(tappOrder);
            countReward(tappOrder);
        }
    }
}
