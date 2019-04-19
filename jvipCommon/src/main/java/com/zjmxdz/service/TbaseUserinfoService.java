package com.zjmxdz.service;
import com.wyc.common.service.BaseAbstractService;
import com.zjmxdz.dao.TbaseUserinfoDao;
import com.zjmxdz.domain.TbaseUserinfo;
import com.zjmxdz.domain.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        sb.append("userinfo_name as name,userinfo_refereeeuserid as refereeUserId,userinfo_integral as integral,");
        sb.append("userinfo_grade as grade,");
        sb.append("userinfo_peas as peas from tbase_userinfo userinfo where userinfo.userinfo_id="+userId);
        UserInfoVo userInfoVo = findOne(UserInfoVo.class,sb.toString());
        return userInfoVo;
    }
}
