package com.singleevent.sdk.feeds_class;

import com.singleevent.sdk.model.My_Request;

/**
 * Created by webMOBI on 10/11/2017.
 */

public interface Scheduler {

    void accept(My_Request mtrequest);

    void decline(My_Request mtrequest);

}
