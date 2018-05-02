package org.schabi.newpipe.http.resp;

import org.schabi.newpipe.http.resp.status.HttpResponseStatus;

public interface HttpResponse {
    HttpResponseStatus getStatus();

    //Map<String, List<String>>
}
