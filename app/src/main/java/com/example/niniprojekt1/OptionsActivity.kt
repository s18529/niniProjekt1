package com.example.niniprojekt1

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.niniprojekt1.databinding.ActivityOptionsBinding

class OptionsActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: ActivityOptionsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


       //Dark Mode

        sp = getSharedPreferences("mainSP",Context.MODE_PRIVATE)
        editor = sp.edit()

        binding.theme.isChecked = !sp.getBoolean("nightMode",false)

        binding.theme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                editor.putBoolean("nightMode",false)
                editor.apply()

        }else{
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                editor.putBoolean("nightMode",true)
                editor.apply()
            }
        }

        //Background Color
        binding.colorSwitch.isChecked = sp.getBoolean("backgroundColor",false)

        binding.colorSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                editor.putBoolean("backgroundColor",true)
                editor.apply()

            }else{
                editor.putBoolean("backgroundColor",false)
                editor.apply()
            }
        }
    }

    override fun onStart() {
        super.onStart()






//        binding.button2.setOnClickListener {
//            Toast.makeText(binding.et1.context,sp.getString("et2txt","Domyślna"), Toast.LENGTH_SHORT).show()
//        }
//
//        binding.button2.text = sp.getString("et1txt","Domyślna")
//        binding.textView.text = sp.getString("et2txt", "Domyślna")


    }
}