package com.isoft.customalmonds

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.isoft.customalmonds.api.AlmondsApi
import com.isoft.customalmonds.api.RetrofitHelper
import com.isoft.customalmonds.barcodegenerate.BarcodeCreater
import com.isoft.customalmonds.modelclass.bittagsave_response
import com.isoft.customalmonds.modelclass.handler_response
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddSubTagActivity : AppCompatActivity() {
    private var inkkk: Intent? = null
    var runno = ""
    var runid = ""
    var handlername = "0"
    var type = ""
    var typeval = ""
    var grower = ""
    var variety = ""
    var ranchname = ""
    var handlerval = ""
    var tag = "maintag"
    private var btMap: Bitmap? = null

    private val sharedPrefFile = "customalmondspref"
    private var movies: MutableList<handler_response> = ArrayList()

    lateinit var generatePDFBtn: Button

    // declaring width and height
    // for our PDF file.
    var pageHeight = 1120
    var pageWidth = 792

    // creating a bitmap variable
    // for storing our images
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap
    lateinit var edtgrosswgt: EditText
    lateinit var edttarewgt: EditText
    lateinit var edtnwgt: TextView

    lateinit var rbgroup: RadioGroup
    lateinit var rb_meet: RadioButton
    lateinit var rb_inshell: RadioButton

    // on below line we are creating a
    // constant code for runtime permissions.
    var PERMISSION_CODE = 101
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_details)
        supportActionBar?.hide()
        movies = ArrayList()

        var  imgback= findViewById<ImageView>(R.id.driver_list_iv_back)
        var  txt_submit= findViewById<TextView>(R.id.txt_submit)
        var  txtprint= findViewById<TextView>(R.id.txt_print)
        var  txt_runno= findViewById<TextView>(R.id.edt_runno)
        var  txt_type= findViewById<TextView>(R.id.edt_type)
        var  txt_grower= findViewById<TextView>(R.id.edt_grower)
        var  txt_variety= findViewById<TextView>(R.id.edt_variety)
        var  txt_ranchname= findViewById<TextView>(R.id.edt_rname)
          edtgrosswgt= findViewById<EditText>(R.id.edt_grosswgt)
        edttarewgt= findViewById<EditText>(R.id.edt_tare)
          edtnwgt= findViewById<TextView>(R.id.edt_nwgt)
        edttarewgt.addTextChangedListener(textWatcher)
        edtgrosswgt.addTextChangedListener(textWatcher)
        rb_meet = findViewById<RadioButton>(R.id.r_meet)
        rb_inshell = findViewById<RadioButton>(R.id.r_inshell)
        rbgroup = findViewById<RadioGroup>(R.id.add_tripmode)
        val sharedPreferencesz: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =
            sharedPreferencesz.edit()
        editor.putString("resumsstatus", "1")
        editor.apply()
        editor.commit()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val sharedNameValue = sharedPreferences.getString("cc","sa")
        val userid = sharedPreferences.getString("user_id","")
        inkkk = intent
        if (inkkk != null) {
            if (inkkk!!.hasExtra("runno")) {
                runid = inkkk!!.getStringExtra("runid").toString()
                runno = inkkk!!.getStringExtra("runno").toString()
                 type= inkkk!!.getStringExtra("type").toString()
                 grower= inkkk!!.getStringExtra("grower").toString()
                 variety = inkkk!!.getStringExtra("variety").toString()
                 ranchname= inkkk!!.getStringExtra("ranchname").toString()
                tag= inkkk!!.getStringExtra("tag").toString()
            }
        }
        if(type !=null && type.isNotEmpty())
        {
            if(type.contentEquals("INSHELL"))
            {
                rb_inshell.isChecked=true
            }else{
               rb_meet.isChecked=true
            }
        }
        Log.e("tag","@"+tag)
        gethandler(sharedNameValue)
        txt_runno.setText("" +runno)
        txt_type.setText("" +type)
        txt_grower.setText("" +grower)
        txt_variety.setText("" +variety)
        if(ranchname !=null && ranchname.isNotEmpty() && !ranchname.contentEquals("null"))
        {
            txt_ranchname.setText("" +ranchname)
        }else{
            ranchname=""
            txt_ranchname.setText("")
        }

        txtprint.setOnClickListener{

if(!handlername.contentEquals("0")) {
    if(edtnwgt.text.toString() !=null && edtnwgt.text.toString().isNotEmpty() && !edtnwgt.text.toString().contentEquals("null"))
    {
    getprintbintag(
        sharedNameValue,
        runid,
        txt_type.text.toString(),
        txt_grower.text.toString(),
        txt_variety.text.toString(),
        txt_ranchname.text.toString(),
        edtgrosswgt.text.toString(),
        edttarewgt.text.toString(),
        edtnwgt.text.toString(),userid
    )
    }else{
        Toast.makeText(
            this@AddSubTagActivity,
            "Please enter values all fields",
            Toast.LENGTH_LONG
        ).show()
    }
}else{
    Toast.makeText(
        this@AddSubTagActivity,
        "Please select Handler",
        Toast.LENGTH_LONG
    ).show()
}
        }

        txt_submit.setOnClickListener{
            if(!handlername.contentEquals("0")) {
                if(edtnwgt.text.toString() !=null && edtnwgt.text.toString().isNotEmpty() && !edtnwgt.text.toString().contentEquals("null"))
                {
            getsubmitbintag(sharedNameValue,runid,txt_type.text.toString(),
                txt_grower.text.toString(),txt_variety.text.toString(),txt_ranchname.text.toString()
                ,edtgrosswgt.text.toString()
                ,edttarewgt.text.toString()
                ,edtnwgt.text.toString(),userid)
                }else{
                    Toast.makeText(
                        this@AddSubTagActivity,
                        "Please enter values all fields",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }else{
                Toast.makeText(
                    this@AddSubTagActivity,
                    "Please select Handler",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        imgback.setOnClickListener{
//            val intent =
//                Intent(this@AddSubTagActivity, HomeActivitynew::class.java)
//            startActivity(intent)
            finish()
        }

    }



    @RequiresApi(Build.VERSION_CODES.M)
    fun gethandler(sharedNameValue: String?)
    {
        if(isOnline(this)) {
            val progressDialog = ProgressDialog(this@AddSubTagActivity)
            //progressDialog.setTitle("Kotlin Progress Bar")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()

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
                                progressDialog.dismiss()
                                movies=ArrayList();
                                handlername="0"
                                val grv = handler_response("0","0", "Select Handler")
                                movies.add(grv)
                                movies.addAll(response.body()!!)
                                callspinner(movies)

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
                this@AddSubTagActivity,
                "Network not available ",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun callspinner(movies: MutableList<handler_response>) {
        var handlernames=arrayOf("")
        handlernames=movies.map { it.name }.toTypedArray()

        val spinner = findViewById<Spinner>(R.id.spin_handler)
        if (spinner != null) {
            //var languages = arrayOf("Java", "PHP", "Kotlin", "Javascript", "Python", "Swift")

           // var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)

            var adapter = ArrayAdapter(this@AddSubTagActivity,
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
//                    Toast.makeText(this@AddSubTagActivity,
//                        getString(R.string.app_name) + " " +
//                                "" + handlernames[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getsubmitbintag(
        sharedNameValue: String?,
        runno: String,
        type: String,
        grower: String,
        variety: String,
        ranchname: String,
        grosswgt: String,
        tare: String,
        tnwgt: String,
        userid: String?
    )
    {

        if(isOnline(this)) {

            typeval = getTripItem()

            val progressDialog = ProgressDialog(this@AddSubTagActivity)
            //progressDialog.setTitle("Kotlin Progress Bar")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()
            var  call: Call<List<bittagsave_response>?>?
            val api = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
            // Log.e("dccc","@"+dc);2019-01-14
            if(tag.contentEquals("maintag")) {
                call =
                    api.savebintag(
                        sharedNameValue,
                        "" + runno,
                        "" + handlername,
                        "" + grosswgt,
                        "" + tare,
                        "" + tnwgt,userid,typeval
                    )
            }else{
                call =
                api.savebintagsub(
                    sharedNameValue,
                    "" + runno,
                    "" + handlername,
                    "" + grosswgt,
                    "" + tare,
                    "" + tnwgt,userid,typeval
                )
            }
                if (call != null) {
                call!!.enqueue(object : Callback<List<bittagsave_response>?> {
                    override fun onResponse(
                        call: Call<List<bittagsave_response>?>,
                        response: Response<List<bittagsave_response>?>
                    ) {

                        if (response.isSuccessful) {
                            val result: List<bittagsave_response?> = response.body()!!
                            if (result.isNotEmpty()) {
                                progressDialog.dismiss()
                                Log.e("responsez", response.body().toString())

                                setresponsesubmit(response.body()!!,sharedNameValue,
                                    runno,
                                    type,
                                    grower,
                                    variety,
                                    ranchname,
                                    grosswgt,
                                    tare,
                                    tnwgt)
                            } else {
                            }
                            //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                        }else{
                            progressDialog.dismiss()
                        }
                    }


                    override fun onFailure(call: Call<List<bittagsave_response>?>, t: Throwable) {
                        progressDialog.dismiss()

                    }
                })
            }

        }else{
            Toast.makeText(
                this@AddSubTagActivity,
                "Network not available ",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getprintbintag(
        sharedNameValue: String?,
        runno: String,
        typed: String,
        grower: String,
        variety: String,
        ranchname: String,
        grosswgt: String,
        tare: String,
        tnwgt: String,
        userid: String?
    )
    {

        if(isOnline(this)) {


            typeval = getTripItem()
            val progressDialog = ProgressDialog(this@AddSubTagActivity)
            //progressDialog.setTitle("Kotlin Progress Bar")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()

            val api = RetrofitHelper.getInstance().create(AlmondsApi::class.java)
            // Log.e("dccc","@"+dc);2019-01-14
            val call: Call<List<bittagsave_response>?>?;
            if(tag.contentEquals("maintag")) {
                call = api.savebintag(
                    sharedNameValue,
                    "" + runno,
                    "" + handlername,
                    "" + grosswgt,
                    "" + tare,
                    "" + tnwgt,userid,typeval
                )
            }else{
                call = api.savebintagsub(
                    sharedNameValue,
                    "" + runno,
                    "" + handlername,
                    "" + grosswgt,
                    "" + tare,
                    "" + tnwgt,userid,typeval
                )
            }
            if (call != null) {
                call!!.enqueue(object : Callback<List<bittagsave_response>?> {
                    override fun onResponse(
                        call: Call<List<bittagsave_response>?>,
                        response: Response<List<bittagsave_response>?>
                    ) {

                        if (response.isSuccessful) {
                            val result: List<bittagsave_response?> = response.body()!!
                            if (result.isNotEmpty()) {
                                progressDialog.dismiss()
                                Log.e("responsez", response.body().toString())

                              setresponseprint(response.body()!!,sharedNameValue,
                                runno,
                                type,
                                grower,
                                variety,
                                ranchname,
                                grosswgt,
                                tare,
                                tnwgt)
                            } else {
                            }
                            //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                        }else{
                            progressDialog.dismiss()
                        }
                    }


                    override fun onFailure(call: Call<List<bittagsave_response>?>, t: Throwable) {
                        progressDialog.dismiss()

                    }
                })
            }

        }else{
            Toast.makeText(
                this@AddSubTagActivity,
                "Network not available ",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private fun setresponsesubmit(
        listtag: List<bittagsave_response>,
        sharedNameValue: String?,
        runno: String,
        type: String,
        grower: String,
        variety: String,
        ranchname: String,
        grosswgt: String,
        tare: String,
        tnwgt: String
    ) {
        try {
            if (listtag.size > 0) {
                for (i in listtag) {
                    if (i.status == "0") {
                        Toast.makeText(
                            this@AddSubTagActivity,
                            "No Data ",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        //txtData.setText(json)
                        Toast.makeText(
                            this@AddSubTagActivity,
                            "Gross Bin Tag Have Been Added Sucessfully!",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent =
                            Intent(this@AddSubTagActivity, Listproduct::class.java)
                        startActivity(intent)
                        finish()


                    }
                }
            }
        } catch (e: Exception) {
            // TODO: handle exception
        }
    }
    private fun setresponseprint(
        listtag: List<bittagsave_response>,
        sharedNameValue: String?,
        runno: String,
        type: String,
        grower: String,
        variety: String,
        ranchname: String,
        grosswgt: String,
        tare: String,
        tnwgt: String
    ) {
        try {
            if (listtag.size > 0) {
                for (i in listtag) {
                    if (i.status == "0") {
                        Toast.makeText(
                            this@AddSubTagActivity,
                            "No Data ",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        //txtData.setText(json)
                        Toast.makeText(
                            this@AddSubTagActivity,
                            "Gross Bin Tag Have Been Added Sucessfully!",
                            Toast.LENGTH_LONG
                        ).show()

                                    btMap = BarcodeCreater.creatBarcode(
                                        this@AddSubTagActivity, i.tagno,
                                        10, 10,
                                        300, 40, true, 3,
                                        40
                                    )
                                    try {
                                        createPDF(
                                            this.runno,
                                            this.type,
                                            this.grower,
                                            this.variety,
                                            this.ranchname,handlername,grosswgt,tare,tnwgt,i.tagno,i.create_date,i.slid)
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    } catch (e: DocumentException) {
                                        e.printStackTrace()
                                    }


                    }
                }
            }
        } catch (e: Exception) {
            // TODO: handle exception
        }
    }




    private fun printz(file: File, fname: String) {

//        val outputFile = File(this@AddSubTagActivity.filesDir, "addbintag.pdf")
//        val uri = FileProvider.getUriForFile(
//            this@AddSubTagActivity,
//            "com.isoft.customalmonds",  //(use your app signature + ".provider" )
//            outputFile
//        )
//        val share = Intent()
//        share.action = Intent.ACTION_VIEW
//        share.type = "application/pdf"
//        share.putExtra(Intent.EXTRA_STREAM, uri)
//        //share.setPackage("in.kahub.fgdsp");
//        share.setPackage("in.kahub.fgdsp");
//        //share.setPackage("com.jdprint")
//        startActivity(share)




//        Log.e("file", "@:" + file.toString());

        val sharedPreferencesz: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =
            sharedPreferencesz.edit()
        editor.putString("resumsstatus", "2")
        editor.apply()
        editor.commit()
        val manager = this@AddSubTagActivity.getSystemService(PRINT_SERVICE) as PrintManager
        val adapter = PDFDocumentAdapter(file, "GBT_$fname")
        val attributes = PrintAttributes.Builder().build()
        manager.print("Document", adapter, attributes)

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

    @Throws(IOException::class, DocumentException::class)
    fun createPDF(
        runnz: String,
        typez: String,
        grower: String,
        variety: String,
        ranchname: String,
        handlername: String,
        grosswgt: String,
        tare: String,
        tnwgt: String,
        tagno: String?,
        createDate: String?,
        slid: String?
    ) {

        val doc = Document(PageSize.A6)
        try {
            try {
                val dir = this@AddSubTagActivity.filesDir
                val filez = File(dir, "addbintag.pdf")
                val deleted = filez.delete()
                if (deleted) {
                }
            } catch (e: java.lang.Exception) {
            }

            val file = File(this@AddSubTagActivity.filesDir,  "addbintag.pdf")
            val fOut = FileOutputStream(file)
            PdfWriter.getInstance(doc, fOut)
            doc.open()
           // val mainheadin = Font(Font.TIMES_ROMAN, 12.0f, Font.BOLD, Color.black)
            val subfontsub = FontFactory.getFont(FontFactory.HELVETICA, 9f)

            val subfontsubxx = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9f)

            val headyr = PdfPTable(1)
            headyr.widthPercentage = 100f

            val cellyr: PdfPCell
            cellyr = PdfPCell(
                Paragraph(
                    "Crop Year : " + Calendar.getInstance()[Calendar.YEAR], subfontsubxx
                )
            )
            cellyr.border = 0
            cellyr.paddingBottom = 5f
            cellyr.horizontalAlignment = Paragraph.ALIGN_LEFT
            headyr.addCell(cellyr)
            doc.add(headyr)
            val d = resources.getDrawable(R.drawable.logoprintnew)
            val bitDw = d as BitmapDrawable
            val bmp = bitDw.bitmap
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val image: Image = Image.getInstance(stream.toByteArray())
            image.alignment = Element.ALIGN_CENTER
            image.scaleToFit(260f, 60f)
            doc.add(image)

            //            LineSeparator line = new LineSeparator();
//            doc.add(line);
            val yourCustomFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13f)
            val yourCustomFontsub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f)
            val yourCustomFontz = FontFactory.getFont(FontFactory.HELVETICA, 13f)
            val head = PdfPTable(1)
            head.widthPercentage = 100f
            val cellkzq: PdfPCell
            cellkzq = PdfPCell(Paragraph("", yourCustomFont))
            cellkzq.border = 0
            cellkzq.paddingBottom = 5f
            cellkzq.horizontalAlignment = Paragraph.ALIGN_CENTER
            head.addCell(cellkzq)
            val cellk: PdfPCell
            cellk = PdfPCell(Paragraph("Gross Bin Tag",yourCustomFont))
            cellk.border = 0
            cellk.paddingBottom = 2f
            /*cellk.setBorderWidthLeft(1);
			cellk.setBorderWidthRight(1);*/cellk.horizontalAlignment = Paragraph.ALIGN_CENTER
            head.addCell(cellk)

            val cellkzqw: PdfPCell
            cellkzqw = PdfPCell(Paragraph("", yourCustomFont))
            cellkzqw.border = 0
            cellkzqw.paddingBottom = 5f
            cellkzqw.horizontalAlignment = Paragraph.ALIGN_CENTER
            head.addCell(cellkzqw)

            var cellkzh: PdfPCell
//            cellkzh = PdfPCell(Paragraph("" + type, yourCustomFontz))
//            cellkzh.border = 0
//            cellkzh.paddingBottom = 2f
//            cellkzh.horizontalAlignment = Paragraph.ALIGN_CENTER
//            head.addCell(cellkzh)

//            val cellkzqwr: PdfPCell
//            cellkzqwr = PdfPCell(Paragraph("", yourCustomFontz))
//            cellkzqwr.border = 0
//            cellkzqwr.paddingBottom = 5f
//            cellkzqwr.horizontalAlignment = Paragraph.ALIGN_CENTER
//            head.addCell(cellkzqwr)
            //

            //

            cellkzh = PdfPCell(Paragraph("Tag# " + ": 0000" + tagno, yourCustomFontsub))
            cellkzh.border = 0
            cellkzh.paddingBottom = 2f
            cellkzh.horizontalAlignment = Paragraph.ALIGN_CENTER
            head.addCell(cellkzh)

            var cellkzqwrh: PdfPCell
            cellkzqwrh = PdfPCell(Paragraph("", yourCustomFontz))
            cellkzqwrh.border = 0
            cellkzqwrh.paddingBottom = 5f
            cellkzqwrh.horizontalAlignment = Paragraph.ALIGN_CENTER
            head.addCell(cellkzqwrh)
            cellkzqwrh = PdfPCell(Paragraph("", yourCustomFontz))
            cellkzqwrh.border = 0
            cellkzqwrh.paddingBottom = 5f
            cellkzqwrh.horizontalAlignment = Paragraph.ALIGN_CENTER
            head.addCell(cellkzqwrh)
            doc.add(head)



          //  val listvalues = Font(Font.TIMES_ROMAN, 12.0f, Font.NORMAL, Color.black)


//set driver name and date

            val subfont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9f)
            val subfontright = FontFactory.getFont(FontFactory.HELVETICA, 9f)
            //val subfontsub = FontFactory.getFont(FontFactory.HELVETICA, 9f)
           // val lik = LineSeparator()
            //doc.add(lik)


//set driver name and date
            val lhtab0 = PdfPTable(9)
            lhtab0.widthPercentage = 100f
            lhtab0.horizontalAlignment = 0

            val lhcell1: PdfPCell

            lhcell1 = PdfPCell(Paragraph("Run No. ",subfont))
            lhcell1.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell1.colspan =4
            lhcell1.border = 0
            lhcell1.setPadding(2f)
            lhtab0.addCell(lhcell1)

            var lhcell2zd: PdfPCell
            lhcell2zd = PdfPCell(Paragraph(": ", subfontright))
            lhcell2zd.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2zd.colspan = 1
            lhcell2zd.border = 0
            lhcell2zd.setPadding(2f)
            lhtab0.addCell(lhcell2zd)

            val lhcell1z: PdfPCell
            lhcell1z = PdfPCell(Paragraph(""+runno,subfontright))
            lhcell1z.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell1z.colspan =4
            lhcell1z.border = 0
            lhcell1z.setPadding(2f)
            lhtab0.addCell(lhcell1z)

            //
            //new

            //new


            //new
            val lhcellmk2: PdfPCell
            lhcellmk2 = PdfPCell(Paragraph("Type ", subfont))
            lhcellmk2.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcellmk2.colspan = 4
            lhcellmk2.border = 0
            lhcellmk2.setPadding(2f)
            lhtab0.addCell(lhcellmk2)

            val lhcell2zmkd: PdfPCell
            lhcell2zmkd = PdfPCell(Paragraph(": ", subfontright))
            lhcell2zmkd.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2zmkd.colspan = 1
            lhcell2zmkd.border = 0
            lhcell2zmkd.setPadding(2f)
            lhtab0.addCell(lhcell2zmkd)

            val lhcell2mkz: PdfPCell
            lhcell2mkz = PdfPCell(Paragraph("" + type, subfontright))
            lhcell2mkz.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2mkz.colspan = 4
            lhcell2mkz.border = 0
            lhcell2mkz.setPadding(2f)
            lhtab0.addCell(lhcell2mkz)

            val lhcell6: PdfPCell
            lhcell6 = PdfPCell(Paragraph("Handler  ",subfont))
            lhcell6.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell6.colspan = 4
            lhcell6.border = 0
            lhcell6.setPadding(2f)
            lhtab0.addCell(lhcell6)




            lhcell2zd = PdfPCell(Paragraph(": ", subfontright))
            lhcell2zd.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2zd.colspan = 1
            lhcell2zd.border = 0
            lhcell2zd.setPadding(2f)
            lhtab0.addCell(lhcell2zd)

            val lhcell6z: PdfPCell
            lhcell6z = PdfPCell(Paragraph(""+handlerval,subfontright))
            lhcell6z.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell6z.colspan = 4
            lhcell6z.border = 0
            lhcell6z.setPadding(2f)
            lhtab0.addCell(lhcell6z)

            //
            val lhcell3: PdfPCell
            lhcell3 = PdfPCell(Paragraph("Grower  ",subfont))
            lhcell3.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell3.colspan = 4
            lhcell3.border = 0
            lhcell3.setPadding(2f)
            lhtab0.addCell(lhcell3)
            lhcell2zd = PdfPCell(Paragraph(": ", subfontright))
            lhcell2zd.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2zd.colspan = 1
            lhcell2zd.border = 0
            lhcell2zd.setPadding(2f)
            lhtab0.addCell(lhcell2zd)
            val lhcell3z: PdfPCell
            lhcell3z = PdfPCell(Paragraph(""+grower,subfontright))
            lhcell3z.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell3z.colspan = 4
            lhcell3z.border = 0
            lhcell3z.setPadding(2f)
            lhtab0.addCell(lhcell3z)

            //
            val lhcell4: PdfPCell
            lhcell4 = PdfPCell(Paragraph("Variety  ",subfont))
            lhcell4.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell4.colspan = 4
            lhcell4.border = 0
            lhcell4.setPadding(2f)
            lhtab0.addCell(lhcell4)

            lhcell2zd = PdfPCell(Paragraph(": ", subfontright))
            lhcell2zd.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2zd.colspan = 1
            lhcell2zd.border = 0
            lhcell2zd.setPadding(2f)
            lhtab0.addCell(lhcell2zd)

            val lhcell4z: PdfPCell
            lhcell4z = PdfPCell(Paragraph(""+variety,subfontright))
            lhcell4z.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell4z.colspan = 4
            lhcell4z.border = 0
            lhcell4z.setPadding(2f)
            lhtab0.addCell(lhcell4z)

            val lhcell5: PdfPCell
            lhcell5 = PdfPCell(Paragraph("Ranch Name  ",subfont))
            lhcell5.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell5.colspan = 4
            lhcell5.border = 0
            lhcell5.setPadding(2f)
            lhtab0.addCell(lhcell5)

            lhcell2zd = PdfPCell(Paragraph(": ", subfontright))
            lhcell2zd.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2zd.colspan = 1
            lhcell2zd.border = 0
            lhcell2zd.setPadding(2f)
            lhtab0.addCell(lhcell2zd)

            val lhcell5z: PdfPCell
            lhcell5z = PdfPCell(Paragraph(""+ranchname,subfontright))
            lhcell5z.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell5z.colspan = 4
            lhcell5z.border = 0
            lhcell5z.setPadding(2f)
            lhtab0.addCell(lhcell5z)

            //
            //
            val lhcell9f: PdfPCell
            lhcell9f = PdfPCell(Paragraph("Fumigation ", subfont))
            lhcell9f.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell9f.colspan = 4
            lhcell9f.border = 0
            lhcell9f.setPadding(2f)
            lhcell9f.paddingBottom = 5f
            lhtab0.addCell(lhcell9f)

            lhcell2zd = PdfPCell(Paragraph(": ", subfontright))
            lhcell2zd.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2zd.colspan = 1
            lhcell2zd.border = 0
            lhcell2zd.setPadding(2f)
            lhtab0.addCell(lhcell2zd)

            val lhcell9ff: PdfPCell
            lhcell9ff = PdfPCell(Paragraph(" ", subfontright))
            lhcell9ff.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell9ff.colspan = 4
            lhcell9ff.border = 0
            lhcell9ff.setPadding(2f)
            lhcell9ff.paddingBottom = 5f
            lhtab0.addCell(lhcell9ff)

            //


            //
            val lhcell9fz: PdfPCell
            lhcell9fz = PdfPCell(Paragraph(" ", subfont))
            lhcell9fz.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell9fz.colspan = 9
            lhcell9fz.border = 0
            lhcell9fz.setPadding(2f)
            lhcell9fz.paddingBottom = 5f
            lhtab0.addCell(lhcell9fz)

            //new fumugation date


            //new fumugation date
            val lhcell9af: PdfPCell
            lhcell9af = PdfPCell(Paragraph("FUMIGATION DATE", subfont))
            lhcell9af.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell9af.colspan = 4
            lhcell9af.border = 0
            lhcell9af.setPadding(2f)
            lhcell9af.paddingBottom = 5f
            lhtab0.addCell(lhcell9af)

            val lhcell9sffxx: PdfPCell
            lhcell9sffxx = PdfPCell(Paragraph(": ", subfontright))
            lhcell9sffxx.setHorizontalAlignment(Paragraph.ALIGN_LEFT)
            lhcell9sffxx.setColspan(1)
            lhcell9sffxx.setBorder(0)
            lhcell9sffxx.setPadding(2f)
            lhtab0.addCell(lhcell9sffxx)

            val lhcell9sff: PdfPCell
            lhcell9sff = PdfPCell(Paragraph(" ", subfontright))
            lhcell9sff.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell9sff.colspan = 4
            lhcell9sff.border = 0
            lhcell9sff.setPadding(2f)
            lhcell9sff.paddingBottom = 5f
            lhtab0.addCell(lhcell9sff)

doc.add(lhtab0)


            val line = LineSeparator()
            doc.add(line)

            val lhtab2 = PdfPTable(9)
            lhtab2.widthPercentage = 100f
            lhtab2.horizontalAlignment = 0


            val lhcell7: PdfPCell
            lhcell7 = PdfPCell(Paragraph("Gross Weight  ",subfont))
            lhcell7.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell7.colspan = 4
            lhcell7.border = 0
            lhcell7.setPadding(2f)
            lhtab2.addCell(lhcell7)
            lhcell2zd = PdfPCell(Paragraph(": ", subfontright))
            lhcell2zd.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2zd.colspan = 1
            lhcell2zd.border = 0
            lhcell2zd.setPadding(2f)
            lhtab2.addCell(lhcell2zd)
            val lhcell7z: PdfPCell
            if(grosswgt.contains(".")) {
                lhcell7z = PdfPCell(Paragraph("" + grosswgt + " Lb", subfontright))
            }else{
                lhcell7z = PdfPCell(Paragraph("" + grosswgt + ".00 Lb", subfontright))
            }
            lhcell7z.horizontalAlignment = Paragraph.ALIGN_RIGHT
            lhcell7z.colspan = 4
            lhcell7z.border = 0
            lhcell7z.setPadding(2f)
            lhtab2.addCell(lhcell7z)

            val lhcell8: PdfPCell
            lhcell8 = PdfPCell(Paragraph("Tare  ",subfont))
            lhcell8.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell8.colspan = 4
            lhcell8.border = 0
            lhcell8.setPadding(2f)
            lhtab2.addCell(lhcell8)

            lhcell2zd = PdfPCell(Paragraph(": ", subfontright))
            lhcell2zd.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2zd.colspan = 1
            lhcell2zd.border = 0
            lhcell2zd.setPadding(2f)
            lhtab2.addCell(lhcell2zd)

            val lhcell8z: PdfPCell
            if(tare.contains(".")) {
                lhcell8z = PdfPCell(Paragraph("" + tare + " Lb", subfontright))
            }else{
                lhcell8z = PdfPCell(Paragraph("" + tare + ".00 Lb", subfontright))
            }
            lhcell8z.horizontalAlignment = Paragraph.ALIGN_RIGHT
            lhcell8z.colspan =4
            lhcell8z.border = 0
            lhcell8z.setPadding(2f)
            lhtab2.addCell(lhcell8z)

            val lhcell8j: PdfPCell
            lhcell8j = PdfPCell(Paragraph("", subfont))
            lhcell8j.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell8j.colspan = 5
            lhcell8j.border = 0
            lhcell8j.setPadding(2f)
            lhtab2.addCell(lhcell8j)
            val lhcell8zk: PdfPCell
            lhcell8zk = PdfPCell(Paragraph(" --------------- ", subfontright))
            lhcell8zk.horizontalAlignment = Paragraph.ALIGN_RIGHT
            lhcell8zk.colspan = 4
            lhcell8zk.border = 0
            lhcell8zk.setPadding(2f)
            lhtab2.addCell(lhcell8zk)
            val lhcell9: PdfPCell
            lhcell9 = PdfPCell(Paragraph("Net Weight  ",subfont))
            lhcell9.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell9.colspan = 4
            lhcell9.border = 0
            lhcell9.setPadding(2f)
            lhcell9.paddingBottom = 5f
            lhtab2.addCell(lhcell9)
            lhcell2zd = PdfPCell(Paragraph(": ", subfontright))
            lhcell2zd.horizontalAlignment = Paragraph.ALIGN_LEFT
            lhcell2zd.colspan = 1
            lhcell2zd.border = 0
            lhcell2zd.setPadding(2f)
            lhtab2.addCell(lhcell2zd)
            val lhcell9z: PdfPCell
            if(tnwgt.contains("."))
            {
                lhcell9z = PdfPCell(Paragraph(""+tnwgt+" Lb",subfontright))
            }else{
                lhcell9z = PdfPCell(Paragraph(""+tnwgt+".00 Lb",subfontright))
            }

            lhcell9z.horizontalAlignment = Paragraph.ALIGN_RIGHT
            lhcell9z.colspan = 4
            lhcell9z.border = 0
            lhcell9z.setPadding(2f)
            lhcell9z.paddingBottom = 5f
            lhtab2.addCell(lhcell9z)

           // doc.add(lhtab1)


            doc.add(lhtab2)
            var linez = LineSeparator()
            doc.add(linez)
            linez = LineSeparator()
            doc.add(linez)
            if (createDate != null) {
                Log.e(
                    "createdate",createDate
                )
            }
            if (createDate != null && createDate.length > 0) {
                val lhtab3 = PdfPTable(8)
                lhtab3.widthPercentage = 100f
                lhtab3.horizontalAlignment = 0
                val sk = StringTokenizer(createDate, " ")
                val dateyy = sk.nextToken()



                val sdf = SimpleDateFormat("hh:mm")
                val sdfs = SimpleDateFormat("hh:mm a")
                var dt: Date? = null
                try {
                    dt = sdf.parse(sk.nextToken())
                    println("Time Display: " + sdfs.format(dt)) // <-- I got result here
                } catch (e: ParseException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
                val lhcell9zaz: PdfPCell
                lhcell9zaz = PdfPCell(Paragraph("Bin Stamp No:"+slid,subfontsub))
                lhcell9zaz.horizontalAlignment = Paragraph.ALIGN_LEFT
                lhcell9zaz.colspan = 3
                lhcell9zaz.border = 0
               // lhcell9zaz.setPadding(2f)
                lhcell9zaz.paddingBottom = 7f
                lhtab3.addCell(lhcell9zaz)
                val lhcell9za: PdfPCell
                lhcell9za = PdfPCell(Paragraph("Date Time:" + parseDateToddMMyyyy(dateyy)+" | "+sdfs.format(dt),subfontsub))
                lhcell9za.horizontalAlignment = Paragraph.ALIGN_RIGHT
                lhcell9za.colspan = 5
                lhcell9za.border = 0
                lhcell9za.paddingBottom = 7f
                //lhcell9za.setPadding(2f)
                lhtab3.addCell(lhcell9za)
                doc.add(lhtab3)
            }
            try {
                val stream = ByteArrayOutputStream()
                btMap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val myImg = Image.getInstance(stream.toByteArray())
                myImg.alignment = Image.ALIGN_CENTER
                myImg.scaleToFit(260f, 58f)
                doc.add(myImg)
            } catch (ex: IOException) {
                return
            }
            printz(file, "000"+tagno)
        } catch (de: DocumentException) {
            Log.e("PDFCreator", "DocumentException:$de")
        } catch (e: IOException) {
            Log.e("PDFCreator", "ioException:$e")
        } finally {
            doc.close()
        }
    }
    fun parseDateToddMMyyyy(time: String?): String? {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "MM/dd/yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }
    override fun onBackPressed() {
        super.onBackPressed()
//        val intent =
//            Intent(this@AddSubTagActivity, Listproduct::class.java)
//        startActivity(intent)
        finish()
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

    private fun getTripItem(): String {
        // TODO Auto-generated method stub
        var str = ""
        val selectedId: Int = rbgroup.getCheckedRadioButtonId()
        if (selectedId == rb_meet.getId()) {
            type="MEATS"
            return "1"
        } else if (selectedId == rb_inshell.getId()) {
            type="INSHELL"
            return "2"
        }
        return str
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val resumestatus = sharedPreferences.getString("resumsstatus","0")
        Log.e("resumestatus","#"+resumestatus)
        if(resumestatus.contentEquals("2"))
        {
        val intent =
            Intent(this@AddSubTagActivity, Listproduct::class.java)
        startActivity(intent)
finish()
        }
    }
}