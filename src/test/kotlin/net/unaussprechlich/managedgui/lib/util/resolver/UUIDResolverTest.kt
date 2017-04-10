package net.unaussprechlich.managedgui.lib.util.resolver

import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

internal class UUIDResolverTest{

    @Test
    fun UUIDResolverTester() = runBlocking{
        val time  = measureTimeMillis {
            UUIDResolver("unaussprechlich") {
                Assertions.assertEquals("4064d7ec-c212-4a1c-b252-ecc0403a2824", it.toString())
            }
            UUIDResolver("evwgrinjounoiujbrvwenouipbwver") {
                Assertions.assertEquals("00000000-0000-0000-0000-000000000000", it.toString())
            }
        }
        println("TIME: $time ms")
    }

}