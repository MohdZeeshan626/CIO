package com.singleevent.sdk.model.Polling;

import java.io.Serializable;

public class Pollresult implements Serializable{

    private String feedback;

    private String count;

    public String getFeedback ()
    {
        return feedback;
    }

    public void setFeedback (String feedback)
    {
        this.feedback = feedback;
    }

    public String getCount ()
    {
        return count;
    }

    public void setCount (String count)
    {
        this.count = count;
    }
}
