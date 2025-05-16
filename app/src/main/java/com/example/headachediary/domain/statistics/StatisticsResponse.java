package com.example.headachediary.domain.statistics;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticsResponse {

    @SerializedName("fill_percentage")
    @Expose
    private Integer fillPercentage;
    @SerializedName("headache_days")
    @Expose
    private HeadacheDays headacheDays;
    @SerializedName("time_stats")
    @Expose
    private TimeStats timeStats;
    @SerializedName("top_durations")
    @Expose
    private List<TopDuration> topDurations;
    @SerializedName("mean_intensity")
    @Expose
    private Double meanIntensity;
    @SerializedName("top_triggers")
    @Expose
    private List<TopTrigger> topTriggers;

    public Integer getFillPercentage() {
        return fillPercentage;
    }

    public void setFillPercentage(Integer fillPercentage) {
        this.fillPercentage = fillPercentage;
    }

    public HeadacheDays getHeadacheDays() {
        return headacheDays;
    }

    public void setHeadacheDays(HeadacheDays headacheDays) {
        this.headacheDays = headacheDays;
    }

    public TimeStats getTimeStats() {
        return timeStats;
    }

    public void setTimeStats(TimeStats timeStats) {
        this.timeStats = timeStats;
    }

    public List<TopDuration> getTopDurations() {
        return topDurations;
    }

    public void setTopDurations(List<TopDuration> topDurations) {
        this.topDurations = topDurations;
    }

    public Double getMeanIntensity() {
        return meanIntensity;
    }

    public void setMeanIntensity(Double meanIntensity) {
        this.meanIntensity = meanIntensity;
    }

    public List<TopTrigger> getTopTriggers() {
        return topTriggers;
    }

    public void setTopTriggers(List<TopTrigger> topTriggers) {
        this.topTriggers = topTriggers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StatisticsResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("fillPercentage");
        sb.append('=');
        sb.append(((this.fillPercentage == null)?"<null>":this.fillPercentage));
        sb.append(',');
        sb.append("headacheDays");
        sb.append('=');
        sb.append(((this.headacheDays == null)?"<null>":this.headacheDays));
        sb.append(',');
        sb.append("timeStats");
        sb.append('=');
        sb.append(((this.timeStats == null)?"<null>":this.timeStats));
        sb.append(',');
        sb.append("topDurations");
        sb.append('=');
        sb.append(((this.topDurations == null)?"<null>":this.topDurations));
        sb.append(',');
        sb.append("meanIntensity");
        sb.append('=');
        sb.append(((this.meanIntensity == null)?"<null>":this.meanIntensity));
        sb.append(',');
        sb.append("topTriggers");
        sb.append('=');
        sb.append(((this.topTriggers == null)?"<null>":this.topTriggers));
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
        result = ((result* 31)+((this.headacheDays == null)? 0 :this.headacheDays.hashCode()));
        result = ((result* 31)+((this.timeStats == null)? 0 :this.timeStats.hashCode()));
        result = ((result* 31)+((this.fillPercentage == null)? 0 :this.fillPercentage.hashCode()));
        result = ((result* 31)+((this.meanIntensity == null)? 0 :this.meanIntensity.hashCode()));
        result = ((result* 31)+((this.topDurations == null)? 0 :this.topDurations.hashCode()));
        result = ((result* 31)+((this.topTriggers == null)? 0 :this.topTriggers.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StatisticsResponse) == false) {
            return false;
        }
        StatisticsResponse rhs = ((StatisticsResponse) other);
        return (((((((this.headacheDays == rhs.headacheDays)||((this.headacheDays!= null)&&this.headacheDays.equals(rhs.headacheDays)))&&((this.timeStats == rhs.timeStats)||((this.timeStats!= null)&&this.timeStats.equals(rhs.timeStats))))&&((this.fillPercentage == rhs.fillPercentage)||((this.fillPercentage!= null)&&this.fillPercentage.equals(rhs.fillPercentage))))&&((this.meanIntensity == rhs.meanIntensity)||((this.meanIntensity!= null)&&this.meanIntensity.equals(rhs.meanIntensity))))&&((this.topDurations == rhs.topDurations)||((this.topDurations!= null)&&this.topDurations.equals(rhs.topDurations))))&&((this.topTriggers == rhs.topTriggers)||((this.topTriggers!= null)&&this.topTriggers.equals(rhs.topTriggers))));
    }

}
