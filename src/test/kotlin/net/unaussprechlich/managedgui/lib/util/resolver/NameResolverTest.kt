package net.unaussprechlich.managedgui.lib.util.resolver

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

internal class NameResolverTest{

    @Test
    fun NameResolverTester(){
        var result1 = ""
        var result2 = ""
        NameResolver(UUID.fromString("4064d7ec-c212-4a1c-b252-ecc0403a2824")) {
            result1 = it.currentName
        }
        NameResolver(UUID.fromString("00000000-0000-0000-0000-000000000000")) {
            result2 = it.currentName
        }
        Assertions.assertEquals("unaussprechlich", result1)
        Assertions.assertEquals("!ERROR!", result2)
    }
}