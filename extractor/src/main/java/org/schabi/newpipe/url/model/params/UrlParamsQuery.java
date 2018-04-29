package org.schabi.newpipe.url.model.params;

import java.util.*;

public class UrlParamsQuery extends HashMap<String, List<String>> implements UrlParamsQueryInterface {
    protected enum GOT {
        NOT_EXIST,
        OPTION,
        KEY_VALUE
    }

    protected List<String> options = new ArrayList<String>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        boolean pseudo = o instanceof UrlPseudoParamsQuery;
        if (o == null || (getClass() != o.getClass() && (!(pseudo)))) {
            return false;
        }
        if (!super.equals(o)) return false;
        UrlParamsQuery that = pseudo ? ((UrlPseudoParamsQuery) o).query : (UrlParamsQuery) o;

        Set<Map.Entry<String, List<String>>> thatEntrySet = that.entrySet();
        Set<Map.Entry<String, List<String>>> entrySet = entrySet();
        if (entrySet.size() != thatEntrySet.size()) {
            return false;
        }


        Iterator<Map.Entry<String, List<String>>> thatEntryIterator = thatEntrySet.iterator();
        Iterator<Map.Entry<String, List<String>>> entryIterator = entrySet.iterator();

        while (thatEntryIterator.hasNext() && entryIterator.hasNext()) {
            Map.Entry<String, List<String>> thatEntry = thatEntryIterator.next();
            Map.Entry<String, List<String>> entry = entryIterator.next();

            if (!(thatEntry.getKey().equals(entry.getKey()))) {
                return false;
            }

            if (!(thatEntry.getValue().equals(entry.getValue()))) {
                return false;
            }
        }

        List<String> thatOptions = that.options;
        if (thatOptions.size() != options.size()) {
            return false;
        }

        for (int i = 0; i < options.size(); i++) {
            String thatOption = options.get(i);
            String option = options.get(i);
            if (!option.equals(thatOption)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "UrlParamsQuery{" +
                "options=" + options.toString() +
                "map=" + mapToString() +
                '}';
    }

    protected String mapToString() {
        StringBuilder map = new StringBuilder();
        for (Map.Entry<String, List<String>> entry:super.entrySet()) {
            map.append('{').append(entry.getKey()).append('[');
            for (String option:entry.getValue()) {
                map.append(option).append(", ");
            }
            map.append("]}, ");
        }
        return map.toString();
    }

    protected GOT gotTyp(String key) {
        if (this.containsKey(key)) {
            return GOT.KEY_VALUE;
        }

        if (options.contains(key)) {
            return GOT.OPTION;
        }

        return GOT.NOT_EXIST;
    }

    @Override
    public void add(String option) {
        options.add(option);
    }

    @Override
    public void add(String key, String value) {
        if (!this.containsKey(key)) {
            this.put(key, new ArrayList<String>());
        }
        List<String> listValue = super.get(key);
        listValue.add(value);
        this.put(key, listValue);
    }

    @Override
    public boolean got(String key) {
        return gotTyp(key) != GOT.NOT_EXIST;
    }

    @Override
    public boolean gotOption(String option) {
        return options.contains(option);
    }

    @Override
    public void remove(String key) {
        GOT typ = gotTyp(key);

        if (typ == GOT.OPTION) {
            options.remove(key);
        }

        if (typ == GOT.KEY_VALUE) {
            super.remove(key);
        }
    }

    @Override
    public void remove(String key, String value) {
        List<String> listValue = super.get(key);
        int index = listValue.indexOf(value);
        if (index > 0) {
            listValue.remove(index);
        }
        super.put(key, listValue);
    }

    @Override
    public List<String> get(String key) {
        List<String> allValues = new ArrayList<String>();

        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            if (option.equals(key)) {
                allValues.add(option);
            }
        }

        if (super.containsKey(key)) {
            allValues.addAll(super.get(key));
        }

        return allValues;
    }

    @Override
    public int size() {
        return super.size()+this.options.size();
    }

    @Override
    public List<String> getOptions() {
        return options;
    }

    public UrlPublicParamsQuery toPublic() {
        return new UrlPublicParamsQuery(this);
    }

    public UrlPrivateParamsQuery toPrivate() {
        return new UrlPrivateParamsQuery(this);
    }
}
