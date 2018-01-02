package com.gtafe.experimental.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by ZhouJF on 2017/12/20.
 */
@Entity
public class UserBean {

    @Id
    private Long id;
    private String name;
    private String type;
    private String password;
    private String rfid;
    private String fingerprint;
    @Generated(hash = 1454619932)
    public UserBean(Long id, String name, String type, String password, String rfid,
            String fingerprint) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.password = password;
        this.rfid = rfid;
        this.fingerprint = fingerprint;
    }
    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRfid() {
        return this.rfid;
    }
    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
    public String getFingerprint() {
        return this.fingerprint;
    }
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }



}
