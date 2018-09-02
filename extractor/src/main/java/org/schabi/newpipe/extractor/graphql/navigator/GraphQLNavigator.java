package org.schabi.newpipe.extractor.graphql.navigator;

import org.schabi.newpipe.extractor.graphql.fragment.GraphQLFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphQLNavigator extends GraphQLNavigateable {
    public static final char OPEN_MIXIN = '@';
    public static final char OPEN_PARAM = '(';
    public static final char CLOSE_PARAM = ')';
    public static final char CLOSE_BODY = '}';
    public static final char OPEN_BODY = '{';
    public static final char ALIAS_INDICATOR = ':';

    protected Map<String, GraphQLFragment> rootFragments;

    public GraphQLNavigator(GraphQLParameters parameters, Map<String, GraphQLFragment> rootFragments, Map<String, GraphQLNavigateable> rootQuerys, List<String> availData) {
        super(parameters, rootQuerys, availData);
        this.rootFragments = rootFragments;
    }

    public enum CollectTyp {
        FRAGMENT,
        QUERY,
        NAVI,
        NO
    }

    public enum CollectLevel {
        BODY,
        PARAM,
        META,
        ROOT
    }

    public class GraphQLNavigateableBuildItem {
        protected GraphQLNavigator navigator = null;
        protected GraphQLNavigateable navigateable = null;
        protected String key;

        public GraphQLNavigateableBuildItem(String key, GraphQLNavigator navigator, GraphQLNavigateable navigateable) {
            this.key = key;
            this.navigateable = navigateable;
            this.navigator = navigator;
        }

        public GraphQLNavigateable getNavigateable() {
            return navigateable;
        }

        public GraphQLNavigator getNavigator() {
            return navigator;
        }

        public String getKey() {
            return key;
        }
    }

    public static GraphQLNavigator from(String query) {
        return from(query, CollectTyp.NO, CollectLevel.ROOT).getNavigator();
    }

    public static GraphQLNavigateable from(String query) {
        return from(query, CollectTyp.NO, CollectLevel.BODY).getNavigateable();
    }

    public static GraphQLNavigateableBuildItem from(String query, CollectTyp startTyp, CollectLevel startLevel) {
        boolean needFullNavi = startTyp != CollectTyp.NAVI;

        Map<String, GraphQLNavigateable> rootQuerys = new HashMap<String, GraphQLNavigateable>();
        Map<String, GraphQLFragment> rootFragments = new HashMap<String, GraphQLFragment>();
        List<String> availData = new ArrayList<String>();

        CollectTyp collectTyp = startTyp;
        CollectLevel collectLevel = startLevel;

        int inlineBodies = 0;
        int inlineBodyIndex = 0;
        List<StringBuilder> inlineBodyCollectors = new ArrayList<StringBuilder>();

        boolean comment = false;
        boolean isRootElement = false;

        Map<CollectLevel, String> needParseCollector = new HashMap<CollectLevel, String>(3);
        StringBuilder stringCollect = new StringBuilder();
        char[] queryChars = query.toCharArray();
        for (int i = 0; i < queryChars.length; i++) {
            char qchar = queryChars[i];
            boolean last = (i+1) == queryChars.length;

            boolean continueByNo = (!Character.isLetterOrDigit(qchar)) && collectTyp == CollectTyp.NO;
            boolean isDelimiter = qchar == ' ' || qchar == '\n' || qchar == '\t';
            boolean continueByOther = (!Character.isLetterOrDigit(qchar)) && qchar != CLOSE_PARAM && qchar != OPEN_PARAM && qchar != CLOSE_BODY && qchar != OPEN_BODY && qchar != ALIAS_INDICATOR && (!isDelimiter) && collectTyp != CollectTyp.NO;

            if (comment && qchar == '\n') {
                comment = false;
                continue;
            }

            if (comment || continueByNo || continueByOther) {
                stringCollect.setLength(0);
                continue;
            }

            if (qchar == '#') {
                comment = true;
                continue;
            }

            switch (collectLevel) {
                case ROOT:
                    stringCollect.append(qchar);
                    String currentStr = stringCollect.toString();

                    boolean isFragment = currentStr == "fragment";
                    if (isFragment || currentStr == "query") {
                        collectTyp = isFragment ? CollectTyp.FRAGMENT : CollectTyp.QUERY;
                        collectLevel = CollectLevel.META;
                        stringCollect.setLength(0);
                        isRootElement = true;
                    }
                    break;
                case META:
                    boolean openParam = OPEN_PARAM == qchar;
                    if (openParam || OPEN_BODY == qchar) {
                        collectLevel = openParam ? CollectLevel.PARAM : CollectLevel.BODY;
                        if (!openParam) {
                            needParseCollector.put(CollectLevel.PARAM, "");
                        }
                        needParseCollector.put(CollectLevel.META, stringCollect.toString());
                    } else {
                        stringCollect.append(qchar);
                    }
                    break;
                case BODY:
                    if (!isDelimiter) {
                        if (inlineBodies > 0) {
                            StringBuilder builder = inlineBodyCollectors.get(inlineBodyIndex);
                            builder.append(qchar);
                            inlineBodyCollectors.set(inlineBodyIndex, builder);
                        }

                        if (qchar == OPEN_BODY) {
                            inlineBodies++;
                            if (inlineBodies == 1) {
                                inlineBodyCollectors.add(new StringBuilder());
                            }
                        } else if (qchar == CLOSE_BODY) {
                            if (inlineBodies > 0) {
                                if (inlineBodies == 1) {
                                    inlineBodyIndex++;
                                }
                                inlineBodies--;
                            }

                            if (inlineBodies == 0) {
                                for (StringBuilder inlineBodyCollector:inlineBodyCollectors) {
                                    inlineBodyCollector.setLength(inlineBodyCollector.length()-1);
                                    GraphQLNavigateableBuildItem item = from(inlineBodyCollector.toString(), CollectTyp.NAVI, CollectLevel.META);
                                    rootQuerys.put(item.getKey(), item.getNavigateable());
                                }
                                inlineBodies = 0;
                                inlineBodyIndex = 0;
                                inlineBodyCollectors = new ArrayList<StringBuilder>();
                            }
                        }
                    } else {
                        availData.add(stringCollect.toString());
                    }
                    break;
                case PARAM:
                    break;
            }
        }
    }

    public Map<String, GraphQLFragment> getRootFragments() {
        return rootFragments;
    }
}
