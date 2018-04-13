package org.schabi.newpipe.extractor.url.model.params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UrlParamsQuery extends HashMap<String, List<String>> implements UrlParamsQueryInterface {
    private enum GOT {
        NOT_EXIST,
        OPTION,
        KEY_VALUE
    }

    private List<String> options;

    private GOT gotTyp(String key) {
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
        List<String> listValue = this.get(key);
        listValue.add(value);
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
