package org.schabi.newpipe.url.model.list.domain;

import org.schabi.newpipe.url.model.list.UrlPseudoQueryList;
import org.schabi.newpipe.url.model.list.UrlQueryList;

public class UrlDomainPrivate<T> extends UrlPseudoQueryList<T> {
    public UrlDomainPrivate(UrlQueryList<T> queryList) {
        super(queryList);
    }
}
