package com.example.moonphases

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_fullmoons.*
import java.util.*
import kotlin.collections.ArrayList

class FullmoonsActivity : AppCompatActivity() {

    val array = ArrayList<TextView>()
    private val utils = Utils()
    private var algorithmChoice : Int? = null
    var lastCorrectYear : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullmoons)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val textInput = findViewById<EditText>(R.id.yearTextInput)
        val currentYear = intent.extras?.getString("currentYear")
        lastCorrectYear = currentYear?.toInt()
        algorithmChoice = intent.extras?.getInt("algorithmChoice")
        if(array.isEmpty()){
            array.add(textView1);array.add(textView2);array.add(textView3);array.add(textView4);array.add(textView5);array.add(textView6)
            array.add(textView7);array.add(textView8);array.add(textView9);array.add(textView10);array.add(textView11);array.add(textView12)
        }
        textInput.setText(currentYear)
        updateDates(algorithmChoice,currentYear?.toInt())
        textInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                textInput.removeTextChangedListener(this)
                val regex = """[^0-9]""".toRegex()
                val matched = regex.containsMatchIn(input = editable.toString())
                when {
                    matched -> {
                        textInput.setText(lastCorrectYear.toString())
                        textInput.setSelection(textInput.text.toString().length)
                    }
                    editable.toString().length > 4 -> {
                        textInput.setText(lastCorrectYear.toString())
                        textInput.setSelection(textInput.text.toString().length)
                    }
                    editable.toString().isNotEmpty() -> {
                        lastCorrectYear = editable.toString().toInt()
                        textInput.setSelection(textInput.text.toString().length)
                    }
                }
                textInput.addTextChangedListener(this)
                if(textInput.text.isNotEmpty() && !matched)
                    updateDates(algorithmChoice, textInput.text.toString().toInt())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    fun updateDates(algorithmChoice: Int?, currentYear: Int?){
        val calendar = Calendar.getInstance()
        if (currentYear != null) {
            calendar.set(currentYear.toInt(), 0, 1)
        }
        if(algorithmChoice!=null)
            for (i in 1..12) {
                array[i - 1].text =
                    utils.getNextFullMoon(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        algorithmChoice
                    )
                calendar.add(Calendar.DAY_OF_MONTH, 30)
            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == android.R.id.home) {
            finish()
            true
        } else
            super.onOptionsItemSelected(item)
    }

    fun nextYearClick(v: View){
        yearTextInput.setText((yearTextInput.text.toString().toInt() + 1).toString())
    }
    fun previousYearClick(v: View){
        yearTextInput.setText((yearTextInput.text.toString().toInt() - 1).toString())
    }
}
