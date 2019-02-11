package com.minewtech.thingoo.model.instructions;

public class Instructions {
    private  String action;
    private  String requestId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "Instructions{" +
                "action='" + action + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
