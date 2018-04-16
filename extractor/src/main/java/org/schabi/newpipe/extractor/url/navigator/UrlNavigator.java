package org.schabi.newpipe.extractor.url.navigator;

import org.schabi.newpipe.extractor.url.model.UrlQuery;
import org.schabi.newpipe.extractor.url.model.list.UrlPseudoQueryList;
import org.schabi.newpipe.extractor.url.model.list.domain.UrlDomainPrivate;
import org.schabi.newpipe.extractor.url.model.list.domain.UrlDomainPublic;
import org.schabi.newpipe.extractor.url.model.list.filepath.UrlFilepathPrivate;
import org.schabi.newpipe.extractor.url.model.list.filepath.UrlFilepathPublic;
import org.schabi.newpipe.extractor.url.model.params.UrlParamsQueryInterface;
import org.schabi.newpipe.extractor.url.model.params.UrlPrivateParamsQuery;
import org.schabi.newpipe.extractor.url.model.params.UrlPublicParamsQuery;
import org.schabi.newpipe.extractor.url.model.protocol.model.UrlProtocol;
import org.schabi.newpipe.extractor.url.model.protocol.wellknown.WellKnownProtocolHelper;

import java.util.ArrayList;
import java.util.List;

public class UrlNavigator {
    // TODO make type safer
    private List<UrlQuery> protocols = new ArrayList<UrlQuery>();
    private List<UrlQuery> privateDomains = new ArrayList<UrlQuery>();
    private List<UrlQuery> publicDomains = new ArrayList<UrlQuery>();
    private List<UrlQuery> publicFilepaths = new ArrayList<UrlQuery>();
    private List<UrlQuery> privateFilepaths = new ArrayList<UrlQuery>();
    private List<UrlQuery> publicParams = new ArrayList<UrlQuery>();
    private List<UrlQuery> privateParams = new ArrayList<UrlQuery>();

    public UrlNavigator(List<UrlQuery> queries) {
        for (UrlQuery query:queries) {
            if (query instanceof WellKnownProtocolHelper) {
                protocols.add(query);
            } else if (query instanceof UrlDomainPrivate) {
                privateDomains.add(query);
            } else if (query instanceof UrlDomainPublic) {
                publicDomains.add(query);
            } else if (query instanceof UrlFilepathPrivate) {
                privateFilepaths.add(query);
            } else if (query instanceof UrlFilepathPublic) {
                publicFilepaths.add(query);
            } else if (query instanceof UrlPrivateParamsQuery) {
                privateParams.add(query);
            } else if (query instanceof UrlPublicParamsQuery) {
                publicParams.add(query);
            }
        }
    }

    private boolean gotInQueryedPseudoList(List<UrlQuery> queryedList, String in, String delimiter) {
        String[] requiredParts = in.split(delimiter);
        for (UrlQuery queryed:queryedList) {
            UrlPseudoQueryList parts = (UrlPseudoQueryList) queryed;
            if (parts.size() == requiredParts.length) {
                continue;
            }
            boolean isIn = true;
            for (int i = 0; i < requiredParts.length; i++) {
                if (!requiredParts[i].equals(parts.get(i))) {
                    isIn = false;
                    break;
                }
            }
            if (isIn) {
                return true;
            }
        }
        return false;
    }

    private boolean gotInQueryedParams(List<UrlQuery> queryedParams, String key) {
        for (UrlQuery queryed:queryedParams) {
            UrlParamsQueryInterface params = (UrlParamsQueryInterface) queryed;
            if (params.got(key)) {
                return true;
            }
        }
        return false;
    }

    private boolean gotInQueryedParamsOptions(List<UrlQuery> queryedParams, String option) {
        for (UrlQuery queryed:queryedParams) {
            UrlParamsQueryInterface params = (UrlParamsQueryInterface) queryed;
            if (params.gotOption(option)) {
                return true;
            }
        }
        return false;
    }

    private List<String> getFromQueryedParams(List<UrlQuery> queryedParams, String key) {
        List<String> values = new ArrayList<String>();
        for (UrlQuery queryed:queryedParams) {
            UrlParamsQueryInterface params = (UrlParamsQueryInterface) queryed;
            List<String> currentValues = params.get(key);
            if (currentValues.size() > 0) {
                values.addAll(currentValues);
            }
        }
        return values;
    }

    private List<String> getOptionsFromQueryedParams(List<UrlQuery> queryedParams) {
        List<String> values = new ArrayList<String>();
        for (UrlQuery queryed:queryedParams) {
            UrlParamsQueryInterface params = (UrlParamsQueryInterface) queryed;
            List<String> currentValues = params.getOptions();
            if (currentValues.size() > 0) {
                values.addAll(currentValues);
            }
        }
        return values;
    }

