package com.example.niniprojekt1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.niniprojekt1.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = getSharedPreferences("mainSP", Context.MODE_PRIVATE)
        editor = sp.edit()

        var productViewModel = ProductViewModel(application)
        var productadapter = ProductAdapter(productViewModel)

        auth = FirebaseAuth.getInstance()

        val productListActivity = Intent(applicationContext, ProductListActivity::class.java)
        val optionsActivity = Intent(applicationContext, OptionsActivity::class.java)
        val mainActivity = Intent(applicationContext, MainActivity::class.java)


        binding.buttonList.setOnClickListener {
            val mauth = FirebaseAuth.getInstance().currentUser
            if (mauth != null) {
                Toast.makeText(this,mauth.toString(),Toast.LENGTH_LONG).show()
                productListActivity.putExtra("Auth",FirebaseAuth.getInstance().currentUser?.uid)
                startActivity(productListActivity)
            } else{
                    Toast.makeText(this,"najpeirw sie zaloguj",Toast.LENGTH_LONG).show()
                }
        }


        binding.checkButton.setOnClickListener {
            val asd = FirebaseAuth.getInstance().currentUser?.uid
            Toast.makeText(this,asd,Toast.LENGTH_LONG).show()
        }

        binding.singout.setOnClickListener {
            auth.signOut()
            startActivity(mainActivity)
        }


        binding.buttonOptions.setOnClickListener {
            startActivity(optionsActivity)
        }



        if (sp.getBoolean("nightMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        delegate.applyDayNight()

        binding.buttonreg.setOnClickListener {
            Toast.makeText(this, binding.email.text.toString().toString(), Toast.LENGTH_LONG).show()
            auth.createUserWithEmailAndPassword(
                binding.email.text.toString(),
                binding.pass.text.toString()

            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Rejestracja powiodła się", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Rejestracja nie powiodła się", Toast.LENGTH_LONG).show()
                    Log.e("rejestracja", it.exception?.message.toString())
                }
            }

        }

        binding.buttonLogin.setOnClickListener {
//            val user = FirebaseAuth.getInstance().currentUser?.uid?.let { it1 ->
//                Users(
//                    it1,
//                    //it1
//                )



//            if (user != null) {
//                productadapter.addUser(user)
//            }
            auth.signInWithEmailAndPassword(
                binding.email.text.toString(),
                binding.pass.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val upIntent = Intent(this,ProductListActivity::class.java)
                    Toast.makeText(this, "Logowanie powiodło się", Toast.LENGTH_LONG).show()
                    startActivity(upIntent)
                } else {
                    Toast.makeText(this, "Logowanie nie powiodło się", Toast.LENGTH_LONG).show()
                    Log.e("logowanie", it.exception?.message.toString())
                }
            }
        }
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



