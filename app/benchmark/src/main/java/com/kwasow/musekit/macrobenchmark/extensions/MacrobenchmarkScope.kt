package com.kwasow.musekit.macrobenchmark.extensions

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until

fun MacrobenchmarkScope.waitForTextShown(text: String) {
    check(device.wait(Until.hasObject(By.text(text)), 1000)) {
        "View showing '$text' not found after waiting 1000 ms."
    }
}

fun MacrobenchmarkScope.clickOnText(text: String) {
    device
        .findObject(By.text(text))
        .click()
}
