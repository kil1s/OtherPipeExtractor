package org.schabi.newpipe.extractor.stream;

/*
 * Created by Christian Schabesberger on 04.03.16.
 *
 * Copyright (C) Christian Schabesberger 2016 <chris.schabesberger@mailbox.org>
 * VideoStream.java is part of NewPipe Extractor.
 *
 * NewPipe Extractor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe Extractor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe Extractor. If not, see <https://www.gnu.org/licenses/>.
 */

import org.schabi.newpipe.extractor.MediaFormat;
import org.schabi.newpipe.extractor.utils.ItagItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VideoStream extends Stream {
    public static final String RESOLUTION_UNKNOWN = "";

    /** @deprecated Use {@link #getResolution()} instead. */
    @Deprecated
    public final String resolution;

    /** @deprecated Use {@link #isVideoOnly()} instead. */
    @Deprecated
    public final boolean isVideoOnly;

    // Fields for DASH
    private int itag = ITAG_NOT_AVAILABLE_OR_NOT_APPLICABLE;
    private int bitrate;
    private int initStart;
    private int initEnd;
    private int indexStart;
    private int indexEnd;
    private int width;
    private int height;
    private int fps;
    private String quality;
    private String codec;
    @Nullable private ItagItem itagItem;

    /**
     * Create a new video stream.
     *
     * @param id             the identifier which uniquely identifies the stream, e.g. for YouTube
     *                       this would be the itag
     * @param content        the content or the URL of the stream, depending on whether isUrl is
     *                       true
     * @param isUrl          whether content is the URL or the actual content of e.g. a DASH
     *                       manifest
     * @param format         the {@link MediaFormat} used by the stream, which can be null
     * @param deliveryMethod the {@link DeliveryMethod} of the stream
     * @param resolution     the resolution of the stream
     * @param isVideoOnly    whether the stream is video-only
     * @param itagItem       the {@link ItagItem} corresponding to the stream, which cannot be null
     * @param manifestUrl    the URL of the manifest this stream comes from (if applicable,
     *                       otherwise null)
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public VideoStream(@Nonnull final String id,
                       @Nonnull final String content,
                       final boolean isUrl,
                       @Nullable final MediaFormat format,
                       @Nonnull final DeliveryMethod deliveryMethod,
                       @Nonnull final String resolution,
                       final boolean isVideoOnly,
                       @Nullable final String manifestUrl,
                       @Nullable final ItagItem itagItem) {
        super(id, content, isUrl, format, deliveryMethod, manifestUrl);
        if (itagItem != null) {
            this.itagItem = itagItem;
            this.itag = itagItem.id;
            this.bitrate = itagItem.getBitrate();
            this.initStart = itagItem.getInitStart();
            this.initEnd = itagItem.getInitEnd();
            this.indexStart = itagItem.getIndexStart();
            this.indexEnd = itagItem.getIndexEnd();
            this.codec = itagItem.getCodec();
            this.height = itagItem.getHeight();
            this.width = itagItem.getWidth();
            this.quality = itagItem.getQuality();
            this.fps = itagItem.getFps();
        }
        this.resolution = resolution;
        this.isVideoOnly = isVideoOnly;
    }

    @Override
    public boolean equalStats(final Stream cmp) {
        return super.equalStats(cmp)
                && cmp instanceof VideoStream
                && resolution.equals(((VideoStream) cmp).resolution)
                && isVideoOnly == ((VideoStream) cmp).isVideoOnly;
    }

    /**
     * Get the video resolution.
     *
     * <p>
     * It can be unknown for some streams, like for HLS master playlists. In this case,
     * {@link #RESOLUTION_UNKNOWN} is returned by this method.
     * </p>
     *
     * @return the video resolution or {@link #RESOLUTION_UNKNOWN}
     */
    @Nonnull
    public String getResolution() {
        return resolution;
    }

    /**
     * Return whether the stream is video-only.
     *
     * <p>
     * Video-only streams have no audio.
     * </p>
     *
     * @return {@code true} if this stream is video-only, {@code false} otherwise
     */
    public boolean isVideoOnly() {
        return isVideoOnly;
    }

    /**
     * Get the itag identifier of the stream.
     *
     * <p>
     * Always equals to {@link #ITAG_NOT_AVAILABLE_OR_NOT_APPLICABLE} for other streams than the
     * ones of the YouTube service.
     * </p>
     *
     * @return the number of the {@link ItagItem} passed in the constructor of the video stream.
     */
    public int getItag() {
        return itag;
    }

    /**
     * Get the bitrate of the stream.
     *
     * @return the bitrate set from the {@link ItagItem} passed in the constructor of the stream.
     */
    public int getBitrate() {
        return bitrate;
    }

    /**
     * Get the initialization start of the stream.
     *
     * @return the initialization start value set from the {@link ItagItem} passed in the
     * constructor of the
     * stream.
     */
    public int getInitStart() {
        return initStart;
    }

    /**
     * Get the initialization end of the stream.
     *
     * @return the initialization end value set from the {@link ItagItem} passed in the constructor
     * of the stream.
     */
    public int getInitEnd() {
        return initEnd;
    }

    /**
     * Get the index start of the stream.
     *
     * @return the index start value set from the {@link ItagItem} passed in the constructor of the
     * stream.
     */
    public int getIndexStart() {
        return indexStart;
    }

    /**
     * Get the index end of the stream.
     *
     * @return the index end value set from the {@link ItagItem} passed in the constructor of the
     * stream.
     */
    public int getIndexEnd() {
        return indexEnd;
    }

    /**
     * Get the width of the video stream.
     *
     * @return the width set from the {@link ItagItem} passed in the constructor of the
     * stream.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the video stream.
     *
     * @return the height set from the {@link ItagItem} passed in the constructor of the
     * stream.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the frames per second of the video stream.
     *
     * @return the frames per second set from the {@link ItagItem} passed in the constructor of the
     * stream.
     */
    public int getFps() {
        return fps;
    }

    /**
     * Get the quality of the stream.
     *
     * @return the quality label set from the {@link ItagItem} passed in the constructor of the
     * stream.
     */
    public String getQuality() {
        return quality;
    }

    /**
     * Get the codec of the stream.
     *
     * @return the codec set from the {@link ItagItem} passed in the constructor of the stream.
     */
    public String getCodec() {
        return codec;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public ItagItem getItagItem() {
        return itagItem;
    }
}
