/**
 * Copyright 2017 Dan Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.junit.Test;

import gov.nasa.jpf.util.test.TestJPF;

public class SampleJPFTest extends TestJPF {

  @Test(expected =  AssertionError.class)
  public void test() throws InterruptedException {
    Mutate i = new Mutate();
    if(verifyNoPropertyViolation()) {
      Runnable badIncrement = () -> {
        i.value++;
      };

      Thread t1 = new Thread(badIncrement);
      Thread t2 = new Thread(badIncrement);
      t1.start();
      t2.start();
      t1.join();
      t2.join();
      assertEquals(2, i.value);
    }
  }

  private static class Mutate {
    int value = 0;
  }
}
