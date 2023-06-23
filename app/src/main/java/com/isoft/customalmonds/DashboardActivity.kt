package com.isoft.customalmonds

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView

class DashboardActivity : AppCompatActivity() {
    private val sharedPrefFile = "customalmondspref"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()
        val btnproduction= findViewById<ImageView>(R.id.img_production)
        val btnfieldticket= findViewById<ImageView>(R.id.img_fieldticket)

        val imglogout=findViewById<ImageView>(R.id.driver_list_iv_back)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        btnproduction.setOnClickListener{
//            val intent =
//                Intent(this@DashboardActivity, HomeActivitynew::class.java)
//            startActivity(intent)
//            finish()

            val intent =
                Intent(this@DashboardActivity, Listproduct::class.java)
            startActivity(intent)
            finish()
        }
        btnfieldticket.setOnClickListener{
            val intent =
                Intent(this@DashboardActivity, HomefieldActivity::class.java)
            startActivity(intent)
            finish()
        }
        imglogout.setOnClickListener{
            val editor: SharedPreferences.Editor =
                sharedPreferences.edit()
            editor.putString("login_status", "fail")
            editor.apply()
            editor.commit()
            val intent =
                Intent(this@DashboardActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}