package com.xixiaohui.scanner.utils;

import com.google.zxing.Result;

public class MyResult{

    private Result result;

    private Boolean isFavorite;
    public MyResult(Result result){
        this.result = result;
        isFavorite = false;
    }

    public Result getResult() {
        return result;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}
