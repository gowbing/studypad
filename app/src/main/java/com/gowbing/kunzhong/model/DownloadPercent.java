package com.gowbing.kunzhong.model;

import java.util.List;

/**
 * Created by hss on 2017/1/22.
 */

public class DownloadPercent {

    List<Item> item;

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public static class Item {
        String url;
        int percent;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }
    }
}
