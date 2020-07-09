package com.louisgeek.vosk;

import java.util.List;

/**
 * Created by louisgeek on 2020/4/3.
 */
public class VoskResult {


    /**
     * result : [{"conf":1,"end":11.19,"start":8.4,"word":"非常"},{"conf":1,"end":12.39,"start":11.76,"word":"好"},{"conf":1,"end":13.2,"start":12.87,"word":"这"},{"conf":1,"end":13.68,"start":13.2,"word":"是"},{"conf":1,"end":15.33,"start":14.4,"word":"中文"}]
     * text : 非常 好 这 是 中文
     */

    private String text;
    private List<ResultBean> result;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * conf : 1.0
         * end : 11.19
         * start : 8.4
         * word : 非常
         */

        private double conf;
        private double end;
        private double start;
        private String word;

        public double getConf() {
            return conf;
        }

        public void setConf(double conf) {
            this.conf = conf;
        }

        public double getEnd() {
            return end;
        }

        public void setEnd(double end) {
            this.end = end;
        }

        public double getStart() {
            return start;
        }

        public void setStart(double start) {
            this.start = start;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }
}
