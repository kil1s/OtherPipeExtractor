package org.schabi.newpipe.farm.stream.mpd;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import org.schabi.newpipe.extractor.MediaFormat;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.Stream;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.extractor.stream.VideoStream;
import org.schabi.newpipe.extractor.utils.ItagItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * Created by Christian Schabesberger on 02.02.16.
 *
 * Copyright (C) Christian Schabesberger 2016 <chris.schabesberger@mailbox.org>
 * DashMpdParser.java is part of NewPipe.
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

public class DashMpdParser {
    private HttpDownloader downloader;

    public DashMpdParser(HttpDownloader downloader) {
        this.downloader = downloader;
    }

    public static /* sad :-) */ class DashMpdParsingException extends ParsingException {
        DashMpdParsingException(String message, Exception e) {
            super(message, e);
        }
    }

    /**
     * Will try to download (using {@link StreamInfo#dashMpdUrl}) and parse the dash manifest,
     * then it will search for any stream that the ItagItem has (by the id).
     * <p>
     * It has video, video only and audio streams and will only add to the list if it don't
     * find a similar stream in the respective lists (calling {@link Stream#equalStats}).
     *
     * @param streamInfo where the parsed streams will be added
     */
    public void getStreams(StreamInfo streamInfo) throws DashMpdParsingException, ReCaptchaException {
        String dashDoc;
        try {
            dashDoc = downloader.download(streamInfo.getDashMpdUrl());
        } catch (IOException ioe) {
            throw new DashMpdParsingException("Could not get dash mpd: " + streamInfo.getDashMpdUrl(), ioe);
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(dashDoc.getBytes());

            Document doc = builder.parse(stream);
            NodeList representationList = doc.getElementsByTagName("Representation");

            for (int i = 0; i < representationList.getLength(); i++) {
                Element representation = ((Element) representationList.item(i));
                try {
                    String mimeType = ((Element) representation.getParentNode()).getAttribute("mimeType");
                    String id = representation.getAttribute("id");
                    String url = representation.getElementsByTagName("BaseURL").item(0).getTextContent();
                    ItagItem itag = ItagItem.getItag(Integer.parseInt(id));
                    if (itag != null) {
                        MediaFormat mediaFormat = MediaFormat.getFromMimeType(mimeType);

                        if (itag.itagType.equals(ItagItem.ItagType.AUDIO)) {
                            AudioStream audioStream = new AudioStream(url, mediaFormat, itag.avgBitrate);

                            if (!Stream.containSimilarStream(audioStream, streamInfo.getAudioStreams())) {
                                streamInfo.getAudioStreams().add(audioStream);
                            }
                        } else {
                            boolean isVideoOnly = itag.itagType.equals(ItagItem.ItagType.VIDEO_ONLY);
                            VideoStream videoStream = new VideoStream(url, mediaFormat, itag.resolutionString, isVideoOnly);

                            if (isVideoOnly) {
                                if (!Stream.containSimilarStream(videoStream, streamInfo.getVideoOnlyStreams())) {
                                    streamInfo.getVideoOnlyStreams().add(videoStream);
                                }
                            } else if (!Stream.containSimilarStream(videoStream, streamInfo.getVideoStreams())) {
                                streamInfo.getVideoStreams().add(videoStream);
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            throw new DashMpdParsingException("Could not parse Dash mpd", e);
        }
    }
}
