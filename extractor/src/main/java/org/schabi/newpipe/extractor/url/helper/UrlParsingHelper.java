package org.schabi.newpipe.extractor.url.helper;

import org.schabi.newpipe.extractor.feature.Features;
import org.schabi.newpipe.extractor.url.model.UrlParsingFeature;
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
    /*
     * This is not RFC3986 this is own norm that tries to parsing the living standard
     * This is error-, regex- and private-less, option-full written with high level functions
     *
     * Norm explained in ascii:
     * foo://example.com:8042/over/there?name=ferret&other&another#foo://example.com:8042/over/there#?name=ferret&other&anothh#!ando
     * |  |   \_____/ \_/ |  |\___/ \___/ \__/ \____/ \___/ \____/ |  |   \_____/ \_/ |  |\___/ \___/ \__/ \____/ \___/ \____/  \__/
     * |  |     \      /  |  |  \     /   key   value    options   |  |     \      /  |  |  \     /   key   value    options   option
     * |  |      \    /   |  |   \   /     \_______/  \__________/ |  |      \    /   |  |   \   /     \_______/  \__________/  \__/
     * |  |       \  /    |  |    \ /          |               |   |  |       \  /    |  |    \ /          |               |      |
     * |  |      parts    |  |   parts     map<str,list<str>> list |  |      parts    |  |   parts     map<str,list<str>> list  list
     * \__/   \_________/ \__/ |       |  |                      | \__/   \_________/ \__/ |       |  |                       | |  |
     *   |          |      |   |       |  |                      |   |          |      |   |       |  |                       | |  |
     *   |          |      |   |       |  |                      |   |          |      |   |       |  |                       | |  |
     *   |          |      |   \_______/  |                      |   |          |      |   \_______/  |                       | |  |
     *   |          |      |       |      |                      |   |          |      |       |      |                       | |  |
     *  protocol  domain  port  filepath  |                      |  protocol  domain  port  filepath  |                       | |  |
     *  \_______________________________/ \______________________/ \_______________________________/  \_______________________/ \__/
     *                 |                              |                            |                              |               |
     *                raw                           params                        raw                           params          params
     *               request                                                    request
     * \_________________________________________________________/ \_______________________________/  \_______________________/ \__/
     *                            |                                                |                               |              |
     *                          public                                           private                       private         private
     * everything is a query (the js strat everthing is a function :)
     *
     * private delimiters = #! #? #
     * the #? delimiter is not a indicator for params or map
     *
     * params delimiter = ?
     * option/entry<key, value> delimiter = &
     * protocol delimiters = :// :
     *
     * Escaping is not visible in ascii but keys, values and options get unescaped
     * The format not forcing a protocol domain or a port and can even none of them given and still is a valid url/path
     * Their is no valid check for domains (this can be done with iana site and the url navigator)
     * This helper only throws in some cases a UnsupportedEncodingException if is right programmed this never get thrown
     */
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
    public static UrlNavigator parse(String url, String encoding, UrlParsingFeature... features) throws UnsupportedEncodingException {
        return new UrlNavigator(getQueriesFromUrl(url, encoding, features));
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
    public static List<UrlQuery> getQueriesFromUrl(String url, String encoding, UrlParsingFeature... features) throws UnsupportedEncodingException {
        List<UrlQuery> queries = new ArrayList<UrlQuery>();

        RawUrlRequest urlRequest = getRawRequestFromUrl(url);
        queries.addAll(getQueriesFromRawRequest(urlRequest));
        queries.addAll(getQueriesFromRequestPath(urlRequest.getRequest(), encoding, features));
        return queries;
    }

    public static List<UrlQuery> getQueriesFromRawRequest(RawUrlRequest urlRequest) {
        List<UrlQuery> queries = new ArrayList<UrlQuery>();

        String protocol = urlRequest.getProtocol();
        String strPort = urlRequest.getPort();
        Integer port = null;
        if (strPort != null) {
            try {
                port = Integer.parseInt(strPort);
            } catch (NumberFormatException ex) {
                // TODO better error handling
            }
        }

        boolean gotProtocol = protocol != null;
        boolean gotPort = port != null;

        if (gotProtocol && gotPort) {
            queries.add(UrlProtocolTyp.selectProtocolByName(protocol, port));
        } else if (gotProtocol) {
            queries.add(UrlProtocolTyp.selectProtocolByName(protocol));
        }

        return queries;
    }

    /*
     * getRawRequestFromUrl - a simple function that split the url into request, protocol and port if given
     *
     * @param String url
     * @return RawUrlRequest
     */
    public static RawUrlRequest getRawRequestFromUrl(String url) {
        String protocol = null;
        String port = null;
        String domain = null;
        String request = null;

        String urlWithoutProtocol = url;
        String[] requestSplit = url.split("(:\\/\\/|:)");
        if (requestSplit.length > 1) {
            protocol = requestSplit[0];
            urlWithoutProtocol = url.substring(protocol.length());
        }

        // TODO better string splitting functions
        String[] pathParts = urlWithoutProtocol.split("/");
        String domainAndPort = pathParts[0];
        String[] portSplit = domainAndPort.split(":");

        boolean portOrientated = portSplit.length > 1;
        boolean pathOrientated = pathParts.length > 1;

        if (portOrientated || pathOrientated) {
            request = urlWithoutProtocol.substring(domainAndPort.length());

            if (portOrientated) {
                port = portSplit[portSplit.length - 1];
                domain = domainAndPort.substring(0, port.length() - 1);
            } else if (pathOrientated) {
                domain = domainAndPort;
            }
        } else {
            request = urlWithoutProtocol;
        }

        return new RawUrlRequest(protocol, domain, port, request);
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

        boolean isFirstQuery = true;
        String firstQuery = null;
        for (String part:request.split("(\\#\\?|\\#\\!|\\#)")) {
            if (part.isEmpty() && removeEmpty) {
                continue;
            }
            if (isFirstQuery) {
                firstQuery = part;
                isFirstQuery = false;
                continue;
            }
            privateParts.add(part);
        }

        return new UrlRawQuery[]{
                getRawQueryFromFirstQuery(firstQuery, removeEmpty),
                new UrlRawQuery<List<String>>(parts, UrlRawQuery.UrlRawQueryState.PUBLIC),
                new UrlRawQuery<List<String>>(parts, UrlRawQuery.UrlRawQueryState.PRIVATE)
        };
    }

    public static UrlRawQuery getRawQueryFromFirstQuery(String firstQuery, boolean removeEmpty) {
        if (firstQuery == null) {
            return new UrlRawQuery<String>("");
        }

        List<String> parts = new ArrayList<String>();
        if (firstQuery != null) {
            for (String part : firstQuery.split("\\?")) {
                if (part.trim().isEmpty() && removeEmpty) {
                    continue;
                }
                parts.add(part);
            }
        }
        return new UrlRawQuery<String>(firstQuery);
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
    public static List<UrlQuery> getQueriesFromRequestPath(String requestPath, String encoding, UrlParsingFeature... features) throws UnsupportedEncodingException {
        Features<UrlParsingFeature> check = new Features<UrlParsingFeature>(features);
        List<UrlQuery> queries = new ArrayList<UrlQuery>();

        UrlRawQuery[] rawQuerys = getRawQueriesFromRequest(requestPath, check.got(UrlParsingFeature.REMOVE_EMPTY_QUERY));
        for (UrlRawQuery rawQuery:rawQuerys) {
            if (rawQuery.getValue() instanceof String) {
                queries.addAll(getQueriesFromDomainFilePath((String) rawQuery.getValue(), check.got(UrlParsingFeature.REMOVE_EMPTY_DOMAIN_PART), false, encoding));
            } else if (rawQuery.getValue() instanceof List) {
                if (rawQuery.getState() == UrlRawQuery.UrlRawQueryState.PRIVATE) {
                    // PrivateQueryList
                    for (String strPrivateQuery:(List<String>) rawQuery.getValue()) {
                        if (check.got(UrlParsingFeature.PRIVATE_URL_SUPPORT)) {
                            RawUrlRequest request = getRawRequestFromUrl((String) rawQuery.getValue());
                            queries.addAll(getQueriesFromRawRequest(request));

                            String strRequest = request.getRequest();
                            if (strRequest != null) {
                                strPrivateQuery = strRequest;
                            } else {
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
    public static UrlParamsQuery getUrlParamsQueryFromRawQuery(String rawQuery, String encoding) throws UnsupportedEncodingException {
        UrlParamsQuery query = new UrlParamsQuery();

        String[] keyValues = rawQuery.split("&");
        if (keyValues.length == 1) {
            query.add(URLDecoder.decode(rawQuery, encoding));
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
