package com.example.headachediary.domain.headache;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NoteResponse {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("is_headache")
    @Expose
    private Boolean isHeadache;
    @SerializedName("headache_time")
    @Expose
    private String headacheTime;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("intensity")
    @Expose
    private Integer intensity;
    @SerializedName("headache_type")
    @Expose
    private List<String> headacheType;
    @SerializedName("triggers")
    @Expose
    private List<String> triggers;
    @SerializedName("area")
    @Expose
    private List<String> area;
    @SerializedName("symptoms")
    @Expose
    private List<String> symptoms;
    @SerializedName("medicine")
    @Expose
    private List<Medicine> medicine;
    @SerializedName("pressure_morning_up")
    @Expose
    private Integer pressureMorningUp;
    @SerializedName("pressure_morning_down")
    @Expose
    private Integer pressureMorningDown;
    @SerializedName("pressure_evening_up")
    @Expose
    private Integer pressureEveningUp;
    @SerializedName("pressure_evening_down")
    @Expose
    private Integer pressureEveningDown;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getIsHeadache() {
        return isHeadache;
    }

    public void setIsHeadache(Boolean isHeadache) {
        this.isHeadache = isHeadache;
    }

    public String getHeadacheTime() {
        return headacheTime;
    }

    public void setHeadacheTime(String headacheTime) {
        this.headacheTime = headacheTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getIntensity() {
        return intensity;
    }

    public void setIntensity(Integer intensity) {
        this.intensity = intensity;
    }

    public List<String> getHeadacheType() {
        return headacheType;
    }

    public void setHeadacheType(List<String> headacheType) {
        this.headacheType = headacheType;
    }

    public List<String> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<String> triggers) {
        this.triggers = triggers;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    public List<Medicine> getMedicine() {
        return medicine;
    }

    public void setMedicine(List<Medicine> medicine) {
        this.medicine = medicine;
    }

    public Integer getPressureMorningUp() {
        return pressureMorningUp;
    }

    public void setPressureMorningUp(Integer pressureMorningUp) {
        this.pressureMorningUp = pressureMorningUp;
    }

    public Integer getPressureMorningDown() {
        return pressureMorningDown;
    }

    public void setPressureMorningDown(Integer pressureMorningDown) {
        this.pressureMorningDown = pressureMorningDown;
    }

    public Integer getPressureEveningUp() {
        return pressureEveningUp;
    }

    public void setPressureEveningUp(Integer pressureEveningUp) {
        this.pressureEveningUp = pressureEveningUp;
    }

    public Integer getPressureEveningDown() {
        return pressureEveningDown;
    }

    public void setPressureEveningDown(Integer pressureEveningDown) {
        this.pressureEveningDown = pressureEveningDown;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(NoteResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("date");
        sb.append('=');
        sb.append(((this.date == null)?"<null>":this.date));
        sb.append(',');
        sb.append("isHeadache");
        sb.append('=');
        sb.append(((this.isHeadache == null)?"<null>":this.isHeadache));
        sb.append(',');
        sb.append("headacheTime");
        sb.append('=');
        sb.append(((this.headacheTime == null)?"<null>":this.headacheTime));
        sb.append(',');
        sb.append("duration");
        sb.append('=');
        sb.append(((this.duration == null)?"<null>":this.duration));
        sb.append(',');
        sb.append("intensity");
        sb.append('=');
        sb.append(((this.intensity == null)?"<null>":this.intensity));
        sb.append(',');
        sb.append("headacheType");
        sb.append('=');
        sb.append(((this.headacheType == null)?"<null>":this.headacheType));
        sb.append(',');
        sb.append("triggers");
        sb.append('=');
        sb.append(((this.triggers == null)?"<null>":this.triggers));
        sb.append(',');
        sb.append("area");
        sb.append('=');
        sb.append(((this.area == null)?"<null>":this.area));
        sb.append(',');
        sb.append("symptoms");
        sb.append('=');
        sb.append(((this.symptoms == null)?"<null>":this.symptoms));
        sb.append(',');
        sb.append("medicine");
        sb.append('=');
        sb.append(((this.medicine == null)?"<null>":this.medicine));
        sb.append(',');
        sb.append("pressureMorningUp");
        sb.append('=');
        sb.append(((this.pressureMorningUp == null)?"<null>":this.pressureMorningUp));
        sb.append(',');
        sb.append("pressureMorningDown");
        sb.append('=');
        sb.append(((this.pressureMorningDown == null)?"<null>":this.pressureMorningDown));
        sb.append(',');
        sb.append("pressureEveningUp");
        sb.append('=');
        sb.append(((this.pressureEveningUp == null)?"<null>":this.pressureEveningUp));
        sb.append(',');
        sb.append("pressureEveningDown");
        sb.append('=');
        sb.append(((this.pressureEveningDown == null)?"<null>":this.pressureEveningDown));
        sb.append(',');
        sb.append("comment");
        sb.append('=');
        sb.append(((this.comment == null)?"<null>":this.comment));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("userId");
        sb.append('=');
        sb.append(((this.userId == null)?"<null>":this.userId));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.date == null)? 0 :this.date.hashCode()));
        result = ((result* 31)+((this.area == null)? 0 :this.area.hashCode()));
        result = ((result* 31)+((this.headacheType == null)? 0 :this.headacheType.hashCode()));
        result = ((result* 31)+((this.pressureEveningUp == null)? 0 :this.pressureEveningUp.hashCode()));
        result = ((result* 31)+((this.medicine == null)? 0 :this.medicine.hashCode()));
        result = ((result* 31)+((this.triggers == null)? 0 :this.triggers.hashCode()));
        result = ((result* 31)+((this.userId == null)? 0 :this.userId.hashCode()));
        result = ((result* 31)+((this.duration == null)? 0 :this.duration.hashCode()));
        result = ((result* 31)+((this.intensity == null)? 0 :this.intensity.hashCode()));
        result = ((result* 31)+((this.symptoms == null)? 0 :this.symptoms.hashCode()));
        result = ((result* 31)+((this.headacheTime == null)? 0 :this.headacheTime.hashCode()));
        result = ((result* 31)+((this.pressureMorningDown == null)? 0 :this.pressureMorningDown.hashCode()));
        result = ((result* 31)+((this.isHeadache == null)? 0 :this.isHeadache.hashCode()));
        result = ((result* 31)+((this.comment == null)? 0 :this.comment.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.pressureEveningDown == null)? 0 :this.pressureEveningDown.hashCode()));
        result = ((result* 31)+((this.pressureMorningUp == null)? 0 :this.pressureMorningUp.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof NoteResponse) == false) {
            return false;
        }
        NoteResponse rhs = ((NoteResponse) other);
        return ((((((((((((((((((this.date == rhs.date)||((this.date!= null)&&this.date.equals(rhs.date)))&&((this.area == rhs.area)||((this.area!= null)&&this.area.equals(rhs.area))))&&((this.headacheType == rhs.headacheType)||((this.headacheType!= null)&&this.headacheType.equals(rhs.headacheType))))&&((this.pressureEveningUp == rhs.pressureEveningUp)||((this.pressureEveningUp!= null)&&this.pressureEveningUp.equals(rhs.pressureEveningUp))))&&((this.medicine == rhs.medicine)||((this.medicine!= null)&&this.medicine.equals(rhs.medicine))))&&((this.triggers == rhs.triggers)||((this.triggers!= null)&&this.triggers.equals(rhs.triggers))))&&((this.userId == rhs.userId)||((this.userId!= null)&&this.userId.equals(rhs.userId))))&&((this.duration == rhs.duration)||((this.duration!= null)&&this.duration.equals(rhs.duration))))&&((this.intensity == rhs.intensity)||((this.intensity!= null)&&this.intensity.equals(rhs.intensity))))&&((this.symptoms == rhs.symptoms)||((this.symptoms!= null)&&this.symptoms.equals(rhs.symptoms))))&&((this.headacheTime == rhs.headacheTime)||((this.headacheTime!= null)&&this.headacheTime.equals(rhs.headacheTime))))&&((this.pressureMorningDown == rhs.pressureMorningDown)||((this.pressureMorningDown!= null)&&this.pressureMorningDown.equals(rhs.pressureMorningDown))))&&((this.isHeadache == rhs.isHeadache)||((this.isHeadache!= null)&&this.isHeadache.equals(rhs.isHeadache))))&&((this.comment == rhs.comment)||((this.comment!= null)&&this.comment.equals(rhs.comment))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.pressureEveningDown == rhs.pressureEveningDown)||((this.pressureEveningDown!= null)&&this.pressureEveningDown.equals(rhs.pressureEveningDown))))&&((this.pressureMorningUp == rhs.pressureMorningUp)||((this.pressureMorningUp!= null)&&this.pressureMorningUp.equals(rhs.pressureMorningUp))));
    }

}