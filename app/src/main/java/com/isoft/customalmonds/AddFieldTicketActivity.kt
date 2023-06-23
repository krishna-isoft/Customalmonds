package com.isoft.customalmonds
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.isoft.customalmonds.api.AlmondsApi
import com.isoft.customalmonds.api.RetrofitHelper
import com.isoft.customalmonds.modelclass.fticketsave_response
import com.isoft.customalmonds.modelclass.handler_response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddFieldTicketActivity : AppCompatActivity() {
    private val sharedPrefFile = "customalmondspref"
    private var listvariety: MutableList<handler_response> = ArrayList()
    private var listgrower: MutableList<handler_response> = ArrayList()
    private var listranch: MutableList<handler_response> = ArrayList()
    private var listhandler: MutableList<handler_response> = ArrayList()
    lateinit var rbgroup: RadioGroup
    lateinit var rb_meet: RadioButton
    lateinit var rb_inshell: RadioButton

    lateinit var edtfield: EditText

    lateinit var edtgrosswgt: EditText
    lateinit var edttarewgt: EditText
    lateinit var edtnwgt: TextView

    lateinit var edttruck: EditText
    lateinit var edttrucklicense: EditText
    lateinit var edtsemino: EditText
    lateinit var edtsemilicense: EditText
    lateinit var edttrailerno: EditText
    lateinit var edttrailerlicense: EditText
    lateinit var edtdriver: EditText
    lateinit var txtsubmit: TextView



    private var type=""
     var  growerid="0"
     var  growerval=""

     var  ranchid=""
     var  ranchnamesval=""

     var  varietyid="0"
     var  varietyval=""
    var  handlername="0"
    var  handlerval=""
    var ik=0;

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_fieldticket)
        supportActionBar?.hide()
        listvariety = ArrayList()
        listgrower = ArrayList()
        listranch = ArrayList()
        listhandler = ArrayList()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val sharedNameValue = sharedPreferences.getString("cc","sa")
        val userid = sharedPreferences.getString("user_id","")
         rb_meet = findViewById<RadioButton>(R.id.r_meet)
          rb_inshell = findViewById<RadioButton>(R.id.r_inshell)
         rbgroup = findViewById<RadioGroup>(R.id.add_tripmode)

        edtfield = findViewById<EditText>(R.id.edt_field)

        edtgrosswgt = findViewById<EditText>(R.id.edt_grosswgt)
        edttarewgt = findViewById<EditText>(R.id.edt_tarewgt)
        edttarewgt.addTextChangedListener(textWatcher)
        edtgrosswgt.addTextChangedListener(textWatcher)
        edtnwgt = findViewById<TextView>(R.id.edt_nwgt)
        edttruck= findViewById<EditText>(R.id.edt_truck)
        edttrucklicense= findViewById<EditText>(R.id.edt_trucklicense)
        edttrailerno= findViewById<EditText>(R.id.edt_trailerno)
        edtsemino= findViewById<EditText>(R.id.edt_semino)
        edtsemilicense= findViewById<EditText>(R.id.edt_semilicense)
        edttrailerlicense= findViewById<EditText>(R.id.edt_trailerlicense)
        edtdriver= findViewById<EditText>(R.id.edt_driver)
        var imgback=findViewById<ImageView>(R.id.driver_list_iv_back)
        txtsubmit= findViewById<TextView>(R.id.txt_submit)

