package com.zjmxdz.service;
import com.wyc.common.service.BaseAbstractService;
import com.zjmxdz.dao.TbaseUserinfoDao;
import com.zjmxdz.domain.TbaseUserinfo;
import com.zjmxdz.domain.dto.TbaseUserinfoDto;
import com.zjmxdz.domain.vo.GradleNumVo;
import com.zjmxdz.domain.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TbaseUserinfoService extends BaseAbstractService<TbaseUserinfo>{
    @Autowired
    private TbaseUserinfoDao tbaseUserinfoDao;
    @Autowired
    public TbaseUserinfoService(TbaseUserinfoDao tbaserUserinfoDao) {
        super(tbaserUserinfoDao);
    }

    public UserInfoVo userInfo(String userId) {

        StringBuffer sb = new StringBuffer();
        sb.append("select userinfo_id as id,userinfo_username as username,userinfo_phonenumber as phonenumber,");
        sb.append("userinfo_name as name,userinfo_refereeeuseraccount as refereeAccount,userinfo_integral as integral,");
        sb.append("userinfo_grade as grade,");
        sb.append("userinfo_amount as amount,");
        sb.append("userinfo_totalamount as totalAmount,");
        sb.append("userinfo_peas as peas from tbase_userinfo userinfo where userinfo.userinfo_id='"+userId+"'");
        UserInfoVo userInfoVo = findOne(UserInfoVo.class,sb.toString());

        Object subordinateNum = getValue("select count(*) from tapp_subordinate where subordinate_userid='"+userId+"'");
        userInfoVo.setSubordinateNum(Integer.valueOf(subordinateNum.toString()));

        Object orderNum = getValue("select count(*) from tapp_order where order_account='"+userInfoVo.getUsername()+"'");
        userInfoVo.setOrderNum(Integer.valueOf(orderNum.toString()));
        List<GradleNumVo> gradleNums = findAll(GradleNumVo.class,"select count(*) as num,SUM(userinfo_totalamount) as amount,userinfo_grade as gradle from tbase_userinfo where userinfo_id in (select subordinate_suborinateuserid from tapp_subordinate where subordinate_userid = '"+userId+"') group by userinfo_grade;");
        userInfoVo.setGradleNums(gradleNums);
        return userInfoVo;
    }
}