    public boolean gotProtocol(String protocolName) {
        UrlProtocol requiredProtocol = WellKnownProtocolHelper.selectProtocolByName(protocolName);
        return gotProtocol(requiredProtocol);
    }

    public boolean gotProtocol(UrlProtocol requiredProtocol) {
        for (UrlQuery queryProtocol:protocols) {
            WellKnownProtocolHelper protocol = (WellKnownProtocolHelper) queryProtocol;
            if (protocol.getProtocol().getClass().isInstance(requiredProtocol)) {
                if (protocol == WellKnownProtocolHelper.UNKNOWN) {
                    if (protocol.getName().equalsIgnoreCase(requiredProtocol.getName())) {
                        return true;
                    }
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public List<WellKnownProtocolHelper> getProtocols() {
        List<WellKnownProtocolHelper> protocolTyps = new ArrayList<WellKnownProtocolHelper>();
        for (UrlQuery protocol:protocols) {
            protocolTyps.add((WellKnownProtocolHelper) protocol);
        }
        return protocolTyps;
    }

    public boolean gotPublicDomain(String requiredDomain) {
        return gotInQueryedPseudoList(publicDomains, requiredDomain, ".");
    }

    public boolean gotPrivateDomain(String requiredDomain) {
        return gotInQueryedPseudoList(privateDomains, requiredDomain, ".");
    }

    public boolean gotDomain(String requiredDomain) {
        return gotPrivateDomain(requiredDomain) || gotPublicDomain(requiredDomain);
    }

    public boolean gotPublicFilepath(String requiredFilePath) {
        return gotInQueryedPseudoList(publicFilepaths, requiredFilePath, "/");
    }

    public boolean gotPrivateFilepath(String requiredFilePath) {
        return gotInQueryedPseudoList(privateFilepaths, requiredFilePath, "/");
    }

    public boolean gotFilepath(String requiredFilepath) {
        return gotPrivateFilepath(requiredFilepath) || gotPublicFilepath(requiredFilepath);
    }

    public boolean gotPrivateParams(String key) {
        return gotInQueryedParams(privateParams, key);
    }

    public boolean gotPublicParams(String key) {
        return gotInQueryedParams(publicParams, key);
    }

    public boolean gotParams(String key) {
        return gotPublicParams(key) || gotPrivateParams(key);
    }

    public List<String> getPrivateParams(String key) {
        return getFromQueryedParams(privateParams, key);
    }

    public List<String> getPublicParams(String key) {
        return getFromQueryedParams(publicParams, key);
    }

    public List<String> getParams(String key) {
        List<String> values = new ArrayList<String>();
        values.addAll(getPublicParams(key));
        values.addAll(getPrivateParams(key));
        return values;
    }

    public boolean gotPrivateOption(String option) {
        return gotInQueryedParamsOptions(privateParams, option);
    }

    public boolean gotPublicOption(String option) {
        return gotInQueryedParamsOptions(publicParams, option);
    }

    public boolean gotOption(String option) {
        return gotPublicOption(option) || gotPrivateOption(option);
    }

    public List<String> getPrivateOptions() {
        return getOptionsFromQueryedParams(privateParams);
    }

    public List<String> getPublicOptions() {
        return getOptionsFromQueryedParams(publicParams);
    }

    public List<String> getOptions() {
        List<String> options = new ArrayList<String>();
        options.addAll(getPublicOptions());
        options.addAll(getPrivateOptions());
        return options;
    }

    public List<UrlQuery> getPublicDomains() {
        return publicDomains;
    }

    public List<UrlQuery> getPrivateDomains() {
        return privateDomains;
    }

    public List<UrlQuery> getPublicFilepaths() {
        return publicFilepaths;
    }

    public List<UrlQuery> getPrivateFilepaths() {
        return privateFilepaths;
    }

    public List<UrlQuery> getFilepaths() {
        List<UrlQuery> filepaths = new ArrayList<UrlQuery>();
        filepaths.addAll(publicFilepaths);
        filepaths.addAll(privateFilepaths);
        return filepaths;
    }

    public List<UrlQuery> getPublicParams() {
        return publicParams;
    }

    public List<UrlQuery> getPrivateParams() {
        return privateParams;
    }

    public List<String> get(String entry) {
        List<String> resultList = new ArrayList<String>();
        resultList.addAll(getParams(entry));
        return resultList;
    }

    public boolean got(String entry) {
        return gotProtocol(entry) || gotDomain(entry) || gotFilepath(entry) || gotParams(entry);
    }
}