txtsubmit.setOnClickListener{
    type = getTripItem()
    if(type!=null && type.length>0 && !type.contentEquals("null")) {
        if(!growerid.contentEquals("0")) {
        if(!varietyid.contentEquals("0")) {
            if(!handlername.contentEquals("0")) {
                if(!ranchid.contentEquals("0")) {
            if(edtfield.text.toString() !=null && edtfield.text.toString().isNotEmpty() && !edtfield.text.toString().contentEquals("null"))
            {
                if(edttruck.text.toString() !=null && edttruck.text.toString().isNotEmpty() && !edttruck.text.toString().contentEquals("null")
                    && edttrucklicense.text.toString() !=null && edttrucklicense.text.toString().isNotEmpty() && !edttrucklicense.text.toString().contentEquals("null")
                    && edtsemino.text.toString() !=null && edtsemino.text.toString().isNotEmpty() && !edtsemino.text.toString().contentEquals("null")
                    && edtsemilicense.text.toString() !=null && edtsemilicense.text.toString().isNotEmpty() && !edtsemilicense.text.toString().contentEquals("null")
                    && edttrailerno.text.toString() !=null && edttrailerno.text.toString().isNotEmpty() && !edttrailerno.text.toString().contentEquals("null")
                    && edttrailerlicense.text.toString() !=null && edttrailerlicense.text.toString().isNotEmpty() && !edttrailerlicense.text.toString().contentEquals("null")
                    && edtdriver.text.toString() !=null && edtdriver.text.toString().isNotEmpty() && !edtdriver.text.toString().contentEquals("null")) {
                    if (userid != null) {
                        savefiledticket(
                            sharedNameValue,
                            growerid,
                            edtfield.text.toString(),
                            varietyid,
                            handlername,
                            edttruck.text.toString(),
                            edttrucklicense.text.toString(),
                            edtsemino.text.toString(),
                            edtsemilicense.text.toString(),
                            edttrailerno.text.toString(),
                            edttrailerlicense.text.toString(),
                            edtdriver.text.toString(),
                            "0",
                            userid,
                            edtgrosswgt.text.toString(),
                            edttarewgt.text.toString(),
                            edtnwgt.text.toString(),
                            ranchid
                        )
                    }
                }else{
                    Toast.makeText(
                        this@AddFieldTicketActivity,
                        "Please enter All Fields",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }else{
                Toast.makeText(
                    this@AddFieldTicketActivity,
                    "Please enter Field Ticket",
                    Toast.LENGTH_LONG
                ).show()
            }
            }else{
                Toast.makeText(
                    this@AddFieldTicketActivity,
                    "Please select Ranch Name",
                    Toast.LENGTH_LONG
                ).show()
            }
            }else{
                Toast.makeText(
                    this@AddFieldTicketActivity,
                    "Please select Handler",
                    Toast.LENGTH_LONG
                ).show()
            }
        }else{
            Toast.makeText(
                this@AddFieldTicketActivity,
                "Please select Variety",
                Toast.LENGTH_LONG
            ).show()
        }
        }else{
            Toast.makeText(
                this@AddFieldTicketActivity,
                "Please select Grower",
                Toast.LENGTH_LONG
            ).show()
        }
    }else{
        Toast.makeText(
            this@AddFieldTicketActivity,
            "Please select anyone of these options",
            Toast.LENGTH_LONG
        ).show()
    }
}
        imgback.setOnClickListener{
            val intent =
                Intent(this@AddFieldTicketActivity, HomefieldActivity::class.java)
            startActivity(intent)
            finish()
        }

        //strTrip=getTripItem();


        getgrower(sharedNameValue)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun savefiledticket(
        sharedNameValue: String?,
        growerid: String,
        fieldticket: String,
        varietyid: String,
        handlername: String,
        truck: String,
        trucklic: String,
        semino: String,
        semilic: String,
        trailerno: String,
        trailerlic: String,
        driver: String,
        status: String,
        userid: String,
        grosswgt: String,
        tarewgt: String,
        netwgt: String,
        ranchid: String

    ) {
        if(isOnline(this)) {
            val progressDialog = ProgressDialog(this@AddFieldTicketActivity)
            //progressDialog.setTitle("Kotlin Progress Bar")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()
           // Log.e("growerid", growerid)
            //Log.e("varietyid", varietyid)
            //Log.e("handlername", handlername)
            val quotesApi = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
            quotesApi.savefieldticket(sharedNameValue,growerid,ranchid,varietyid,handlername,truck,trucklic,semino,semilic,trailerno,trailerlic,
            driver,type,userid,grosswgt,tarewgt,netwgt,fieldticket)
                ?.enqueue(object : Callback<fticketsave_response?> {
                    override fun onResponse(
                        call: Call<fticketsave_response?>,
                        responsez: Response<fticketsave_response?>
                    ) {
                         // Log.e("Responsestring", responsez.body().toString())

                        if (responsez.isSuccessful) {
                            if (responsez.body() != null) {
                                //Log.e("status: ", responsez.body()?.status.toString())
                                //  var json = Gson().toJson(response.body())
                                if (responsez.body()?.status .contentEquals("0")) {
                                    Toast.makeText(
                                        this@AddFieldTicketActivity,
                                        "No Data ",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    progressDialog.dismiss()
                                } else {
                                    //txtData.setText(json)
                                    progressDialog.dismiss()
                                    Toast.makeText(
                                        this@AddFieldTicketActivity,
                                        "FIELD TICKET has been added ",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent = Intent(this@AddFieldTicketActivity, AddFieldTicketActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                }
                            }
                        } else {
                        }
                    }

                    override fun onFailure(call: Call<fticketsave_response?>, t: Throwable) {


                    }
                })

        }else{
            Toast.makeText(
                this@AddFieldTicketActivity,
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
            Intent(this@AddFieldTicketActivity, HomefieldActivity::class.java)
        startActivity(intent)
        finish()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun getgrower(sharedNameValue: String?)
    {
        if(isOnline(this)) {
            val progressDialog = ProgressDialog(this@AddFieldTicketActivity)
            //progressDialog.setTitle("Kotlin Progress Bar")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()

            val api = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
            // Log.e("dccc","@"+dc);2019-01-14
            val call: Call<List<handler_response>?>? =
                api.getgrower(sharedNameValue)
            if (call != null) {
                call!!.enqueue(object : Callback<List<handler_response>?> {
                    override fun onResponse(
                        call: Call<List<handler_response>?>,
                        response: Response<List<handler_response>?>
                    ) {

                        if (response.isSuccessful) {
                            val result: List<handler_response?> = response.body()!!
                            if (result.isNotEmpty()) {
                                progressDialog.dismiss()
                                listvariety = ArrayList()
                                growerid="0"

                                val grv = handler_response("0","0", "Select Grower")
                                listgrower = ArrayList()
                                listgrower.add(grv)
                                listgrower.addAll(response.body()!!)
                                callspinnergrower(listgrower,sharedNameValue)

                            } else {
                            }
                            //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                        }else{
                            progressDialog.dismiss()
                        }
                    }


                    override fun onFailure(call: Call<List<handler_response>?>, t: Throwable) {

                        progressDialog.dismiss()
                    }
                })
            }

        }else{
            Toast.makeText(
                this@AddFieldTicketActivity,
                "Network not available ",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun callspinnergrower(movies: MutableList<handler_response>, sharedNameValue: String?) {
        var growernames=arrayOf("Select Grower")


        growernames=movies.map { it.name }.toTypedArray()

        val spinner = findViewById<Spinner>(R.id.spin_grower)
        if (spinner != null) {
            var adapter = ArrayAdapter(this@AddFieldTicketActivity,
                R.layout.spinner_list, growernames)
            spinner.adapter = adapter
            spinner.setSelection(1)
            //spinner.setSelection(getIndex(spinner, "TOOR FARMS"));
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    //  handlername=handlernames[position]
                      growerid=movies.get(position).id
                      growerval=movies.get(position).name
                    getranch(sharedNameValue,growerid)

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        var index = 0
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == myString) {
                index = i
            }
        }
        return index
    }
//get variety
@RequiresApi(Build.VERSION_CODES.M)
fun getvariety(sharedNameValue: String?)
{
    if(isOnline(this)) {


        val api = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
        // Log.e("dccc","@"+dc);2019-01-14
        val call: Call<List<handler_response>?>? =
            api.getvariety(sharedNameValue)
        if (call != null) {
            call!!.enqueue(object : Callback<List<handler_response>?> {
                override fun onResponse(
                    call: Call<List<handler_response>?>,
                    response: Response<List<handler_response>?>
                ) {

                    if (response.isSuccessful) {
                        val result: List<handler_response?> = response.body()!!
                        if (result.isNotEmpty()) {
                            varietyid="0"
                            val varty = handler_response("0","0", "Select Variety")
                            listvariety= ArrayList()
                            listvariety.add(varty)
                            listvariety.addAll(response.body()!!)
                            callspinnervariety(listvariety,sharedNameValue)

                        } else {
                        }
                        //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                    }else{

                    }
                }


                override fun onFailure(call: Call<List<handler_response>?>, t: Throwable) {


                }
            })
        }

    }else{
        Toast.makeText(
            this@AddFieldTicketActivity,
            "Network not available ",
            Toast.LENGTH_LONG
        ).show()
    }
}

    @RequiresApi(Build.VERSION_CODES.M)
    private fun callspinnervariety(movies: MutableList<handler_response>, sharedNameValue: String?) {
        var varietynames=arrayOf("")


        varietynames=movies.map { it.name }.toTypedArray()

        val spinner = findViewById<Spinner>(R.id.spin_variety)
        if (spinner != null) {
            var adapter = ArrayAdapter(this@AddFieldTicketActivity,
                R.layout.spinner_list, varietynames)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    //  handlername=handlernames[position]
                      varietyid=movies.get(position).id
                      varietyval=movies.get(position).name

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        gethandler(sharedNameValue)

    }
    //get ranch

    @RequiresApi(Build.VERSION_CODES.M)
    fun getranch(sharedNameValue: String?, growerid: String)
    {
       // Log.e("groweridz",growerid)
        if(isOnline(this)) {
            val progressDialog = ProgressDialog(this@AddFieldTicketActivity)
            //progressDialog.setTitle("Kotlin Progress Bar")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()

            val api = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
            // Log.e("dccc","@"+dc);2019-01-14
            val call: Call<List<handler_response>?>? =
                api.getranch(sharedNameValue,growerid)
            if (call != null) {
                call!!.enqueue(object : Callback<List<handler_response>?> {
                    override fun onResponse(
                        call: Call<List<handler_response>?>,
                        response: Response<List<handler_response>?>
                    ) {

                        if (response.isSuccessful) {
                            val result: List<handler_response?> = response.body()!!
                            if (result.isNotEmpty()) {
                                progressDialog.dismiss()
                                val hettt = handler_response("0","0", "Select Ranch Name")
                                ranchid="0"
                                listranch = ArrayList()
                                listranch.add(hettt)
                                listranch.addAll(response.body()!!)
                                callspinnerranch(listranch,sharedNameValue)

                            } else {
                                progressDialog.dismiss()
                                val hettt = handler_response("0","0", "Select Ranch Name")
                                listranch = ArrayList()
                                listranch.add(hettt)
                                callspinnerranch(listranch,sharedNameValue)
                            }
                            //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                        }else{
                            progressDialog.dismiss()
                        }
                    }


                    override fun onFailure(call: Call<List<handler_response>?>, t: Throwable) {

                        progressDialog.dismiss()
                    }
                })
            }

        }else{
            Toast.makeText(
                this@AddFieldTicketActivity,
                "Network not available ",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun callspinnerranch(movies: MutableList<handler_response>, sharedNameValue: String?) {
        var ranchnames=arrayOf("")


        ranchnames=movies.map { it.name }.toTypedArray()

        val spinner = findViewById<Spinner>(R.id.spin_ranchname)
        if (spinner != null) {
            var adapter = ArrayAdapter(this@AddFieldTicketActivity,
                R.layout.spinner_list, ranchnames)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    //  handlername=handlernames[position]
                      ranchid=movies.get(position).id
                      ranchnamesval=movies.get(position).name

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        if(ik==0) {
            getvariety(sharedNameValue);
        }
    }
    private fun getTripItem(): String {
        // TODO Auto-generated method stub
        var str = ""
        val selectedId: Int = rbgroup.getCheckedRadioButtonId()
        if (selectedId == rb_meet.getId()) {
            return "1"
        } else if (selectedId == rb_inshell.getId()) {
            return "2"
        }
        return str
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun gethandler(sharedNameValue: String?)
    {
        if(isOnline(this)) {


            val api = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
            // Log.e("dccc","@"+dc);2019-01-14
            val call: Call<List<handler_response>?>? =
                api.gethandler(sharedNameValue)
            if (call != null) {
                call!!.enqueue(object : Callback<List<handler_response>?> {
                    override fun onResponse(
                        call: Call<List<handler_response>?>,
                        response: Response<List<handler_response>?>
                    ) {

                        if (response.isSuccessful) {
                            val result: List<handler_response?> = response.body()!!
                            if (result.isNotEmpty()) {
                                handlername="0"
                                val hettt = handler_response("0","0", "Select Handler")
                                listhandler= ArrayList()
                                listhandler.add(hettt)
                                listhandler.addAll(response.body()!!)
                                callspinner(listhandler)

                            } else {
                            }
                            //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                        }else{

                        }
                    }


                    override fun onFailure(call: Call<List<handler_response>?>, t: Throwable) {


                    }
                })
            }

        }else{
            Toast.makeText(
                this@AddFieldTicketActivity,
                "Network not available ",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun callspinner(movies: MutableList<handler_response>) {
        var handlernames=arrayOf("")
        handlernames=movies.map { it.name }.toTypedArray()
ik=1;
        val spinner = findViewById<Spinner>(R.id.spin_handler)
        if (spinner != null) {

            var adapter = ArrayAdapter(this@AddFieldTicketActivity,
                R.layout.spinner_list, handlernames)
            spinner.adapter = adapter
            spinner.setSelection(1)
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    //  handlername=handlernames[position]
                    handlername=movies.get(position).id
                    handlerval=movies.get(position).name
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        }
    }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            var gross=edtgrosswgt.text.toString();
            var tare=edttarewgt.text.toString();
            var total=0;
            if(gross !=null && gross.length>0 && tare!=null && tare.length>0)
            {total=0
                total=gross.toInt()-tare.toInt();
                edtnwgt.setText(total.toString())
            }else if(gross !=null && gross.length>0 ){
                total=0
                total=gross.toInt();
                edtnwgt.setText(total.toString())
            }else{
                total=0
                total=tare.toInt();
                edtnwgt.setText(total.toString())
            }

        }
    }

}