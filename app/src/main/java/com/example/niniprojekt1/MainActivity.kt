package com.example.niniprojekt1

import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.niniprojekt1.databinding.ActivityMainBinding
import com.google.android.material.resources.CancelableFontCallback.ApplyFont

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productListActivity = Intent(applicationContext, ProductListActivity::class.java)
        val optionsActivity = Intent(applicationContext, OptionsActivity::class.java)


        binding.buttonList.setOnClickListener{
            startActivity(productListActivity)
        }

        binding.buttonOptions.setOnClickListener {
            startActivity(optionsActivity)
        }

        sp = getSharedPreferences("mainSP",Context.MODE_PRIVATE)
        editor = sp.edit()

        if (sp.getBoolean("nightMode",false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        delegate.applyDayNight()


    }

    override fun onStart() {
        super.onStart()

        if (sp.getBoolean("backgroundColor",false)){
            binding.buttonList.setBackgroundColor(getColor(R.color.green))
            binding.buttonOptions.setBackgroundColor(getColor(R.color.green))
        }else{
            binding.buttonList.setBackgroundColor(getColor(R.color.purple_200))
            binding.buttonOptions.setBackgroundColor(getColor(R.color.purple_200))
        }
    }
}



