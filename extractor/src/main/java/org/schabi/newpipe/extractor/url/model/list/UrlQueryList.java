package org.schabi.newpipe.extractor.url.model.list;

import org.schabi.newpipe.extractor.url.model.UrlQuery;
import org.schabi.newpipe.extractor.url.model.list.domain.UrlDomainPrivate;
import org.schabi.newpipe.extractor.url.model.list.domain.UrlDomainPublic;

import java.util.ArrayList;

public class UrlQueryList<T> extends ArrayList<T> implements UrlQuery {
    public UrlDomainPublic<T> toPublicDomain() {
        return new UrlDomainPublic<T>(this);
    }

    public UrlDomainPrivate<T> toPrivateDomain() {
        return new UrlDomainPrivate<T>(this);
    }

    public UrlDomainPublic<T> toPublicFilepath() {
        return new UrlDomainPublic<T>(this);
    }

    public UrlDomainPrivate<T> toPrivateFilepath() {
        return new UrlDomainPrivate<T>(this);
    }
}
