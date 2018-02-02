package com.gtafe.experimental.bean;


/**
 * Created by ZhouJF on 2017/9/22.
 */

public class DataBean {


    private int lampLight;   //灯光强度
    private int sunLight;//太阳光强度

    private boolean safeState;//安全模式

    public boolean isCalling() {
        return calling;
    }

    public void setCalling(boolean calling) {
        this.calling = calling;
    }


    private int smartLightL;//智能灯打开阀值
    private int smartLightH;//智能灯关闭阀值


    private int powerLimit;//智能灯关闭阀值
    private boolean calling;//安全模式


    private double temperature;//温度
    private double humidity;//湿度
    private boolean smartLightState;//智能灯光
    private int currentState;//窗帘
    private int chazuoState;//插座状态
    private int fengshan;//插座状态
    private double electricity;//电压
    private double electricity_all;//电压
    private double votage_all;//电压
    private int power;//功率
    private boolean isCurtainAuto;//窗帘
    private int safe_gas;//燃气
    private int safe_smoke;//烟气
    private int safe_flame;//火焰
    private int safe_infrared;//红外
    private int safe_voice;//语音报警

    public int getFengshan() {
        return fengshan;
    }

    public void setFengshan(int fengshan) {
        this.fengshan = fengshan;
    }

    public int getSmartLightL() {
        return smartLightL;
    }

    public void setSmartLightL(int smartLightL) {
        this.smartLightL = smartLightL;
    }

    public int getSmartLightH() {
        return smartLightH;
    }

    public void setSmartLightH(int smartLightH) {
        this.smartLightH = smartLightH;
    }

    public int getPower() {
        return power;
    }

    public boolean setPower(int power) {
        if (this.power == power)
            return true;
        this.power = power;
        return false;

    }
    public int getChazuoState() {
        return chazuoState;
    }

    public boolean setChazuoState(int chazuoState) {
        if (this.chazuoState == chazuoState)
            return true;
        this.chazuoState = chazuoState;
        return false;
    }

    public double getElectricity_all() {
        return electricity_all;
    }

    public void setElectricity_all(double electricity_all) {
        this.electricity_all = electricity_all;
    }

    public double getVotage_all() {
        return votage_all;
    }

    public void setVotage_all(double votage_all) {
        this.votage_all = votage_all;
    }
    public boolean isSafeState() {
        return safeState;
    }

    public void setSafeState(boolean safeState) {
        this.safeState = safeState;
    }

    public int getLampLight() {
        return lampLight;
    }

    public boolean setLampLight(int lampLight) {
        if (this.lampLight == lampLight)
            return true;
        this.lampLight = lampLight;
        return false;
    }

    public int getSunLight() {
        return sunLight;
    }

    public boolean setSunLight(int sunLight) {
        if (this.sunLight == sunLight)
            return true;
        this.sunLight = sunLight;
        return false;
    }

    public double getTemperature() {
        return temperature;
    }

    public boolean setTemperature(double temperature) {
        if (this.temperature == temperature)
            return true;
        this.temperature = temperature;
        return false;
    }

    public double getHumidity() {
        return humidity;
    }

    public boolean setHumidity(double humidity) {
        if (this.humidity == humidity)
            return true;
        this.humidity = humidity;
        return false;
    }

    public boolean isSmartLightState() {
        return smartLightState;
    }

    public boolean setSmartLightState(boolean smartLightState) {
        if (this.smartLightState == smartLightState)
            return true;
        this.smartLightState = smartLightState;
        return false;
    }
    public int getPowerLimit() {
        return powerLimit;
    }

    public void setPowerLimit(int powerLimit) {
        this.powerLimit = powerLimit;
    }

    public int getCurrentState() {
        return currentState;
    }

    public boolean setCurrentState(int currentState) {
        if (this.currentState == currentState)
            return true;
        this.currentState = currentState;
        return false;
    }

    public double getElectricity() {
        return electricity;
    }

    public boolean setElectricity(double electricity) {
        if (this.electricity == electricity)
            return true;
        this.electricity = electricity;
        return false;
    }

    public boolean isCurtainAuto() {
        return isCurtainAuto;
    }

    public void setCurtainAuto(boolean curtainAuto) {
        isCurtainAuto = curtainAuto;
    }

    public int getSafe_gas() {
        return safe_gas;
    }

    public boolean setSafe_gas(int safe_gas) {
        if (this.safe_gas == safe_gas)
            return true;
        this.safe_gas = safe_gas;
        return false;
    }

    public int getSafe_smoke() {
        return safe_smoke;
    }

    public boolean setSafe_smoke(int safe_smoke) {
        if (this.safe_smoke == safe_smoke)
            return true;
        this.safe_smoke = safe_smoke;
        return false;
    }

    public int getSafe_flame() {
        return safe_flame;
    }

    public boolean setSafe_flame(int safe_flame) {
        if (this.safe_flame == safe_flame)
            return true;
        this.safe_flame = safe_flame;
        return false;
    }

    public int getSafe_infrared() {
        return safe_infrared;
    }

    public boolean setSafe_infrared(int safe_infrared) {
        if (this.safe_infrared == safe_infrared)
            return true;
        this.safe_infrared = safe_infrared;
        return false;
    }

    public int getSafe_voice() {
        return safe_voice;
    }

    public boolean setSafe_voice(int safe_voice) {
        if (this.safe_voice == safe_voice)
            return true;
        this.safe_voice = safe_voice;
        return false;
    }
}
