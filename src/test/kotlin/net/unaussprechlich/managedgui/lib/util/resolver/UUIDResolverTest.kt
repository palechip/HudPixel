package net.unaussprechlich.managedgui.lib.util.resolver

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class UUIDResolverTest{

    @Test
    fun UUIDResolverTester(){
        var result1 = ""
        var result2 = ""
        UUIDResolver("unaussprechlich") {
            result1 = it.toString()
        }
        UUIDResolver("evwgrinjounoiujbrvwenouipbwver") {
            result2 = it.toString()
        }
        Assertions.assertEquals("4064d7ec-c212-4a1c-b252-ecc0403a2824", result1)
        Assertions.assertEquals("00000000-0000-0000-0000-000000000000", result2)
    }

}