package org.schabi.newpipe.extractor.url.helper;

import org.schabi.newpipe.extractor.url.model.UrlQuery;
import org.schabi.newpipe.extractor.url.model.UrlRawQuery;
import org.schabi.newpipe.extractor.url.model.list.UrlQueryList;
import org.schabi.newpipe.extractor.url.model.protocol.UrlProtocolTyp;
import org.schabi.newpipe.extractor.url.model.request.RawUrlRequest;
import org.schabi.newpipe.extractor.url.model.params.UrlParamsQuery;
import org.schabi.newpipe.extractor.url.navigator.UrlNavigator;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class UrlParsingHelper {
    public static final String FILE_PATH_DELIMITER = "/";
    public static final String DOMAIN_DELIMITER = ".";

    /*
     * parse - get a url navigator from the url
     *
     * @param String url
     * @param boolean removeEmptyQuery
     * @param boolean removeEmptyPart
     * @param String encoding
     * @return UrlNavigator
     */
    public static UrlNavigator parse(String url, boolean removeEmptyQuery, boolean removeEmptyPart, boolean privateFilepathSupport, String encoding) throws UnsupportedEncodingException {
        return new UrlNavigator(getQueriesFromUrl(url, removeEmptyQuery, removeEmptyPart, privateFilepathSupport, encoding));
    }

    /*
     * parse - get a url navigator from the url
     *
     * @param String url
     * @param boolean removeEmptyQuery
     * @param boolean removeEmptyPart
     * @param String encoding
     * @return UrlNavigator
     */
    public static UrlNavigator parseTillPort(String url) {
        return new UrlNavigator(getQueriesFromRawRequest(getRawRequestFromUrl(url)));
    }

    /*
     * getQueriesFromUrl - get all url queries that can be resolved from the url
     *
     * @param String url
     * @param boolean removeEmptyQuery
     * @param boolean removeEmptyPart
     * @param String encoding
     * @return List<UrlQuery>
     */
    public static List<UrlQuery> getQueriesFromUrl(String url, boolean removeEmptyQuery, boolean removeEmptyPart, boolean privateFilepathSupport,String encoding) throws UnsupportedEncodingException {
        List<UrlQuery> queries = new ArrayList<UrlQuery>();

        RawUrlRequest urlRequest = getRawRequestFromUrl(url);
        queries.addAll(getQueriesFromRawRequest(urlRequest));
        queries.addAll(getQueriesFromRequestPath(urlRequest.getRequest(), removeEmptyQuery, removeEmptyPart, privateFilepathSupport, encoding));
        return queries;
    }

    public static List<UrlQuery> getQueriesFromRawRequest(RawUrlRequest urlRequest) {
        List<UrlQuery> queries = new ArrayList<UrlQuery>();

        String protocol = urlRequest.getProtocol();
        if (urlRequest.getPort() != null) {
            try {
                Integer port = Integer.parseInt(urlRequest.getPort());
                queries.add(UrlProtocolTyp.selectProtocolByName(protocol, port));
            } catch (NumberFormatException ex) {
                queries.add(UrlProtocolTyp.selectProtocolByName(protocol));
            }
        } else {
            queries.add(UrlProtocolTyp.selectProtocolByName(protocol));
        }

        return queries;
    }

    /*
     * getRawRequestFromUrl - a simple function that split the url into request and port if given as RawRequest
     *
     * @param String url
     * @return RawUrlRequest
     */
    public static RawUrlRequest getPortAndDomainAsRawRequest(String url) {
        // TODO better string splitting functions
        String[] pathParts = url.split("/");
        String domainAndPort = pathParts[0];

        String[] portSplit = domainAndPort.split(":");
        if (portSplit.length > 1) {
            String port = portSplit[portSplit.length - 1];
            url = domainAndPort.substring(0, port.length() - 1)+url.substring(domainAndPort.length());
            return new RawUrlRequest(null, url, port);
        }

        return new RawUrlRequest(null,url,null);
    }

    /*
     * getRawRequestFromUrl - a simple function that split the url into request, protocol and port if given
     *
     * @param String url
     * @return RawUrlRequest
     */
    public static RawUrlRequest getRawRequestFromUrl(String url) {
        String[] requestSplit = url.split(":\\/\\/");
        if (requestSplit.length > 1) {
            String protocol = requestSplit[0];
            RawUrlRequest portAndDomain = getPortAndDomainAsRawRequest(url);
            return new RawUrlRequest(protocol, portAndDomain.getRequest(), portAndDomain.getPort());
        }
        RawUrlRequest portAndDomain = getPortAndDomainAsRawRequest(url);
        return new RawUrlRequest(null,portAndDomain.getRequest(), portAndDomain.getPort());
    }

    /*
     * getRawQueriesFromRequest - get all raw queries from request string
     *
     * @param String request
     * @param boolean removeEmpty
     * @return UrlRawQuery[]
     */
    public static UrlRawQuery[] getRawQueriesFromRequest(String request, boolean removeEmpty) {
        // TODO use a better method for detect private and public query
        List<String> parts = new ArrayList<String>();
        List<String> privateParts = new ArrayList<String>();

        boolean isfirstQuery = true;
        String firstQuery = null;
        for (String part:request.split("(\\#\\!|\\#)")) {
            if (part.isEmpty() && removeEmpty) {
                continue;
            }
            if (isfirstQuery) {
                firstQuery = part;
                continue;
            }
            privateParts.add(part);
        }

        if (firstQuery != null) {
            for (String part : firstQuery.split("\\?")) {
                if (part.trim().isEmpty() && removeEmpty) {
                    continue;
                }
                parts.add(part);
            }
        }

        return new UrlRawQuery[]{
                new UrlRawQuery<String>(firstQuery != null ? firstQuery : ""),
                new UrlRawQuery<List<String>>(parts, UrlRawQuery.UrlRawQueryState.PUBLIC),
                new UrlRawQuery<List<String>>(parts, UrlRawQuery.UrlRawQueryState.PRIVATE)
        };
    }

    /*
     * getQueriesFromRequestPath - get all url queries that can be resolved from the request path
     *
     * @param String requestPath
     * @param boolean removeEmptyQuery
     * @param boolean removeEmptyPart
     * @param String encoding
     * @return List<UrlQuery>
     */
    public static List<UrlQuery> getQueriesFromRequestPath(String requestPath, boolean removeEmptyQuery, boolean removeEmptyPart, boolean privateFilepathSupport,String encoding) throws UnsupportedEncodingException {
        List<UrlQuery> queries = new ArrayList<UrlQuery>();

        UrlRawQuery[] rawQuerys = getRawQueriesFromRequest(requestPath, removeEmptyQuery);
        for (UrlRawQuery rawQuery:rawQuerys) {
            if (rawQuery.getValue() instanceof String) {
                queries.addAll(getQueriesFromDomainFilePath((String) rawQuery.getValue(), removeEmptyPart, false, encoding));
            } else if (rawQuery.getValue() instanceof List) {
                if (rawQuery.getState() == UrlRawQuery.UrlRawQueryState.PRIVATE) {
                    // PrivateQueryList
                    for (String strPrivateQuery:(List<String>) rawQuery.getValue()) {
                        if (privateFilepathSupport) {
                            boolean isFilePath = strPrivateQuery.startsWith(FILE_PATH_DELIMITER) || strPrivateQuery.endsWith(FILE_PATH_DELIMITER);
                            if (isFilePath && strPrivateQuery.split(FILE_PATH_DELIMITER).length > 1) {
                                queries.addAll(getQueriesFromDomainFilePath((String) rawQuery.getValue(), removeEmptyPart, true, encoding));
                                continue;
                            }
                        }
                        queries.add(getUrlParamsQueryFromRawQuery(strPrivateQuery, encoding).toPrivate());
                    }
                } else if (rawQuery.getState() == UrlRawQuery.UrlRawQueryState.PUBLIC) {
                    // PublicQueryList
                    for (String strPublicQuery:(List<String>) rawQuery.getValue()) {
                        queries.add(getUrlParamsQueryFromRawQuery(strPublicQuery, encoding).toPublic());
                    }
                }
            }
        }

        return queries;
    }

    /*
     * getQueriesFromDomainFilePath - get all url queries as private or public from a path that maybe contains domain and contains a file path
     *
     * @param String domainFilePath
     * @param boolean removeEmptyPart
     * @param boolean privat
     * @param String encoding
     * @return List<UrlQuery>
     */
    public static List<UrlQuery> getQueriesFromDomainFilePath(String domainFilePath, boolean removeEmptyPart, boolean privat, String encoding) throws UnsupportedEncodingException {
        List<UrlQuery> queries = new ArrayList<UrlQuery>();

        UrlQueryList<String> queryList = (UrlQueryList<String>) getPartsFromPath(domainFilePath, removeEmptyPart, encoding, FILE_PATH_DELIMITER);
        String domain = queryList.get(0);
        if (domain.split(DOMAIN_DELIMITER).length > 1) {
            queryList.remove(domain);
            UrlQueryList<String> domainList = (UrlQueryList<String>) getPartsFromPath(domain, removeEmptyPart, encoding, DOMAIN_DELIMITER);
            queries.add(privat ? domainList.toPrivateDomain() : domainList.toPublicDomain());
        }
        queries.add(privat ? queryList.toPrivateFilepath() : queryList.toPublicFilepath());

        return queries;
    }

    /*
     * getUrlParamsQueryFromRawQuery - get url parameters from raw string
     *
     * @param String rawString
     * @param String encoding
     * @return UrlParamsQuery
     */
    public static UrlParamsQuery getUrlParamsQueryFromRawQuery(String rawString, String encoding) throws UnsupportedEncodingException {
        UrlParamsQuery query = new UrlParamsQuery();

        String[] keyValues = rawString.split("&");
        if (keyValues.length == 1) {
            query.add(URLDecoder.decode(rawString, encoding));
            return query;
        }

        for (String keyValue:keyValues) {
            String[] keyValueSplit = keyValue.split("=");
            String key = URLDecoder.decode(keyValueSplit[0], encoding);
            String value = URLDecoder.decode(keyValue.substring(key.length()), encoding);
            if (value.isEmpty()) {
                query.add(key);
                continue;
            }
            query.add(key, value);
        }

        return query;
    }

    /*
     * getUrlParamsQueryFromRawQuery - get all path parts from path string with delimiter
     *
     * @param String path
     * @param boolean removeEmpty
     * @param String encoding
     * @param String delimiter
     * @return List<String> (instanceof UrlQueryList)
     */
    // TODO move to better string splitting util
    public static List<String> getPartsFromPath(String path, boolean removeEmpty, String encoding, String delimiter) throws UnsupportedEncodingException {
        UrlQueryList<String> parts = new UrlQueryList<String>();
        for (String part:path.split(delimiter)) {
            if (part.isEmpty() && removeEmpty) {
                continue;
            }
            parts.add(URLDecoder.decode(part, encoding));
        }
        return parts;
    }
}
