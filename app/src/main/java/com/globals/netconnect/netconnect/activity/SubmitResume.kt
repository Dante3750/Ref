package com.globals.netconnect.netconnect.activity

import android.Manifest
import android.app.Activity
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.app.SharedPrefManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

import android.provider.ContactsContract
import android.provider.OpenableColumns
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.support.v7.widget.Toolbar
import android.util.Patterns
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.*

import com.globals.netconnect.netconnect.app.Cons.URL_SUBMIT_RESUME



import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.regex.Pattern


class SubmitResume : AppCompatActivity(), View.OnFocusChangeListener {

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    private var pdfFileName: String? = null

    var file : File? = null
    private var rQueue: RequestQueue? = null
    private lateinit var pref: SharedPrefManager
    internal var filePath: String? = null
    private var status: Int = 0
    private var toolbar: Toolbar? = null
    private lateinit var tvFirstName: TextView
    private lateinit var tvMiddleName: TextView
    private lateinit var tvLastName: TextView
    private lateinit var tvMobile: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvUploadResume: TextView
    private var pdfPath: String? = null
    private lateinit var upload: TextView

    private lateinit var etFirstName: EditText
    private lateinit var etMiddleName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etMobile: EditText
    private lateinit var etEmail: EditText
    private lateinit var tvContact: TextView
    private lateinit var tvChoose: TextView

    private lateinit var btSubmit: Button
    private val PICK_CONTACT = 1

    private val REQUEST_DATA = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_resume)
        requestMultiplePermissions()
        window.statusBarColor = resources.getColor(R.color.statusbar)
        toolbar = findViewById<View>(R.id.toolbar_submit) as Toolbar
        setSupportActionBar(findViewById(R.id.toolbar_submit))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val regular = Typeface.createFromAsset(this.assets, "fonts/lato_regular.ttf")
        val latoSemiBold = Typeface.createFromAsset(this.assets, "fonts/lato_semibold.ttf")
        val latoMedium = Typeface.createFromAsset(this.assets, "fonts/lato_medium.ttf")
        val jobid: String = intent.getStringExtra("jobID")


