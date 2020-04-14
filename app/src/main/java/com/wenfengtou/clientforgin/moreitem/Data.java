package com.wenfengtou.clientforgin.moreitem;

public class Data {
    public static final int TYPE_ONE = 1;//类型1
    public static final int TYPE_TWO = 2;//类型2

    public int type;//item内容 类型

    public int icon;
    public String username;
    public String message;

    public Data(int type, int icon, String username, String message) {
        this.type = type;
        this.icon = icon;
        this.username = username;
        this.message = message;
    }

    public static int getTypeOne() {
        return TYPE_ONE;
    }

    public static int getTypeTwo() {
        return TYPE_TWO;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}