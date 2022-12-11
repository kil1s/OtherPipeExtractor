package org.schabi.newpipe.extractor.stream;

import org.schabi.newpipe.extractor.*;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;

import java.util.List;

/*
 * Created by Christian Schabesberger on 26.08.15.
 *
 * Copyright (C) Christian Schabesberger 2016 <chris.schabesberger@mailbox.org>
 * StreamInfo.java is part of NewPipe.
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

/**
 * Info object for opened videos, ie the video ready to play.
 */
public class StreamInfo extends Info {
    public static class StreamExtractException extends ExtractionException {
        StreamExtractException(String message) {
            super(message);
        }
    }

    public StreamInfo(int serviceId, String url, String originalUrl, StreamType streamType, String id, String name, int ageLimit) {
        super(serviceId, id, url, originalUrl, name);
        this.streamType = streamType;
        this.ageLimit = ageLimit;
    }

    private StreamType streamType;
    private String thumbnailUrl;
    private String uploadDate;
    private long duration = -1;
    private int ageLimit = -1;
    private String description;

    private long viewCount = -1;
    private long likeCount = -1;
    private long dislikeCount = -1;

    private String uploaderName;
    private String uploaderUrl;
    private String uploaderAvatarUrl;

    private List<VideoStream> videoStreams;
    private List<AudioStream> audioStreams;
    private List<VideoStream> videoOnlyStreams;

    private String dashMpdUrl;
    private String hlsUrl;
    private StreamInfoItem nextVideo;
    private List<InfoItem> relatedStreams;

    private long startPosition = 0;
    private List<Subtitles> subtitles;

    /**
     * Get the stream type
     *
     * @return the stream type
     */
    public StreamType getStreamType() {
        return streamType;
    }

    public void setStreamType(StreamType streamType) {
        this.streamType = streamType;
    }

    /**
     * Get the thumbnail url
     *
     * @return the thumbnail url as a string
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * Get the duration in seconds
     *
     * @return the duration in seconds
     */
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * Get the number of likes.
     *
     * @return The number of likes or -1 if this information is not available
     */
    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * Get the number of dislikes.
     *
     * @return The number of likes or -1 if this information is not available
     */
    public long getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(long dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getUploaderUrl() {
        return uploaderUrl;
    }

    public void setUploaderUrl(String uploaderUrl) {
        this.uploaderUrl = uploaderUrl;
    }

    public String getUploaderAvatarUrl() {
        return uploaderAvatarUrl;
    }

    public void setUploaderAvatarUrl(String uploaderAvatarUrl) {
        this.uploaderAvatarUrl = uploaderAvatarUrl;
    }

    public List<VideoStream> getVideoStreams() {
        return videoStreams;
    }

    public void setVideoStreams(List<VideoStream> videoStreams) {
        this.videoStreams = videoStreams;
    }

    public List<AudioStream> getAudioStreams() {
        return audioStreams;
    }

    public void setAudioStreams(List<AudioStream> audioStreams) {
        this.audioStreams = audioStreams;
    }

    public List<VideoStream> getVideoOnlyStreams() {
        return videoOnlyStreams;
    }

    public void setVideoOnlyStreams(List<VideoStream> videoOnlyStreams) {
        this.videoOnlyStreams = videoOnlyStreams;
    }

    public String getDashMpdUrl() {
        return dashMpdUrl;
    }

    public void setDashMpdUrl(String dashMpdUrl) {
        this.dashMpdUrl = dashMpdUrl;
    }

    public String getHlsUrl() {
        return hlsUrl;
    }

    public void setHlsUrl(String hlsUrl) {
        this.hlsUrl = hlsUrl;
    }

    public StreamInfoItem getNextVideo() {
        return nextVideo;
    }

    public void setNextVideo(StreamInfoItem nextVideo) {
        this.nextVideo = nextVideo;
    }

    public List<InfoItem> getRelatedStreams() {
        return relatedStreams;
    }

    public void setRelatedStreams(List<InfoItem> relatedStreams) {
        this.relatedStreams = relatedStreams;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public List<Subtitles> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<Subtitles> subtitles) {
        this.subtitles = subtitles;
    }

}
