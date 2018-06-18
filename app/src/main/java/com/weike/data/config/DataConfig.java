package com.weike.data.config;

public final class DataConfig {



    public static class RemindDateType {
        public static final int TYPE_OF_DAY = 1;
        public static final int TYPE_OF_WEEK = 2;
        public static final int TYPE_OF_MONTH = 3;
        public static final int TYPE_OF_YEAR = 4;
    }

    public static class RemindType{
        public static final int TYPE_REMIND  = 1;
        public static final int TYPE_UNREMIND  = 2;
    }

    public static class RemindRepeat{
        public static final int TYPE_REPEAT = 1;
        public static final int TYPE_UNREPEAT = 2;
    }

    public static class RemindLevel{
        public static final int TYPE_OF_HEIGHT = 1;
        public static final int TYPE_OF_MID = 2;
        public static final int TYPE_OF_LOAD = 3;
    }
}
