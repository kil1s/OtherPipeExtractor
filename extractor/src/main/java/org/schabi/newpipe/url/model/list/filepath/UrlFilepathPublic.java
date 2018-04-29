package org.schabi.newpipe.url.model.list.filepath;

import org.schabi.newpipe.url.model.list.UrlPseudoQueryList;
import org.schabi.newpipe.url.model.list.UrlQueryList;

public class UrlFilepathPublic<T> extends UrlPseudoQueryList<T> {
    public UrlFilepathPublic(UrlQueryList<T> queryList) {
        super(queryList);
    }
}
