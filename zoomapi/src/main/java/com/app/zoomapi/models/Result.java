package com.app.zoomapi.models;

public class Result {
    private int status;
    private String errorMessage;
    private Object data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isPresent(){
        return data!=null;
    }

    public Object getData(){
       return data;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public int getStatus(){
        return status;
    }
}
