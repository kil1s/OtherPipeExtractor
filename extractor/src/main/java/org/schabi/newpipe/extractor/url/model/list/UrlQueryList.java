package org.schabi.newpipe.extractor.url.model.list;

import org.schabi.newpipe.extractor.url.model.UrlQuery;
import org.schabi.newpipe.extractor.url.model.list.domain.UrlDomainPrivate;
import org.schabi.newpipe.extractor.url.model.list.domain.UrlDomainPublic;
import org.schabi.newpipe.extractor.url.model.list.filepath.UrlFilepathPrivate;
import org.schabi.newpipe.extractor.url.model.list.filepath.UrlFilepathPublic;

import java.util.ArrayList;

public class UrlQueryList<T> extends ArrayList<T> implements UrlQuery {
    public UrlDomainPublic<T> toPublicDomain() {
        return new UrlDomainPublic<T>(this);
    }

    public UrlDomainPrivate<T> toPrivateDomain() {
        return new UrlDomainPrivate<T>(this);
    }

    public UrlFilepathPublic<T> toPublicFilepath() {
        return new UrlFilepathPublic<T>(this);
    }

    public UrlFilepathPrivate<T> toPrivateFilepath() {
        return new UrlFilepathPrivate<T>(this);
    }
}
