/*
 * Copyright 2018 Titan Class Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.com.titanclass.flatmate;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

/** A readiness check that waits until the specified host and port are bound. */
public class TcpReadinessCheck implements ReadinessCheck {
  private final String host;
  private final int port;

  TcpReadinessCheck(final String host, final int port) {
    this.host = host;
    this.port = port;
  }

  @Override
  public boolean isReady() {
    boolean ready = false;

    try {
      final Socket socket = new Socket(host, port);

      ready = true;

      socket.close();
    } catch (final IOException e) {
      // note that if close threw the exception, we're ready
    }

    return ready;
  }

  @Override
  public String toString() {
    return "TcpReadinessCheck{" + "host='" + host + '\'' + ", port=" + port + '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final TcpReadinessCheck that = (TcpReadinessCheck) o;
    return port == that.port && Objects.equals(host, that.host);
  }

  @Override
  public int hashCode() {
    return Objects.hash(host, port);
  }
}
