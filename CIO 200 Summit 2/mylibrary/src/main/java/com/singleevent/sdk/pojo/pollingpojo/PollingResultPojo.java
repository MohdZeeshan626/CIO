package com.singleevent.sdk.pojo.pollingpojo;

public class PollingResultPojo {
    String optionName;
    int numberOfVotes;
    int numberOfTotalVotes;

    public PollingResultPojo(String optionName, int numberOfVotes, int numberOfTotalVotes) {
        this.optionName = optionName;
        this.numberOfVotes = numberOfVotes;
        this.numberOfTotalVotes = numberOfTotalVotes;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public int getNumberOfTotalVotes() {
        return numberOfTotalVotes;
    }
}
