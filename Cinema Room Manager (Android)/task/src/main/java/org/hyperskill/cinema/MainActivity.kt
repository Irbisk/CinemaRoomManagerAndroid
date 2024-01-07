package org.hyperskill.cinema

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.GridLayout
import android.widget.LinearLayout

import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import org.hyperskill.cinema.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var totalIncome = 0.0
    private var currentIncome = 0.0
    private var availableSeats = 56
    private var occupiedSeats = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        val layout =  binding.cinemaRoomPlaces

        for (i in 0..6) {
            for (j in 0..7) {
                layout.addView(CardView(this).apply {
                    layoutParams = GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f)).apply {
                        width = 0
                        height = 0
                        setMargins(5)
                        setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
                    }
                    val row = i + 1
                    val seat = j + 1
                    val seatNumber = "$row.$seat"

                    val step = 1.0 / 7
                    val ticketPrice = (1.5 - (i * step)) * getTicketPrice()

                    this.addView(TextView(context).apply {
                        id = R.id.cinema_room_place_item_text
                        text = seatNumber
                        gravity = Gravity.CENTER
                        setTextColor(resources.getColor(R.color.white))
                    }, LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

                    totalIncome += ticketPrice



                    this.setOnClickListener {

                        if (cardBackgroundColor.defaultColor != Color.DKGRAY) {
                            AlertDialog.Builder(context).apply {
                                id = R.id.cinema_room_place_indicator
                                setTitle("Buy a ticket $row row $seat place")
                                setMessage(String.format(context.getString(R.string.your_ticket_price_is), ticketPrice))
                                setPositiveButton(context.getString(R.string.buy_a_ticket)) { _, _ ->
                                    setCardBackgroundColor(Color.DKGRAY)
                                    Toast.makeText(this@MainActivity, String.format("%.2f$", ticketPrice), Toast.LENGTH_SHORT).show()


                                    currentIncome += ticketPrice
                                    availableSeats--
                                    occupiedSeats++

                                    binding.cinemaRoomCurrentIncome.text =
                                        String.format(context.getString(R.string.current_cinema_income), currentIncome)
                                    binding.cinemaRoomAvailableSeats.text =
                                        String.format(context.getString(R.string.available_seats), availableSeats)
                                    binding.cinemaRoomOccupiedSeats.text =
                                        String.format(context.getString(R.string.occupied_seats), occupiedSeats)
                                }
                                setNegativeButton(context.getString(R.string.cancel_purchase), null)
                            }.show()
                        }

                    }

                })

            }
        }
        binding.cinemaRoomTicketPrice.text =
            String.format(getString(R.string.estimated_ticket_price), getTicketPrice())
        binding.cinemaRoomTotalIncome.text = String.format(getString(R.string.total_cinema_income), totalIncome)
        binding.cinemaRoomCurrentIncome.text =
            String.format(this.getString(R.string.current_cinema_income), currentIncome)
        binding.cinemaRoomAvailableSeats.text = String.format(this.getString(R.string.available_seats), availableSeats)
        binding.cinemaRoomOccupiedSeats.text = String.format(this.getString(R.string.occupied_seats), occupiedSeats)

    }


    fun getTicketPrice(): Float {
        val duration = intent.getIntExtra("DURATION", 108)
        val rating = intent.getFloatExtra("RATING", 4.5f)
        val profit = (-1f/90f) * (duration * duration) + (2 * duration) + 90
        return (rating * profit) / 56

    }

}