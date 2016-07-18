package com.cmcc.entity;

import java.io.Serializable;

public class UserRunAs implements Serializable{
    private Long fromUserId;    //授予身份账号
    private Long toUserId;      //被授予身份账号
    
    public Long getFromUserId() {
        return fromUserId;
    }
    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }
    public Long getToUserId() {
        return toUserId;
    }
    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        UserRunAs userRunAs = (UserRunAs) obj;
        
        //下面两个if的意思就是要想UserRunAs对象相等，那么其中的fromUserId和toUserId两个属性必须都相等
        //fromUserId和obj的fromUserId判断是否相等
        if(fromUserId != null ? !fromUserId.equals(userRunAs.fromUserId) : userRunAs.fromUserId != null) {
            return false;
        }
        //toUserId和obj的toUserId判断是否相等
        if(toUserId != null ? !toUserId.equals(userRunAs.toUserId) : userRunAs.toUserId != null) {
            return false;
        }

        return true;
    }
    
    @Override
    public int hashCode() {
        int result = fromUserId != null ? fromUserId.hashCode() : 0;
        result = 31 * result + (toUserId != null ? toUserId.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "UserRunAs{" +
                "fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                '}';
    }
}
