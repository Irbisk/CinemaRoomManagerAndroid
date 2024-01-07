package org.hyperskill.cinema

import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.core.view.size
import org.hyperskill.cinema.abstraction.AbstractUnitTest
import org.hyperskill.cinema.abstraction.find
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

//Version 04.2022 B
@RunWith(RobolectricTestRunner::class)
class Stage1UnitTest : AbstractUnitTest<MainActivity>(MainActivity::class.java) {

    private val `cinema room places`: GridLayout by lazy {
        activity.find("cinema_room_places")
    }

    private val `for screen text view`: TextView by lazy {
        activity.find("cinema_room_screen_text")
    }


    @Test
    fun `test should check screen view text`() {
        val message = "View with id \"cinema_room_screen_text\" should contain \"Screen\" as text."

        activityController.`launch this activity and execute` {
            `for screen text view`.`text should be`(errorMessage = message, "Screen")
        }
    }

    @Test
    fun `test should check places columns count`() {
        val message = "View with id \"cinema_room_places\" should contain the correct amount of columns."

        activityController.`launch this activity and execute` {
            assertEquals(message, 8, `cinema room places`.columnCount)
        }
    }

    @Test
    fun `test should check places rows count`() {
        val message = "View with id \"cinema_room_places\" should contain the correct amount of rows."

        activityController.`launch this activity and execute` {
            assertEquals(message, 7, `cinema room places`.rowCount)
        }
    }

    @Test
    fun `test should check places seats`() {
        val message = "View with id \"cinema_room_places\" should contain the correct seat number."

        activityController.`launch this activity and execute` {
            `cinema room places`.also { gridLayout ->
                assertEquals(56, gridLayout.size)
            }.iterator().asSequence().map { eitherSeatOrSeatWrapper ->
                eitherSeatOrSeatWrapper.find<TextView>("cinema_room_place_item_text")
            }.forEachIndexed { index, seat ->
                val seatRow = index / 8 + 1
                val seatColumn = index % 8 + 1

                val seatText = seat.text
                assertEquals(message, "${seatRow}.${seatColumn}", seatText)
            }
        }
    }

    @Test
    fun `columns should be displayed in order`() {
        activityController.`launch this activity and execute` {
            val rowSize  = `cinema room places`.rowCount
            val colSize = `cinema room places`.columnCount

            for(row in 0 until rowSize) {
                var lastLeftPx = Int.MIN_VALUE
                for(col in 0 until colSize) {
                    val index = row * colSize + col
                    val currentLeftPx = `cinema room places`[index].left
                    assertTrue("Columns should be displayed in order with crescent column numbers", currentLeftPx > lastLeftPx)
                    lastLeftPx = currentLeftPx
                }
            }
        }
    }

    @Test
    fun `rows should be displayed in order`() {
        activityController.`launch this activity and execute` {
            val rowSize  = `cinema room places`.rowCount
            val colSize = `cinema room places`.columnCount

            for(col in 0 until colSize) {
                var lastTopPx = Int.MIN_VALUE
                for(row in 0 until rowSize) {
                    val index = row * colSize + col
                    val currentTopPx = `cinema room places`[index].top
                    assertTrue("Rows should be displayed in order with crescent row numbers", currentTopPx > lastTopPx)
                    lastTopPx = currentTopPx
                }
            }
        }
    }
}