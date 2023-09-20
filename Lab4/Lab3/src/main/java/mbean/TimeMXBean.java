package mbean;

import beans.MainBean;

public class TimeMXBean implements TemplateTimeMXBean{
    @Override
    public long getMeanInterval() {
        return MainBean.meanTime();
    }
}
