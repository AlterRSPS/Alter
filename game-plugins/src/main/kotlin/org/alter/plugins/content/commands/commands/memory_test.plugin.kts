import com.sun.management.HotSpotDiagnosticMXBean
import org.alter.rscm.RSCM.getRSCM
import java.lang.management.ManagementFactory
import java.nio.file.Paths

/**
 * @author CloudS3c 12/30/2024
 */
onCommand("qutest", description = "Check memory usage") {
    repeat(10_000_000) {
        player.queue {
            player.inventory.add(getRSCM("item.coins_995"), 1)
        }
    }
}

onCommand("gc", description = "Garbage Collector - Will free up unused memory.") {
    System.gc()
}

onCommand("heap", description = "Creates heap dump") {
    val outp = Paths.get("../dump.hprof")
    val asFile = outp.toFile()
    if (asFile.exists()) asFile.delete()
    try {
        val mxBean = ManagementFactory.newPlatformMXBeanProxy(
            ManagementFactory.getPlatformMBeanServer(),
            "com.sun.management:type=HotSpotDiagnostic",
            HotSpotDiagnosticMXBean::class.java
        )
        mxBean.dumpHeap(outp.toString(), true)
        println("Heap dump created at: ${outp.toAbsolutePath()}")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}