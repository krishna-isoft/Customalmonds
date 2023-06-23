package com.isoft.customalmonds

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isoft.customalmonds.adapter.Field_adap
import com.isoft.customalmonds.api.AlmondsApi
import com.isoft.customalmonds.api.RetrofitHelper
import com.isoft.customalmonds.modelclass.field_response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomefieldActivity : AppCompatActivity() {
    private val sharedPrefFile = "customalmondspref"
    private var movies: MutableList<field_response> = ArrayList()
    lateinit var recyclerView: RecyclerView
    var adapter: Field_adap? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fieldhome)
        supportActionBar?.hide()
        var btnsearch= findViewById<Button>(R.id.btn_search)
        var btnshowall= findViewById<Button>(R.id.btn_showall)
        var linshowall= findViewById<LinearLayout>(R.id.linshowall)
        var  edtuserz= findViewById<EditText>(R.id.edt_userz)
        var imgback=findViewById<ImageView>(R.id.driver_list_iv_back)
        var txtaddfiledticket=findViewById<TextView>(R.id.txt_addfiledticket)
        recyclerView= findViewById<RecyclerView>(R.id.driver_list_listView)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val sharedNameValue = sharedPreferences.getString("cc","sa")
        val userid = sharedPreferences.getString("user_id","")
        Log.e("sharedNameValue: ", sharedNameValue.toString())
        // Log.e("calling","onactivity");
        movies = ArrayList()
        adapter = Field_adap(this, movies)

        imgback.setOnClickListener{
            val intent =
                Intent(this@HomefieldActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        txtaddfiledticket.setOnClickListener{
            val intent =
                Intent(this@HomefieldActivity, AddFieldTicketActivity::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView.setLayoutManager(LinearLayoutManager(this))

        recyclerView.setNestedScrollingEnabled(false)
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter)
        getdata(sharedNameValue,userid)
        btnsearch.setOnClickListener {
            if(edtuserz.text.toString() !=null && edtuserz.text.toString().length>0) {
                getdatasearch(sharedNameValue, edtuserz.text.toString(),userid)
                linshowall.visibility = View.VISIBLE
            }else{
                Toast.makeText(applicationContext,
                    "Please enter Run No#", Toast.LENGTH_SHORT).show()
            }
        }
        btnshowall.setOnClickListener {
            resetAdapterState()
            edtuserz.setText("")
            getdata(sharedNameValue,userid)
            linshowall.visibility = View.GONE
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
//    private fun resetAdapterState() {
//        movies= ArrayList()
//        recyclerView.removeAllViews()
//        recyclerView.setAdapter(null);
//        recyclerView.setLayoutManager(null);
//        adapter = Field_adap(this, movies)
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(LinearLayoutManager(this))
//
//        //recyclerView.setLayoutManager(myLayoutManager);
//        adapter?.notifyDataSetChanged();
//        //adapter.doYourStaff();
//        //recyclerView.setAdapter(adapter);
//    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun getdatasearch(sharedNameValue: String?, idval: String, userid: String?)
    {
        //field_response
        if(isOnline(this)) {
            resetAdapterState()
            val progressDialog = ProgressDialog(this@HomefieldActivity)
            //progressDialog.setTitle("Kotlin Progress Bar")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()

            val api = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
            // Log.e("dccc","@"+dc);2019-01-14
            val call: Call<List<field_response>?>? =
                api.getfieldvaluesearch(sharedNameValue,idval,userid)
            if (call != null) {
                call!!.enqueue(object : Callback<List<field_response>?> {
                    override fun onResponse(
                        call: Call<List<field_response>?>,
                        response: Response<List<field_response>?>
                    ) {

                        if (response !=null && response.isSuccessful) {
                            val result: List<field_response?> = response.body()!!
                            if (result.isNotEmpty()) {
                                progressDialog.dismiss()
                                Log.e("responsez", response.body().toString())
                                //  movies=response.body()
                                movies.addAll(response.body()!!)
                                Log.e("moviesmovies", movies?.size.toString())
                                adapter?.notifyDataSetChanged();
                                //   adapter?.notifyDataChanged()
                                // setfullValue(result as List<production_response>)
                            } else {
                                Toast.makeText(
                                    this@HomefieldActivity,
                                    "No Records Available ",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                        }else{
                            Toast.makeText(
                                this@HomefieldActivity,
                                "No Records Available ",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }


                    override fun onFailure(call: Call<List<field_response>?>, t: Throwable) {
                        //       TODO("Not yet implemented")
                        progressDialog.dismiss()
                    }
                })
            }

        }else{
            Toast.makeText(
                this@HomefieldActivity,
                "Network not available ",
                Toast.LENGTH_LONG
            ).show()
        }
    }









    @RequiresApi(Build.VERSION_CODES.M)
    fun getdata(sharedNameValue: String?, userid: String?)
    {
        //field_response
        if(isOnline(this)) {
            val progressDialog = ProgressDialog(this@HomefieldActivity)
            //progressDialog.setTitle("Kotlin Progress Bar")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()

            val api = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
            // Log.e("dccc","@"+dc);2019-01-14
            val call: Call<List<field_response>?>? =
                api.getfieldvalue(sharedNameValue,userid)
            if (call != null) {
                call!!.enqueue(object : Callback<List<field_response>?> {
                    override fun onResponse(
                        call: Call<List<field_response>?>,
                        response: Response<List<field_response>?>
                    ) {

                        if (response !=null && response.isSuccessful) {
                            val result: List<field_response?> = response.body()!!
                            if (result.isNotEmpty()) {
                                progressDialog.dismiss()
                                Log.e("responsez", response.body().toString())
                                //  movies=response.body()
                                movies.addAll(response.body()!!)
                                Log.e("moviesmovies", movies?.size.toString())
                                adapter?.notifyDataSetChanged();
                                //   adapter?.notifyDataChanged()
                                // setfullValue(result as List<production_response>)
                            } else {
                                Toast.makeText(
                                    this@HomefieldActivity,
                                    "No Records Available ",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                        }else{
                            Toast.makeText(
                                this@HomefieldActivity,
                                "No Records Available ",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }


                    override fun onFailure(call: Call<List<field_response>?>, t: Throwable) {

                        progressDialog.dismiss()
                        Toast.makeText(
                            this@HomefieldActivity,
                            "No Records Available ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }

        }else{
            Toast.makeText(
                this@HomefieldActivity,
                "Network not available ",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private fun resetAdapterState() {
        movies= ArrayList()
        recyclerView.removeAllViews()
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        adapter = Field_adap(this, movies)
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        //recyclerView.setLayoutManager(myLayoutManager);
        adapter?.notifyDataSetChanged();
        //adapter.doYourStaff();
        //recyclerView.setAdapter(adapter);
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent =
            Intent(this@HomefieldActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}


