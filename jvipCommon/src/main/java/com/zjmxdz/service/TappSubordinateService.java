package com.zjmxdz.service;
import com.wyc.common.service.BaseAbstractService;
import com.zjmxdz.dao.TappSubordinateDao;
import com.zjmxdz.domain.TappSubordinate;
import com.zjmxdz.domain.vo.SubordinateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TappSubordinateService extends BaseAbstractService<TappSubordinate> {

    @Autowired
    private TappSubordinateDao tappSubordinateDao;
    @Autowired
    public TappSubordinateService(TappSubordinateDao tappSubordinateDao) {
        super(tappSubordinateDao);
    }

    public List<SubordinateVo> subordinates(String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select subordinate.subordinate_id id," +
                "subordinate.subordinate_level level," +
                "subordinate.subordinate_suborinateuserid subordinateUserid," +
                "subordinateUserInfo.userinfo_name subordinateUsername," +
                "subordinateUserInfo.userinfo_amount subordinateAccount," +
                "userinfo.userinfo_id userid," +
                "userinfo.userinfo_name name," +

                "subordinate.subordinate_refereeuserid refereeUserId," +
                "subordinate.subordinate_refereename refereeName," +

                "subordinateUserInfo.userinfo_grade subordinatGrade," +

                "subordinateUserInfo.userinfo_totalamount totalAmount," +

                "subordinateUserInfo.userinfo_phonenumber subordinatephonenumber," +
                "subordinateUserInfo.userinfo_integral subordinateintegral," +
                "userinfo.userinfo_invitationnum invitationnum," +
                "userinfo.userinfo_amount amount from tapp_subordinate subordinate," +
                "tbase_userinfo subordinateUserInfo,tbase_userinfo userinfo where " +
                "subordinate.subordinate_suborinateuserid=subordinateUserInfo.userinfo_id and " +
                "subordinate.subordinate_userid=userinfo.userinfo_id and " +
                "userinfo.userinfo_id='"+userId+"'");
        List<SubordinateVo> subordinates = findAll(SubordinateVo.class,sb.toString());
        return subordinates;
    }
}
