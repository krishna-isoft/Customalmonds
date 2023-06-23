package com.isoft.customalmonds

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import android.widget.ExpandableListView.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.isoft.customalmonds.adapter.CustomExpandableListAdapter
import com.isoft.customalmonds.api.AlmondsApi
import com.isoft.customalmonds.api.RetrofitHelper
import com.isoft.customalmonds.modelclass.grs_bin_tag
import com.isoft.customalmonds.modelclass.production_response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Listproduct : AppCompatActivity() {
    var expandableListView: ExpandableListView? = null
    var expandableListAdapter: ExpandableListAdapter? = null
    var expandableListTitle: MutableList<production_response> = ArrayList()

    private val sharedPrefFile = "customalmondspref"
    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expandiblelist)
        supportActionBar?.hide()
        expandableListView = findViewById(R.id.explist) as ExpandableListView

        val sharedPreferencesz: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =
            sharedPreferencesz.edit()
        editor.putString("resumsstatus", "0")
        editor.apply()
        editor.commit()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val sharedNameValue = sharedPreferences.getString("cc","sa")
        val userid = sharedPreferences.getString("user_id","")
        if (userid != null) {
            Log.e("userid listkkkkk: ", userid)
        }
        val btnsearch= findViewById<Button>(R.id.btn_search)
        val btnshowall= findViewById<Button>(R.id.btn_showall)
        val linshowall= findViewById<LinearLayout>(R.id.linshowall)
        val  edtuserz= findViewById<EditText>(R.id.edt_userz)
        val imgback=findViewById<ImageView>(R.id.driver_list_iv_back)
        val imghelp= findViewById<ImageView>(R.id.img_help)
        expandableListTitle = ArrayList()
        getdata(sharedNameValue,userid)

        expandableListAdapter =
            CustomExpandableListAdapter(this, expandableListTitle)
        expandableListView!!.setAdapter(expandableListAdapter)
        expandableListView!!.setOnGroupExpandListener { groupPosition ->
        }
        imgback.setOnClickListener{
            val intent =
                Intent(this@Listproduct, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnsearch.setOnClickListener {
            if(edtuserz.text.toString() !=null && edtuserz.text.toString().length>0) {
                expandableListTitle = ArrayList()
                expandableListAdapter =
                    CustomExpandableListAdapter(this@Listproduct, expandableListTitle)
                expandableListView!!.setAdapter(expandableListAdapter)
                getdatasearch(sharedNameValue, edtuserz.text.toString(),userid)
                linshowall.visibility = View.VISIBLE
            }else{
                Toast.makeText(applicationContext,
                    "Please enter Run No#", Toast.LENGTH_SHORT).show()
            }
        }
        btnshowall.setOnClickListener {
            expandableListTitle = ArrayList()
            expandableListAdapter =
                CustomExpandableListAdapter(this@Listproduct, expandableListTitle)
            expandableListView!!.setAdapter(expandableListAdapter)
            edtuserz.setText("")
            getdata(sharedNameValue,userid)
            linshowall.visibility = View.GONE
        }


        imghelp.setOnClickListener{
            callhelplist()
        }
        expandableListView!!.setOnGroupCollapseListener { groupPosition ->
//            Toast.makeText(
//                applicationContext,
//                expandableListTitle.get(groupPosition) + " List Collapsed.",
//                Toast.LENGTH_SHORT
//            ).show()
        }
        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
//            Toast.makeText(
//                applicationContext,
//                expandableListTitle.get(groupPosition)
//                        + " -> "
//                        + expandableListDetail!![expandableListTitle.get(groupPosition)]!![childPosition],
//                Toast.LENGTH_SHORT
//            ).show()
            false
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun getdatasearch(sharedNameValue: String?, searchval: String, userid: String?)
    {

        if(isOnline(this)) {



            val progressDialog = ProgressDialog(this@Listproduct)
            //progressDialog.setTitle("Kotlin Progress Bar")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()

            val api = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
            // Log.e("dccc","@"+dc);2019-01-14
            val call: Call<List<production_response>?>? =
                api.getproductionvaluesearch(sharedNameValue,searchval,userid)
            if (call != null) {
                call!!.enqueue(object : Callback<List<production_response>?> {
                    override fun onResponse(
                        call: Call<List<production_response>?>,
                        response: Response<List<production_response>?>
                    ) {
                        if (response !=null && response.isSuccessful) {
                            val result: List<production_response?> = response.body()!!

                            if (result.isNotEmpty()) {


                                expandableListTitle = ArrayList()
                                expandableListTitle.addAll(response.body()!!)
                                expandableListAdapter =
                                    CustomExpandableListAdapter(this@Listproduct, expandableListTitle)
                                expandableListView!!.setAdapter(expandableListAdapter)


                                progressDialog.dismiss()
                                //   adapter?.notifyDataChanged()
                                // setfullValue(result as List<production_response>)
                            } else {
                                Toast.makeText(
                                    this@Listproduct,
                                    "No Records Available ",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                        }else{
                            Toast.makeText(
                                this@Listproduct,
                                "No Records Available ",
                                Toast.LENGTH_LONG
                            ).show()
                            progressDialog.dismiss()
                        }
                    }


                    override fun onFailure(call: Call<List<production_response>?>, t: Throwable) {
                        progressDialog.dismiss()

                        expandableListAdapter =
                            CustomExpandableListAdapter(this@Listproduct, expandableListTitle)
                        expandableListView!!.setAdapter(expandableListAdapter)


                    }
                })
            }

        }else{
            Toast.makeText(
                this@Listproduct,
                "Network not available ",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private fun callhelplist() {
        val cdialog = Dialog(this@Listproduct)
        cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //   cdialog.setCancelable(false)
        cdialog.setContentView(R.layout.list_help)
        val yesBtn = cdialog.findViewById(R.id.img_close) as ImageView
        yesBtn.setOnClickListener {
            cdialog.dismiss()
        }

        cdialog.show()

    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun getdata(sharedNameValue: String?, userid: String?)
    {
        if(isOnline(this)) {
            val progressDialog = ProgressDialog(this@Listproduct)
            //progressDialog.setTitle("Kotlin Progress Bar")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()

            val api = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
            // Log.e("dccc","@"+dc);2019-01-14
            val call: Call<List<production_response>?>? =
                api.getproductionvalue(sharedNameValue,userid)
            if (call != null) {
                call!!.enqueue(object : Callback<List<production_response>?> {
                    override fun onResponse(
                        call: Call<List<production_response>?>,
                        response: Response<List<production_response>?>
                    ) {

                        if (response != null && response.isSuccessful) {
                            val result: List<production_response?> = response.body()!!
                            if (result.isNotEmpty()) {

                                expandableListTitle = ArrayList()
                                expandableListTitle.addAll(response.body()!!)

                                expandableListAdapter =
                                    CustomExpandableListAdapter(this@Listproduct, expandableListTitle)
                                expandableListView!!.setAdapter(expandableListAdapter)



                                progressDialog.dismiss()
                                //   adapter?.notifyDataChanged()
                                // setfullValue(result as List<production_response>)
                            } else {
                                Toast.makeText(
                                    this@Listproduct,
                                    "No Records Available ",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                        } else {
                            Toast.makeText(
                                this@Listproduct,
                                "No Records Available ",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }


                    override fun onFailure(call: Call<List<production_response>?>, t: Throwable) {
                        //       TODO("Not yet implemented")
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@Listproduct,
                            "No Records Available ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }

        }else{
            Toast.makeText(
                this@Listproduct,
                "Network not available ",
                Toast.LENGTH_LONG
            ).show()
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent =
            Intent(this@Listproduct, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
