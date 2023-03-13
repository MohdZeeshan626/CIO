package com.singleevent.sdk.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.rxjava3.core.Observable;

public class Utils {
    public  static Observable<Document> getjsoupcontent(String url){
        return Observable.fromCallable(()->{
            try{
                Document document = Jsoup.connect(url).timeout(0).get();
                return document;
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });
    }

}