//        val ss:String = intent.getStringExtra("jobID")
        tvFirstName = findViewById(R.id.tvFirstName)
        tvContact = findViewById(R.id.tv_contact)
        tvMiddleName = findViewById(R.id.tvMiddleName)
        tvLastName = findViewById(R.id.tvLastName)
        tvMobile = findViewById(R.id.tvMobile)
        tvEmail = findViewById(R.id.tvEmail)
        tvUploadResume = findViewById(R.id.tvUpload)
        upload = findViewById(R.id.upload)

        tvChoose = findViewById(R.id.tv_upload_text)


        etFirstName = findViewById(R.id.et_first_name)
        etMiddleName = findViewById(R.id.et_middle_name)
        etLastName = findViewById(R.id.et_last_name)
        etMobile = findViewById(R.id.et_mobile)
        etEmail = findViewById(R.id.et_email)


        btSubmit = findViewById(R.id.bt_submit)


        tvFirstName.typeface = regular
        tvMiddleName.typeface = regular
        tvLastName.typeface = regular
        tvMobile.typeface = regular
        tvEmail.typeface = regular
        tvUploadResume.typeface = regular

        etFirstName.typeface = regular
        etMiddleName.typeface = regular
        etLastName.typeface = regular
        etMobile.typeface = regular
        etEmail.typeface = regular

        btSubmit.typeface = latoSemiBold


        etFirstName.onFocusChangeListener = this
        etMiddleName.onFocusChangeListener = this
        etLastName.onFocusChangeListener = this
        etMobile.onFocusChangeListener = this
        etEmail.onFocusChangeListener = this

        tvContact.setOnClickListener { loadContacts() }
        pref = SharedPrefManager.getInstance(this)

        upload.setOnClickListener {


            MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(REQUEST_DATA)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.pdf$"))
                .withTitle("Select PDF file")
                .start()
//            val intent = Intent()
//            intent.type = "application/pdf"
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(Intent.createChooser(intent, "Select Pdf"), REQUEST_DATA)

        }

        btSubmit.setOnClickListener {


            if (!validate()) {

            } else {

                uploadPdf(jobid.toInt())
            }



        }


    }

    private fun validate(): Boolean {
        var valid = true

        val firstName = etFirstName!!.text.toString()
        //val middleName = etMiddleName.text.toString()
        val lastName = etLastName.text.toString()
        val mobileNo = etMobile.text.toString()
        val emailId = etEmail.text.toString()



        if (firstName.isEmpty()) {
            etFirstName!!.error = "Enter First Name"
            valid = false
        } else {
            etFirstName.error = null
        }

        if (lastName.isEmpty()) {
            etLastName!!.error = "Enter Last Name"
            valid = false
        } else {
            etLastName.error = null
        }

        if (mobileNo.isEmpty() || mobileNo.length < 10) {
            etMobile!!.error = "Enter valid  Mobile No"
            valid = false
        } else {
            etMobile.error = null
        }

        if (emailId.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            etEmail!!.error = "Enter valid Email Address"
            valid = false
        } else {
            etEmail.error = null
        }



//        if (empPass.isEmpty() || empPass.length < 6) {
//            mEmpPass.error = "Your Password must be at least 6 characters"
//            valid = false
//        } else {
//            mEmpPass.error = null
//        }
        return valid
    }

    private fun loadContacts() {

        etFirstName.setText("")
        etLastName.setText("")
        etMobile.setText("")
        etEmail.setText("")
        etMiddleName.setText("")

        val i = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(i, PICK_CONTACT)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true

    }


    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {

            R.id.et_first_name -> {
                etMobile.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etMiddleName.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etLastName.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                etEmail.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                etFirstName.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)
                tvFirstName.setTextColor(resources.getColor(R.color.red))

                tvEmail.setTextColor(resources.getColor(R.color.tvColor))
                tvLastName.setTextColor(resources.getColor(R.color.tvColor))
                tvMiddleName.setTextColor(resources.getColor(R.color.tvColor))
                tvMobile.setTextColor(resources.getColor(R.color.tvColor))

            }

            R.id.et_middle_name -> {
                etMobile.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etFirstName.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etLastName.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                etEmail.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                etMiddleName.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)
                tvMiddleName.setTextColor(resources.getColor(R.color.red))

                tvEmail.setTextColor(resources.getColor(R.color.tvColor))
                tvLastName.setTextColor(resources.getColor(R.color.tvColor))
                tvFirstName.setTextColor(resources.getColor(R.color.tvColor))
                tvMobile.setTextColor(resources.getColor(R.color.tvColor))

            }

            R.id.et_last_name -> {
                etMobile.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etFirstName.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etMiddleName.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                etEmail.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                etLastName.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)
                tvLastName.setTextColor(resources.getColor(R.color.red))

                tvEmail.setTextColor(resources.getColor(R.color.tvColor))
                tvMiddleName.setTextColor(resources.getColor(R.color.tvColor))
                tvFirstName.setTextColor(resources.getColor(R.color.tvColor))
                tvMobile.setTextColor(resources.getColor(R.color.tvColor))

            }
            R.id.et_mobile -> {
                etMiddleName.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etFirstName.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etLastName.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                etEmail.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                etMobile.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)
                tvMobile.setTextColor(resources.getColor(R.color.red))

                tvEmail.setTextColor(resources.getColor(R.color.tvColor))
                tvMiddleName.setTextColor(resources.getColor(R.color.tvColor))
                tvFirstName.setTextColor(resources.getColor(R.color.tvColor))
                tvLastName.setTextColor(resources.getColor(R.color.tvColor))

            }

            R.id.et_email -> {
                etMobile.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etFirstName.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etLastName.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                etMiddleName.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                etEmail.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)
                tvEmail.setTextColor(resources.getColor(R.color.red))

                tvLastName.setTextColor(resources.getColor(R.color.tvColor))
                tvMiddleName.setTextColor(resources.getColor(R.color.tvColor))
                tvFirstName.setTextColor(resources.getColor(R.color.tvColor))
                tvMobile.setTextColor(resources.getColor(R.color.tvColor))

            }


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            val contactUri = data!!.data
            val cursor = contentResolver.query(contactUri!!, null, null, null, null)
            cursor!!.moveToFirst()

            val column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val name = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE)

            var myPhn = cursor.getString(column)
            var myName = cursor.getString(name)
            val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

            myPhn = myPhn.replace("\\s".toRegex(), "")
            myPhn = myPhn.replace("+91", "")
            var result: List<String> = myName.split(",").map { it.trim() }
            if (result.size == 2) {
                etFirstName.setText(result[1])
                etLastName.setText(result[0])
            } else {
                etFirstName.setText(result[0])
            }
            etMobile.setText(myPhn)

        }
        if (requestCode == REQUEST_DATA && resultCode == Activity.RESULT_OK) {
            val path = data!!.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            file = File(path!!)
            // displayFromFile(file)
            if (path != null) {
                Log.d("Path: ", path)
                pdfPath = path
                Toast.makeText(this, "Picked file: $path", Toast.LENGTH_LONG).show()
                val uri = Uri.fromFile(File(file!!.absolutePath))
                pdfFileName = getFileName(uri)

                tvChoose.text = pdfFileName
                if (tvChoose.text != "No File Choosen") {
                    tvChoose.setTextColor(resources.getColor(R.color.red))

                } else {
                    //nothing
                }


            }
        }


    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))

                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.lastPathSegment
        }
        return result
    }


    private fun uploadPdf(jobid: Int) {

        val t = Thread(Runnable {

            if (tvChoose.text == "No File Choosen") {

                this@SubmitResume.runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        "Upload Resume First",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                val content_type = getMimeType(file!!.path)

                Log.v("Data", content_type)

                val client = OkHttpClient()
                val file_body = RequestBody.create(MediaType.parse(content_type), file)

                val request_body = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("jobId", jobid.toString())
                    .addFormDataPart("firstName", etFirstName.text.toString().trim())
                    .addFormDataPart("employeeId", pref.user.employeeId.trim())
                    .addFormDataPart("lastName", etLastName.text.toString().trim())
                    .addFormDataPart("middleName", etMiddleName.text.toString().trim())
                    .addFormDataPart("mobileNumber", etMobile.text.toString().trim())
                    .addFormDataPart("emailId", etEmail.text.toString().trim())
                    .addFormDataPart("resume", pdfPath!!.substring(pdfPath!!.lastIndexOf("/") + 1), file_body)
                    .build()

                Log.v("Data", pdfPath!!.substring(pdfPath!!.lastIndexOf("/") + 1))

                val request = Request.Builder()
                    .url(URL_SUBMIT_RESUME)
                    .post(request_body)
                    .build()

                try {
                    client.newCall(request).enqueue(object : okhttp3.Callback {
                        override fun onFailure(call: okhttp3.Call, e: IOException) {
                            call.cancel()
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                            val myResponse = response.body().toString()
                            this@SubmitResume.runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "Done",
                                    Toast.LENGTH_SHORT
                                ).show()




                            }

                        }
                    })

                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        }


        )

        t.start()





    }


    private fun requestMultiplePermissions() {
        Dexter.withActivity(this)
            .withPermissions(

                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        //nothing
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).withErrorListener { Toast.makeText(applicationContext, "Some Error! ", Toast.LENGTH_SHORT).show() }
            .onSameThread()
            .check()
    }


    private fun getMimeType(path: String): String? {

        val extension = MimeTypeMap.getFileExtensionFromUrl(path)

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
}






            
            
