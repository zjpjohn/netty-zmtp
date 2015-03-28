/*
 * Copyright (c) 2012-2013 Spotify AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.spotify.netty4.handler.codec.zmtp;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.AbstractReferenceCounted;

import static io.netty.util.CharsetUtil.UTF_8;
import static java.util.Arrays.asList;

public class ZMTPMessage extends AbstractReferenceCounted {

  private final List<ByteBuf> frames;

  public ZMTPMessage(final List<ByteBuf> frames) {
    this.frames = frames;
  }

  @Override
  public ZMTPMessage retain() {
    super.retain();
    return this;
  }

  @Override
  public ZMTPMessage retain(final int increment) {
    super.retain(increment);
    return this;
  }

  /**
   * Create a new message from a string frames, using UTF-8 encoding.
   */
  public static ZMTPMessage fromUTF8(final String... strings) {
    return from(UTF_8, strings);
  }

  /**
   * Create a new message from a list of string frames, using UTF-8 encoding.
   */
  public static ZMTPMessage fromUTF8(final List<String> strings) {
    return from(UTF_8, strings);
  }

  /**
   * Create a new message from a list of string frames, using a specified encoding.
   */
  public static ZMTPMessage from(final Charset charset, final String... strings) {
    return from(charset, asList(strings));
  }

  /**
   * Create a new message from a list of string frames, using a specified encoding.
   */
  @SuppressWarnings("ForLoopReplaceableByForEach")
  public static ZMTPMessage from(final Charset charset, final List<String> strings) {
    final int n = strings.size();
    final List<ByteBuf> frames = new ArrayList<ByteBuf>(n);
    for (int i = 0; i < n; i++) {
      frames.add(Unpooled.copiedBuffer(strings.get(i), charset));
    }
    return from(frames);
  }

  /**
   * Create a new message from a list of frames.
   */
  public static ZMTPMessage from(final List<ByteBuf> frames) {
    return new ZMTPMessage(frames);
  }

  public int size() {
    return frames.size();
  }

  /**
   * @return Current list of content in the message
   */
  public List<ByteBuf> frames() {
    return frames;
  }

  /**
   * Get a specific frame.
   */
  public ByteBuf frame(final int i) {
    return frames.get(i);
  }

  @Override
  protected void deallocate() {
    for (final ByteBuf frame : frames) {
      frame.release();
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    final ZMTPMessage that = (ZMTPMessage) o;

    if (frames != null ? !frames.equals(that.frames) : that.frames != null) { return false; }

    return true;
  }

  @Override
  public int hashCode() {
    return frames != null ? frames.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "ZMTPMessage{" + ZMTPUtils.toString(frames) + '}';
  }
}
