package com.example.headachediary.domain.statistics;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeStats {

    @SerializedName("night")
    @Expose
    private Integer night;
    @SerializedName("morning")
    @Expose
    private Integer morning;
    @SerializedName("afternoon")
    @Expose
    private Integer afternoon;
    @SerializedName("evening")
    @Expose
    private Integer evening;

    public Integer getNight() {
        return night;
    }

    public void setNight(Integer night) {
        this.night = night;
    }

    public Integer getMorning() {
        return morning;
    }

    public void setMorning(Integer morning) {
        this.morning = morning;
    }

    public Integer getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(Integer afternoon) {
        this.afternoon = afternoon;
    }

    public Integer getEvening() {
        return evening;
    }

    public void setEvening(Integer evening) {
        this.evening = evening;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TimeStats.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("night");
        sb.append('=');
        sb.append(((this.night == null)?"<null>":this.night));
        sb.append(',');
        sb.append("morning");
        sb.append('=');
        sb.append(((this.morning == null)?"<null>":this.morning));
        sb.append(',');
        sb.append("afternoon");
        sb.append('=');
        sb.append(((this.afternoon == null)?"<null>":this.afternoon));
        sb.append(',');
        sb.append("evening");
        sb.append('=');
        sb.append(((this.evening == null)?"<null>":this.evening));
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
        result = ((result* 31)+((this.afternoon == null)? 0 :this.afternoon.hashCode()));
        result = ((result* 31)+((this.evening == null)? 0 :this.evening.hashCode()));
        result = ((result* 31)+((this.night == null)? 0 :this.night.hashCode()));
        result = ((result* 31)+((this.morning == null)? 0 :this.morning.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TimeStats) == false) {
            return false;
        }
        TimeStats rhs = ((TimeStats) other);
        return (((((this.afternoon == rhs.afternoon)||((this.afternoon!= null)&&this.afternoon.equals(rhs.afternoon)))&&((this.evening == rhs.evening)||((this.evening!= null)&&this.evening.equals(rhs.evening))))&&((this.night == rhs.night)||((this.night!= null)&&this.night.equals(rhs.night))))&&((this.morning == rhs.morning)||((this.morning!= null)&&this.morning.equals(rhs.morning))));
    }

}
