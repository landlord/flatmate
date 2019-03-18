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

/**
 * Defines a condition that must become true before Flatmate will start the next application.
 *
 * <p>Currently, only TCP readiness checks are defined.
 */
public interface ReadinessCheck {
  default void waitUntilReady() throws Exception {
    while (!isReady()) {
      Thread.sleep(1000);
    }
  }

  /**
   * Defines if the check is ready. This will be polled indefinitely until either an exception is
   * thrown (which will cause the JVM to exit) or true is returned.
   */
  boolean isReady() throws Exception;
}
