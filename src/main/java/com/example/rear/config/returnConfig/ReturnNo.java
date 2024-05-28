package com.example.rear.config.returnConfig;

import java.util.HashMap;
import java.util.Map;

public enum ReturnNo {
    OK(0,"成功"),
    ;

    private int errNo;
    private String message;

    private static final Map<Integer, ReturnNo> returnNoMap;
    static {
        returnNoMap = new HashMap<>();
        for (ReturnNo enum1 : values()) {
            returnNoMap.put(enum1.errNo, enum1);
        }
    }

    ReturnNo(int code, String message){
        this.errNo = code;
        this.message = message;
    }

    public static ReturnNo getByCode(int code1) {
        ReturnNo[] all=ReturnNo.values();
        for (ReturnNo returnNo :all) {
            if (returnNo.errNo==code1) {
                return returnNo;
            }
        }
        return null;
    }
    public static ReturnNo getReturnNoByCode(int code){
        return returnNoMap.get(code);
    }
    public int getErrNo() {
        return errNo;
    }

    public String getMessage(){
        return message;
    }


}

