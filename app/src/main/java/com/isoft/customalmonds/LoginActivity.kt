package com.isoft.customalmonds

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.isoft.customalmonds.api.AlmondsApi
import com.isoft.customalmonds.api.RetrofitHelper
import com.isoft.customalmonds.modelclass.login_response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val sharedPrefFile = "customalmondspref"
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        var btnsubmit = findViewById<Button>(R.id.btn_submit)
        var edtuserid = findViewById<EditText>(R.id.edt_userid)
        var edtpassword = findViewById<EditText>(R.id.edt_password)
        var edtcmpcode = findViewById<EditText>(R.id.edt_cmpcode)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)


        val loginstatus = sharedPreferences.getString("login_status","fail")
        if(loginstatus.contentEquals("success"))
        {
            val intent =
                Intent(this@LoginActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnsubmit.setOnClickListener {
           if(isOnline(this)) {
               val progressDialog = ProgressDialog(this@LoginActivity)
               //progressDialog.setTitle("Kotlin Progress Bar")
               progressDialog.setMessage("Please wait...")
               progressDialog.show()

               val quotesApi = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
               quotesApi.getQLogin(edtcmpcode.text.toString(),edtuserid.text.toString(),edtpassword.text.toString())
                   ?.enqueue(object : Callback<login_response?> {
                       override fun onResponse(
                           call: Call<login_response?>,
                           responsez: Response<login_response?>
                       ) {
                          // Log.e("Responsestring", responsez.body().toString())

                           if (responsez.isSuccessful) {
                               if (responsez.body() != null) {
                                   //  var json = Gson().toJson(response.body())
                                   if (responsez.body()?.status == 0) {
                                       Toast.makeText(
                                           this@LoginActivity,
                                           "No Data ",
                                           Toast.LENGTH_LONG
                                       ).show()
                                       progressDialog.dismiss()
                                   } else {
                                       //txtData.setText(json)
                                       progressDialog.dismiss()
                                       //Log.e("status: ", responsez.body()?.status.toString())
                                       val editor: SharedPreferences.Editor =
                                           sharedPreferences.edit()
                                       editor.putString("cc", edtcmpcode.text.toString())
                                       editor.putString("login_status", "success")
                                       editor.putString("user_id", ""+responsez.body()?.id.toString())
                                       editor.apply()
                                       editor.commit()

                                       val intent =
                                           Intent(this@LoginActivity, DashboardActivity::class.java)
                                       startActivity(intent)
                                       finish()
                                   }
                               }
                           } else {
                               progressDialog.dismiss()
                           }
                       }

                       override fun onFailure(call: Call<login_response?>, t: Throwable) {

                           progressDialog.dismiss()
                       }
                   })

           }else{
               Toast.makeText(
                   this@LoginActivity,
                   "Network not available ",
                   Toast.LENGTH_LONG
               ).show()
           }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}