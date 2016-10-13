import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

def timeout = 10
def delay = 0

def procsAvail = Runtime.getRuntime().availableProcessors()

log "Start stressing CPU for ${timeout}s after waiting for ${delay}s"

ExecutorService executor = Executors.newFixedThreadPool(procsAvail)
println "Create $procsAvail concurrent tasks"
(1..procsAvail).each { it ->
  executor.submit(new Callable() {
    @Override
    Object call() throws Exception {
      while (!Thread.currentThread().isInterrupted()) {
        fib 138
      }
    }
  })
}


if (!executor.awaitTermination(timeout, TimeUnit.SECONDS)) {
  log "Terminate stressing"
  executor.shutdownNow()
  if (!executor.awaitTermination(2, TimeUnit.SECONDS))
    log "Did not terminate"
}

executor.shutdownNow()

log "The end."
System.exit(0)

/**
 * Calc fibonacci number. Starts to take noticeable time when n > 42
 */
def fib(long n) {
  if (n == 1 || n == 2) {
    return 1;
  } else {
    return fib(n - 1) + fib(n - 2);
  }
}


def log(msg) {
  println " ${new Date().format("yyyy-MM-dd HH:mm:ss")} - $msg"
}