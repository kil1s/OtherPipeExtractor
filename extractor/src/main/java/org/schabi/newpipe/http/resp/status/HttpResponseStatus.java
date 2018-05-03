package org.schabi.newpipe.http.resp.status;

public interface HttpResponseStatus {
    String getMessage();
    boolean isValidStatus();
    Integer getStatus();
    String getOriginalStatus();
}
