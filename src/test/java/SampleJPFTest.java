import org.junit.Test;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.listener.PreciseRaceDetector;
import gov.nasa.jpf.util.test.TestJPF;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.vm.Verify;

/**
 * Created by dan on 7/3/17.
 */
public class SampleJPFTest extends TestJPF {

  @Test
  public void test() throws InterruptedException {
    Mutate i = new Mutate();
//    if(verifyNoPropertyViolation("jpf.listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
    if(verifyNoPropertyViolation()) {
      Verify.incrementCounter(0);
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
    } else {
      assertEquals(1, Verify.getCounter(0));
    }
  }

  @Test(expected = AssertionError.class)
  public void testRace() throws InterruptedException {
    AtomicInteger i = new AtomicInteger();
    if(verifyNoPropertyViolation()) {

      //An unsafe increment. Two threads could
      //call i.get before i.set, and i will
      //end up being 1 instead of the expected 2
      Runnable badIncrement = () -> {
        int value = i.get();
        i.set(value + 1);
      };

      Thread t1 = new Thread(badIncrement);
      Thread t2 = new Thread(badIncrement);
      t1.start();
      t2.start();
      t1.join();
      t2.join();
      assertEquals(2, i.get());
    }
  }

  private static class Mutate {
    int value = 0;
  }
}
