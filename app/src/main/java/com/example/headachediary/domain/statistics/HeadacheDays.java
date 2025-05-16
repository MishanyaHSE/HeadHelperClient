package com.example.headachediary.domain.statistics;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeadacheDays {

    @SerializedName("without_pain")
    @Expose
    private Integer withoutPain;
    @SerializedName("with_pain")
    @Expose
    private Integer withPain;

    public Integer getWithoutPain() {
        return withoutPain;
    }

    public void setWithoutPain(Integer withoutPain) {
        this.withoutPain = withoutPain;
    }

    public Integer getWithPain() {
        return withPain;
    }

    public void setWithPain(Integer withPain) {
        this.withPain = withPain;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(HeadacheDays.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("withoutPain");
        sb.append('=');
        sb.append(((this.withoutPain == null)?"<null>":this.withoutPain));
        sb.append(',');
        sb.append("withPain");
        sb.append('=');
        sb.append(((this.withPain == null)?"<null>":this.withPain));
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
        result = ((result* 31)+((this.withoutPain == null)? 0 :this.withoutPain.hashCode()));
        result = ((result* 31)+((this.withPain == null)? 0 :this.withPain.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HeadacheDays) == false) {
            return false;
        }
        HeadacheDays rhs = ((HeadacheDays) other);
        return (((this.withoutPain == rhs.withoutPain)||((this.withoutPain!= null)&&this.withoutPain.equals(rhs.withoutPain)))&&((this.withPain == rhs.withPain)||((this.withPain!= null)&&this.withPain.equals(rhs.withPain))));
    }

}
