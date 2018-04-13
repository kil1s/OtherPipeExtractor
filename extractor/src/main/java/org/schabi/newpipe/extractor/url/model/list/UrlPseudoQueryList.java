package org.schabi.newpipe.extractor.url.model.list;

import org.schabi.newpipe.extractor.url.model.UrlQuery;

import java.util.*;

public abstract class UrlPseudoQueryList<T> implements UrlQuery, List<T> {
    private UrlQueryList<T> queryList;

    public UrlPseudoQueryList(UrlQueryList<T> queryList) {
        this.queryList = queryList;
    }

    @Override
    public int size() {
        return this.queryList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.queryList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.queryList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.queryList.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.queryList.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return this.queryList.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        return this.queryList.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return this.queryList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return this.queryList.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return this.queryList.addAll(collection);
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        return this.queryList.addAll(i, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return this.queryList.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return this.queryList.retainAll(collection);
    }

    @Override
    public void clear() {
        this.queryList.clear();
    }

    @Override
    public T get(int i) {
        return this.queryList.get(i);
    }

    @Override
    public T set(int i, T t) {
        return this.queryList.set(i, t);
    }

    @Override
    public void add(int i, T t) {
        this.queryList.add(i, t);
    }

    @Override
    public T remove(int i) {
        return this.queryList.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return this.queryList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.queryList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return this.queryList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return this.queryList.listIterator(i);
    }

    @Override
    public List<T> subList(int i, int i1) {
        return this.queryList.subList(i, i1);
    }
}
