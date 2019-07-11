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

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpReadinessCheckTests {
  @Test
  public void test() throws Exception {
    final long startTimeMs = System.nanoTime();
    final long endTimeMs = startTimeMs + 10000000000L;

    final ServerSocket initialServerSocket = new ServerSocket(0);
    final int port = initialServerSocket.getLocalPort();
    initialServerSocket.close();

    final TcpReadinessCheck tcpReadinessCheck = new TcpReadinessCheck("localhost", port);

    assertFalse(tcpReadinessCheck.isReady());

    final ServerSocket serverSocket = new ServerSocket(port);

    new Thread(
            () -> {
              try {

                while (System.nanoTime() < endTimeMs) {
                  serverSocket.accept().close();
                }

                serverSocket.close();
              } catch (IOException e1) {
                try {
                  serverSocket.close();
                } catch (IOException e2) {
                  RuntimeException e3 = new RuntimeException(e2);
                  e1.addSuppressed(e1);
                  throw e3;
                }

                throw new RuntimeException(e1);
              }
            })
        .start();

    tcpReadinessCheck.waitUntilReady();
  }
}
