package org.schabi.newpipe.extractor;

/*
 * Created by Christian Schabesberger on 23.08.15.
 *
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * NewPipe.java is part of NewPipe.
 *
 * NewPipe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.github.kil1s.other.http.HttpDownloader;
import com.github.kil1s.other.http.HttpDownloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.manage.ServiceManager;
import org.schabi.newpipe.farm.search.SearchCollectorFactory;
import org.schabi.newpipe.farm.stream.StreamCollectorFactory;

import java.util.Collection;

/**
 * Provides access to streaming services supported by NewPipe.
 */
public class NewPipe/* Tracker */ {
    private HttpDownloader downloader = null;
    private ServiceManager manager;
    private SearchCollectorFactory searchCollectorFactory = null;
    private StreamCollectorFactory streamCollectorFactory = null;

    private NewPipe(ServiceManager manager, HttpDownloader downloader) {
        this.manager = manager;
        this.downloader = downloader;
    }

    public SearchCollectorFactory getSearchCollectorFactory() {
        if (searchCollectorFactory == null) {
            searchCollectorFactory = new SearchCollectorFactory();
        }
        return searchCollectorFactory;
    }

    public StreamCollectorFactory getStreamCollectorFactory() {
        if (streamCollectorFactory == null) {
            streamCollectorFactory = new StreamCollectorFactory();
        }
        return streamCollectorFactory;
    }

    public HttpDownloader getDownloader() {
        return downloader;
    }

    public Collection<StreamingService> getServices() {
        return manager.all();
    }

    public StreamingService getService(int serviceId) throws ExtractionException {
        for (StreamingService service : manager.all()) {
            if (service.getServiceId() == serviceId) {
                return service;
            }
        }
        throw new ExtractionException("There's no service with the id = \"" + serviceId + "\"");
    }

    public StreamingService getService(String serviceName) throws ExtractionException {
        for (StreamingService service : manager.all()) {
            if (service.getServiceInfo().getName().equals(serviceName)) {
                return service;
            }
        }
        throw new ExtractionException("There's no service with the name = \"" + serviceName + "\"");
    }

    public StreamingService getServiceByUrl(String url) throws ExtractionException {
        for (StreamingService service : manager.all()) {
            if (service.getLinkTypeByUrl(url) != StreamingService.LinkType.NONE) {
                return service;
            }
        }
        throw new ExtractionException("No service can handle the url = \"" + url + "\"");
    }

    public int getIdOfService(String serviceName) {
        try {
            return getService(serviceName).getServiceId();
        } catch (ExtractionException ignored) {
            return -1;
        }
    }

    public String getNameOfService(int id) {
        try {
            return getService(id).getServiceInfo().getName();
        } catch (Exception e) {
            System.err.println("Service id not known");
            e.printStackTrace();
            return "<unknown>";
        }
    }
}
