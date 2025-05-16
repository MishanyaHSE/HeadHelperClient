package com.example.headachediary.domain.headache;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class ReportCreate {

    @SerializedName("date_start")
    @Expose
    private String dateStart;
    @SerializedName("date_end")
    @Expose
    private String dateEnd;
    @SerializedName("format")
    @Expose
    private Integer format;
    @SerializedName("send_to_mail")
    @Expose
    private Boolean sendToMail;
    @SerializedName("statistics")
    @Expose
    private Boolean statistics;

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

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public Boolean getSendToMail() {
        return sendToMail;
    }

    public void setSendToMail(Boolean sendToMail) {
        this.sendToMail = sendToMail;
    }

    public Boolean getStatistics() {
        return statistics;
    }

    public void setStatistics(Boolean statistics) {
        this.statistics = statistics;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ReportCreate.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("dateStart");
        sb.append('=');
        sb.append(((this.dateStart == null)?"<null>":this.dateStart));
        sb.append(',');
        sb.append("dateEnd");
        sb.append('=');
        sb.append(((this.dateEnd == null)?"<null>":this.dateEnd));
        sb.append(',');
        sb.append("format");
        sb.append('=');
        sb.append(((this.format == null)?"<null>":this.format));
        sb.append(',');
        sb.append("sendToMail");
        sb.append('=');
        sb.append(((this.sendToMail == null)?"<null>":this.sendToMail));
        sb.append(',');
        sb.append("statistics");
        sb.append('=');
        sb.append(((this.statistics == null)?"<null>":this.statistics));
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
        result = ((result* 31)+((this.format == null)? 0 :this.format.hashCode()));
        result = ((result* 31)+((this.dateEnd == null)? 0 :this.dateEnd.hashCode()));
        result = ((result* 31)+((this.dateStart == null)? 0 :this.dateStart.hashCode()));
        result = ((result* 31)+((this.sendToMail == null)? 0 :this.sendToMail.hashCode()));
        result = ((result* 31)+((this.statistics == null)? 0 :this.statistics.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ReportCreate) == false) {
            return false;
        }
        ReportCreate rhs = ((ReportCreate) other);
        return ((((((this.format == rhs.format)||((this.format!= null)&&this.format.equals(rhs.format)))&&((this.dateEnd == rhs.dateEnd)||((this.dateEnd!= null)&&this.dateEnd.equals(rhs.dateEnd))))&&((this.dateStart == rhs.dateStart)||((this.dateStart!= null)&&this.dateStart.equals(rhs.dateStart))))&&((this.sendToMail == rhs.sendToMail)||((this.sendToMail!= null)&&this.sendToMail.equals(rhs.sendToMail))))&&((this.statistics == rhs.statistics)||((this.statistics!= null)&&this.statistics.equals(rhs.statistics))));
    }

}
