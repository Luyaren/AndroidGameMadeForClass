package io.robotbois.robotboisapp

import junit.framework.Assert.assertEquals


infix fun Any?.shouldBe(other: Any?){
    return assertEquals(other,this)
}