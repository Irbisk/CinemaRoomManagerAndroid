package org.hyperskill.cinema

import android.content.Intent
import android.widget.TextView
import org.hyperskill.cinema.abstraction.AbstractUnitTest
import org.hyperskill.cinema.abstraction.find
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

//Version 04.2022 B
@RunWith(RobolectricTestRunner::class)
class Stage2UnitTest : AbstractUnitTest<MainActivity>(MainActivity::class.java) {

    companion object {
        private const val DOUBLE_ASSERT_DELTA = 0.1
    }

    private val `price text view` : TextView by lazy {
        activity.find("cinema_room_ticket_price")
    }

    @Test
    fun `test should check ticket price view with default arguments`() {
        val message = "Are default DURATION and RATING properties being used?"

        activityController.`launch this activity and execute` {
            `price text view`.`text should contain double`(message, 14.18, DOUBLE_ASSERT_DELTA)
        }
    }

    @Test
    fun `test should check ticket price view with best arguments`() {
        val message = "Are DURATION and RATING properties received from intent?"

        activityController.`launch this activity and execute`(arguments = `most profitable movie`()) {
            `price text view`.`text should contain double`(message, 16.07, DOUBLE_ASSERT_DELTA)
        }
    }

    @Test
    fun `test should check ticket price view with custom arguments`() {
        val message = "Are DURATION and RATING properties received from intent?"

        activityController.`launch this activity and execute`(arguments = `custom profitable movie`()) {
            `price text view`.`text should contain double`(message, 10.59, DOUBLE_ASSERT_DELTA)
        }
    }

    @Test
    fun `test should check ticket price view contains two digits after dot`() {

        activityController.`launch this activity and execute`(arguments = `custom profitable movie`()) {
            val actualText = `price text view`.text
            val message = "Make sure you have correctly formatted the ticket price message. The price should contain two numbers after the dot.\n" +
                    "Expected:<Estimated ticket price: [priceWithTwoDecimals]$>, Found:<$actualText>"

            `price text view`.`text should`() { text ->
                assertTrue(message, text.matches("(?i)^Estimated ticket price: ([1-9][0-9]*|0)(\\.[0-9][0-9])?\\$$".toRegex()))
            }
        }
    }

    private fun `custom profitable movie`() = Intent().apply {
        putExtra("DURATION", 39)
        putExtra("RATING", 3.9f)
    }
}