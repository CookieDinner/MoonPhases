package com.example.moonphases

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {
    var algorithmChoice : Int? = null
    var hemisphereChoice: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        algorithmChoice = intent.extras?.getInt("algorithmChoice")
        hemisphereChoice = intent.extras?.getInt("hemisphereChoice")
        when(algorithmChoice){
            0 -> algorithmGroup.check(R.id.simple)
            1 -> algorithmGroup.check(R.id.conway)
            2 -> algorithmGroup.check(R.id.trig1)
            3 -> algorithmGroup.check(R.id.trig2)
        }
        algorithmGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.simple -> algorithmChoice = 0
                R.id.conway -> algorithmChoice = 1
                R.id.trig1 -> algorithmChoice = 2
                R.id.trig2 -> algorithmChoice = 3
            }
        }
        when(hemisphereChoice){
            0 -> hemisphereGroup.check(R.id.northern)
            1 -> hemisphereGroup.check(R.id.southern)
        }
        hemisphereGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.northern -> hemisphereChoice = 0
                R.id.southern -> hemisphereChoice = 1
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == android.R.id.home) {
            finish()
            true
        } else
            super.onOptionsItemSelected(item)
    }

    override fun finish() {
        val data = Intent()
        data.putExtra("newAlgorithmChoice", algorithmChoice)
        data.putExtra("newHemisphereChoice", hemisphereChoice)
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }
}