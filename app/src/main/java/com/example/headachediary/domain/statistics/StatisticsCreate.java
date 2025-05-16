package com.example.headachediary.domain.statistics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticsCreate {

    @SerializedName("date_start")
    @Expose
    private String dateStart;
    @SerializedName("date_end")
    @Expose
    private String dateEnd;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StatisticsCreate.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("dateStart");
        sb.append('=');
        sb.append(((this.dateStart == null)?"<null>":this.dateStart));
        sb.append(',');
        sb.append("dateEnd");
        sb.append('=');
        sb.append(((this.dateEnd == null)?"<null>":this.dateEnd));
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
        result = ((result* 31)+((this.dateStart == null)? 0 :this.dateStart.hashCode()));
        result = ((result* 31)+((this.dateEnd == null)? 0 :this.dateEnd.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StatisticsCreate) == false) {
            return false;
        }
        StatisticsCreate rhs = ((StatisticsCreate) other);
        return (((this.dateStart == rhs.dateStart)||((this.dateStart!= null)&&this.dateStart.equals(rhs.dateStart)))&&((this.dateEnd == rhs.dateEnd)||((this.dateEnd!= null)&&this.dateEnd.equals(rhs.dateEnd))));
    }

}
