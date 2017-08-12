import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.listener.PreciseRaceDetector;
import gov.nasa.jpf.util.script.ESParser.Exception;
import gov.nasa.jpf.util.test.TestJPF;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.vm.Verify;

/**
 * Created by dan on 7/3/17.
 */
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
