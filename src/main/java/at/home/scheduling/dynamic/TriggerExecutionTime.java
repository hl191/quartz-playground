package at.home.scheduling.dynamic;

import java.util.Date;

public class TriggerExecutionTime {

    private String triggerKey;
    private Date executionTime;

    public TriggerExecutionTime(String triggerKey, Date executionTime){
        this.triggerKey=triggerKey;
        this.executionTime = executionTime;
    }

    public String getTriggerKey() {
        return triggerKey;
    }

    public void setTriggerKey(String triggerKey) {
        this.triggerKey = triggerKey;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }
}
