package com.example.moonphases

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    val today = Calendar.getInstance()
    var utils = Utils()
    var algorithmChoice :Int? = 2
    var hemisphereChoice : Int? = 0
    var nArray = ArrayList<Int>()
    var sArray = ArrayList<Int>()

    val FULLMOONS_REQUEST_CODE=7
    val SETTINGS_REQUEST_CODE=10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        readSettingsFromFile()
        populateResourceArrays()
        updateMainScreen()
    }
    @SuppressLint("SetTextI18n", "ResourceType")
    fun updateMainScreen(){
        val phaseDay = utils.calculatePhaseDay(today.get(Calendar.YEAR),
            today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH), algorithmChoice)
        todayText.text = "${getString(R.string.today)} ${String.format("%.0f",(phaseDay/30)*100)}%"
        val lastNewMoonDate = Calendar.getInstance()
        lastNewMoonDate.add(Calendar.DAY_OF_MONTH, ((phaseDay-1)*(-1)).toInt())
        lastNewMoon.text = "${getString(R.string.last_newmoon)} ${lastNewMoonDate.get(Calendar.DAY_OF_MONTH)}." +
                "${lastNewMoonDate.get(Calendar.MONTH)+1}." +
                "${lastNewMoonDate.get(Calendar.YEAR)} r."
        lastNewMoonDate.add(Calendar.DAY_OF_MONTH, 15)
        if(lastNewMoonDate.timeInMillis < today.timeInMillis)
            lastNewMoonDate.add(Calendar.DAY_OF_MONTH, 30)
        nextFullMoon.text = "${getString(R.string.next_fullmoon)} ${lastNewMoonDate.get(Calendar.DAY_OF_MONTH)}." +
                "${lastNewMoonDate.get(Calendar.MONTH)+1}." +
                "${lastNewMoonDate.get(Calendar.YEAR)} r."
        if(hemisphereChoice == 0)
            moonImage.setImageResource(nArray[phaseDay.toInt() - 1])
        else
            moonImage.setImageResource(sArray[phaseDay.toInt() - 1])
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings){
            clickSettings()
        }
        return true
    }
    private fun populateResourceArrays(){
        for (i in 1..30) {
            nArray.add(resources.getIdentifier("n$i", "drawable", "com.example.moonphases"))
            sArray.add(resources.getIdentifier("s$i", "drawable", "com.example.moonphases"))
        }
    }
    fun clickButton(v: View){
        val intent = Intent(this, FullmoonsActivity::class.java)
        intent.putExtra("currentYear", today.get(Calendar.YEAR).toString())
        intent.putExtra("algorithmChoice", algorithmChoice)
        startActivity(intent)
    }
    fun clickSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("currentYear", today.get(Calendar.YEAR).toString())
        intent.putExtra("algorithmChoice", algorithmChoice)
        intent.putExtra("hemisphereChoice", hemisphereChoice)
        startActivityForResult(intent, SETTINGS_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if((requestCode==SETTINGS_REQUEST_CODE)
            && (resultCode == Activity.RESULT_OK)){
            if(data!=null){
                if(data.hasExtra("newAlgorithmChoice")){
                    algorithmChoice = data.extras?.getInt("newAlgorithmChoice")
                    hemisphereChoice = data.extras?.getInt("newHemisphereChoice")
                    saveSettingsToFile()
                    updateMainScreen()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun saveSettingsToFile(){
        val filename = "settings"
        val path = this.getExternalFilesDir(null)
        val file = File(path, filename)
        file.writeText("$algorithmChoice$hemisphereChoice")
    }

    fun readSettingsFromFile(){
        try {
            val filename = "settings"
            val path = this.getExternalFilesDir(null)
            val file = File(path, filename)
            if (file.exists()){
                val br = BufferedReader(file.reader())
                val line = br.readLine()
                algorithmChoice = line[0].toString().toInt()
                hemisphereChoice = line[1].toString().toInt()
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun fileExists(path:String):Boolean{
        val file = baseContext.getFileStreamPath(path)
        return file.exists()
    }
}
