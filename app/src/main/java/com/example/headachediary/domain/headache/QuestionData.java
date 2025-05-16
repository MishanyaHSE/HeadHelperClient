package com.example.headachediary.domain.headache;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class QuestionData {

    @SerializedName("time_question")
    @Expose
    private Boolean timeQuestion;
    @SerializedName("duration_question")
    @Expose
    private Boolean durationQuestion;
    @SerializedName("intensity_question")
    @Expose
    private Boolean intensityQuestion;
    @SerializedName("pain_type_question")
    @Expose
    private Boolean painTypeQuestion;
    @SerializedName("area_question")
    @Expose
    private Boolean areaQuestion;
    @SerializedName("triggers_question")
    @Expose
    private Boolean triggersQuestion;
    @SerializedName("medicine_question")
    @Expose
    private Boolean medicineQuestion;
    @SerializedName("symptoms_question")
    @Expose
    private Boolean symptomsQuestion;
    @SerializedName("pressure_question")
    @Expose
    private Boolean pressureQuestion;
    @SerializedName("comment_question")
    @Expose
    private Boolean commentQuestion;

    public Boolean getTimeQuestion() {
        return timeQuestion;
    }

    public void setTimeQuestion(Boolean timeQuestion) {
        this.timeQuestion = timeQuestion;
    }

    public Boolean getDurationQuestion() {
        return durationQuestion;
    }

    public void setDurationQuestion(Boolean durationQuestion) {
        this.durationQuestion = durationQuestion;
    }

    public Boolean getIntensityQuestion() {
        return intensityQuestion;
    }

    public void setIntensityQuestion(Boolean intensityQuestion) {
        this.intensityQuestion = intensityQuestion;
    }

    public Boolean getPainTypeQuestion() {
        return painTypeQuestion;
    }

    public void setPainTypeQuestion(Boolean painTypeQuestion) {
        this.painTypeQuestion = painTypeQuestion;
    }

    public Boolean getAreaQuestion() {
        return areaQuestion;
    }

    public void setAreaQuestion(Boolean areaQuestion) {
        this.areaQuestion = areaQuestion;
    }

    public Boolean getTriggersQuestion() {
        return triggersQuestion;
    }

    public void setTriggersQuestion(Boolean triggersQuestion) {
        this.triggersQuestion = triggersQuestion;
    }

    public Boolean getMedicineQuestion() {
        return medicineQuestion;
    }

    public void setMedicineQuestion(Boolean medicineQuestion) {
        this.medicineQuestion = medicineQuestion;
    }

    public Boolean getSymptomsQuestion() {
        return symptomsQuestion;
    }

    public void setSymptomsQuestion(Boolean symptomsQuestion) {
        this.symptomsQuestion = symptomsQuestion;
    }

    public Boolean getPressureQuestion() {
        return pressureQuestion;
    }

    public void setPressureQuestion(Boolean pressureQuestion) {
        this.pressureQuestion = pressureQuestion;
    }

    public Boolean getCommentQuestion() {
        return commentQuestion;
    }

    public void setCommentQuestion(Boolean commentQuestion) {
        this.commentQuestion = commentQuestion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(QuestionData.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("timeQuestion");
        sb.append('=');
        sb.append(((this.timeQuestion == null)?"<null>":this.timeQuestion));
        sb.append(',');
        sb.append("durationQuestion");
        sb.append('=');
        sb.append(((this.durationQuestion == null)?"<null>":this.durationQuestion));
        sb.append(',');
        sb.append("intensityQuestion");
        sb.append('=');
        sb.append(((this.intensityQuestion == null)?"<null>":this.intensityQuestion));
        sb.append(',');
        sb.append("painTypeQuestion");
        sb.append('=');
        sb.append(((this.painTypeQuestion == null)?"<null>":this.painTypeQuestion));
        sb.append(',');
        sb.append("areaQuestion");
        sb.append('=');
        sb.append(((this.areaQuestion == null)?"<null>":this.areaQuestion));
        sb.append(',');
        sb.append("triggersQuestion");
        sb.append('=');
        sb.append(((this.triggersQuestion == null)?"<null>":this.triggersQuestion));
        sb.append(',');
        sb.append("medicineQuestion");
        sb.append('=');
        sb.append(((this.medicineQuestion == null)?"<null>":this.medicineQuestion));
        sb.append(',');
        sb.append("symptomsQuestion");
        sb.append('=');
        sb.append(((this.symptomsQuestion == null)?"<null>":this.symptomsQuestion));
        sb.append(',');
        sb.append("pressureQuestion");
        sb.append('=');
        sb.append(((this.pressureQuestion == null)?"<null>":this.pressureQuestion));
        sb.append(',');
        sb.append("commentQuestion");
        sb.append('=');
        sb.append(((this.commentQuestion == null)?"<null>":this.commentQuestion));
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
        result = ((result* 31)+((this.intensityQuestion == null)? 0 :this.intensityQuestion.hashCode()));
        result = ((result* 31)+((this.triggersQuestion == null)? 0 :this.triggersQuestion.hashCode()));
        result = ((result* 31)+((this.symptomsQuestion == null)? 0 :this.symptomsQuestion.hashCode()));
        result = ((result* 31)+((this.areaQuestion == null)? 0 :this.areaQuestion.hashCode()));
        result = ((result* 31)+((this.medicineQuestion == null)? 0 :this.medicineQuestion.hashCode()));
        result = ((result* 31)+((this.commentQuestion == null)? 0 :this.commentQuestion.hashCode()));
        result = ((result* 31)+((this.pressureQuestion == null)? 0 :this.pressureQuestion.hashCode()));
        result = ((result* 31)+((this.timeQuestion == null)? 0 :this.timeQuestion.hashCode()));
        result = ((result* 31)+((this.durationQuestion == null)? 0 :this.durationQuestion.hashCode()));
        result = ((result* 31)+((this.painTypeQuestion == null)? 0 :this.painTypeQuestion.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof QuestionData) == false) {
            return false;
        }
        QuestionData rhs = ((QuestionData) other);
        return (((((((((((this.intensityQuestion == rhs.intensityQuestion)||((this.intensityQuestion!= null)&&this.intensityQuestion.equals(rhs.intensityQuestion)))&&((this.triggersQuestion == rhs.triggersQuestion)||((this.triggersQuestion!= null)&&this.triggersQuestion.equals(rhs.triggersQuestion))))&&((this.symptomsQuestion == rhs.symptomsQuestion)||((this.symptomsQuestion!= null)&&this.symptomsQuestion.equals(rhs.symptomsQuestion))))&&((this.areaQuestion == rhs.areaQuestion)||((this.areaQuestion!= null)&&this.areaQuestion.equals(rhs.areaQuestion))))&&((this.medicineQuestion == rhs.medicineQuestion)||((this.medicineQuestion!= null)&&this.medicineQuestion.equals(rhs.medicineQuestion))))&&((this.commentQuestion == rhs.commentQuestion)||((this.commentQuestion!= null)&&this.commentQuestion.equals(rhs.commentQuestion))))&&((this.pressureQuestion == rhs.pressureQuestion)||((this.pressureQuestion!= null)&&this.pressureQuestion.equals(rhs.pressureQuestion))))&&((this.timeQuestion == rhs.timeQuestion)||((this.timeQuestion!= null)&&this.timeQuestion.equals(rhs.timeQuestion))))&&((this.durationQuestion == rhs.durationQuestion)||((this.durationQuestion!= null)&&this.durationQuestion.equals(rhs.durationQuestion))))&&((this.painTypeQuestion == rhs.painTypeQuestion)||((this.painTypeQuestion!= null)&&this.painTypeQuestion.equals(rhs.painTypeQuestion))));
    }

}
