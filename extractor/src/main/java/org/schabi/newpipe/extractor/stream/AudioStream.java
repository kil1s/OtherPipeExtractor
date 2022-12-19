package org.schabi.newpipe.extractor.stream;

/*
 * Created by Christian Schabesberger on 04.03.16.
 *
 * Copyright (C) Christian Schabesberger 2016 <chris.schabesberger@mailbox.org>
 * AudioStream.java is part of NewPipe Extractor.
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
import java.util.Objects;

public class AudioStream extends Stream {
    public static final int UNKNOWN_BITRATE = -1;

    private final int averageBitrate;

    // Fields for DASH
    private int itag = ITAG_NOT_AVAILABLE_OR_NOT_APPLICABLE;
    private int bitrate;
    private int initStart;
    private int initEnd;
    private int indexStart;
    private int indexEnd;
    private String quality;
    private String codec;

    // Fields about the audio track id/name
    private String audioTrackId;
    private String audioTrackName;
    @Nullable
    private ItagItem itagItem;

    /**
     * Create a new audio stream.
     *
     * @param id             the identifier which uniquely identifies the stream, e.g. for YouTube
     *                       this would be the itag
     * @param content        the content or the URL of the stream, depending on whether isUrl is
     *                       true
     * @param isUrl          whether content is the URL or the actual content of e.g. a DASH
     *                       manifest
     * @param format         the {@link MediaFormat} used by the stream, which can be null
     * @param deliveryMethod the {@link DeliveryMethod} of the stream
     * @param averageBitrate the average bitrate of the stream (which can be unknown, see
     *                       {@link #UNKNOWN_BITRATE})
     * @param audioTrackId   the id of the audio track
     * @param audioTrackName the name of the audio track
     * @param itagItem       the {@link ItagItem} corresponding to the stream, which cannot be null
     * @param manifestUrl    the URL of the manifest this stream comes from (if applicable,
     *                       otherwise null)
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public AudioStream(@Nonnull final String id,
                       @Nonnull final String content,
                       final boolean isUrl,
                       @Nullable final MediaFormat format,
                       @Nonnull final DeliveryMethod deliveryMethod,
                       final int averageBitrate,
                       @Nullable final String manifestUrl,
                       @Nullable final String audioTrackId,
                       @Nullable final String audioTrackName,
                       @Nullable final ItagItem itagItem) {
        super(id, content, isUrl, format, deliveryMethod, manifestUrl);
        if (itagItem != null) {
            this.itagItem = itagItem;
            this.itag = itagItem.id;
            this.quality = itagItem.getQuality();
            this.bitrate = itagItem.getBitrate();
            this.initStart = itagItem.getInitStart();
            this.initEnd = itagItem.getInitEnd();
            this.indexStart = itagItem.getIndexStart();
            this.indexEnd = itagItem.getIndexEnd();
            this.codec = itagItem.getCodec();
        }
        this.averageBitrate = averageBitrate;
        this.audioTrackId = audioTrackId;
        this.audioTrackName = audioTrackName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equalStats(final Stream cmp) {
        return super.equalStats(cmp) && cmp instanceof AudioStream
                && averageBitrate == ((AudioStream) cmp).averageBitrate
                && Objects.equals(audioTrackId, ((AudioStream) cmp).audioTrackId);
    }

    /**
     * Get the average bitrate of the stream.
     *
     * @return the average bitrate or {@link #UNKNOWN_BITRATE} if it is unknown
     */
    public int getAverageBitrate() {
        return averageBitrate;
    }

    /**
     * Get the itag identifier of the stream.
     *
     * <p>
     * Always equals to {@link #ITAG_NOT_AVAILABLE_OR_NOT_APPLICABLE} for other streams than the
     * ones of the YouTube service.
     * </p>
     *
     * @return the number of the {@link ItagItem} passed in the constructor of the audio stream.
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
     * constructor of the stream.
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
     * Get the id of the audio track.
     *
     * @return the id of the audio track
     */
    @Nullable
    public String getAudioTrackId() {
        return audioTrackId;
    }

    /**
     * Get the name of the audio track.
     *
     * @return the name of the audio track
     */
    @Nullable
    public String getAudioTrackName() {
        return audioTrackName;
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
