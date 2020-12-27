package com.globals.netconnect.netconnect.activity


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.app.Cons
import com.globals.netconnect.netconnect.app.SharedPrefManager
import com.globals.netconnect.netconnect.helper.RetrofitClient
import com.globals.netconnect.netconnect.model.LoginResponse
import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.OnDrawableClickListener
import kotlinx.android.synthetic.main.activity_update.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : AppCompatActivity(), View.OnFocusChangeListener, View.OnClickListener {



    private var toolbar: Toolbar? = null
    lateinit var tvUpdateEmail :TextView
    lateinit var tvUpdateMobile :TextView
    lateinit var tvUpdatePass :TextView
    private lateinit var progressDialog:ProgressDialog

    private lateinit var etUpdateEmail :EditText
    private lateinit var etUpdatemobile :EditText
    private lateinit var etUpdatePass :EditText
    private lateinit var pref: SharedPrefManager
    private lateinit var btnUpdate:Button
    private var status: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        toolbar = findViewById<View>(R.id.toolbar_update) as Toolbar
        setSupportActionBar(findViewById(R.id.toolbar_update))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = resources.getColor(R.color.statusbar)


        val regular = Typeface.createFromAsset(this.assets, "fonts/lato_regular.ttf")
        val latoSemiBold = Typeface.createFromAsset(this.assets, "fonts/lato_semibold.ttf")
        val latoMedium = Typeface.createFromAsset(this.assets, "fonts/lato_medium.ttf")

        tvUpdateEmail=findViewById(R.id.tvUpdateEmail)
        tvUpdateMobile=findViewById(R.id.tvUpdateMobile)
        tvUpdatePass=findViewById(R.id.tvUpdatePass)
        etUpdateEmail=findViewById(R.id.et_update_email)
        etUpdatemobile=findViewById(R.id.et_update_mobile)
        etUpdatePass=findViewById(R.id.et_update_password)
        btnUpdate = findViewById(R.id.bt_update)
        pref = SharedPrefManager.getInstance(this)

        etUpdateEmail.onFocusChangeListener = this
        etUpdatemobile.onFocusChangeListener = this
        etUpdatePass.onFocusChangeListener = this


        btnUpdate.typeface = latoSemiBold
        tvUpdateEmail.typeface = regular
        tvUpdateMobile.typeface = regular
        tvUpdatePass.typeface = regular
        etUpdateEmail.typeface = regular
        etUpdatemobile.typeface = regular
        etUpdatePass.typeface = regular

        btnUpdate.setOnClickListener(this)

        etUpdatePass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0)

        et_update_password.setDrawableClickListener(object : OnDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                if (status == 1) {
                    DrawablePosition.RIGHT
                    etUpdatePass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0)
                    etUpdatePass.transformationMethod = PasswordTransformationMethod.getInstance()
                    status = 0
                } else {
                    DrawablePosition.RIGHT
                    etUpdatePass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password_icon, 0)
                    etUpdatePass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    status = 1
                }
            }
        })
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    @SuppressLint("ResourceAsColor")
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {

            R.id.et_update_email -> {
                etUpdatemobile.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etUpdatePass.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                etUpdateEmail.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)

                tvUpdateEmail.setTextColor(resources.getColor(R.color.red))

                tvUpdateMobile.setTextColor(resources.getColor(R.color.tvColor))
                tvUpdatePass.setTextColor(resources.getColor(R.color.tvColor))

            }

            R.id.et_update_mobile -> {
                etUpdateEmail.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                etUpdatePass.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                etUpdatemobile.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)


                tvUpdateMobile.setTextColor(resources.getColor(R.color.red))

                tvUpdateEmail.setTextColor(resources.getColor(R.color.tvColor))

                tvUpdatePass.setTextColor(resources.getColor(R.color.tvColor))

            }

            R.id.et_update_password -> {

                etUpdateEmail.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                etUpdatemobile.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                etUpdatePass.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)


                tvUpdateMobile.setTextColor(resources.getColor(R.color.tvColor))

                tvUpdateEmail.setTextColor(resources.getColor(R.color.tvColor))

                tvUpdatePass.setTextColor(resources.getColor(R.color.red))




            }

        }


    }

    override fun onClick(v: View?) {

       updatefromServer()
    }

private fun updatefromServer() {
    if (!validate()) {

    } else {

        operation()
    }

    }

    private fun operation() {
        progressDialog = ProgressDialog(this,
            R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Updating...")
        progressDialog.show()

        val empId =pref.user.employeeId
        val email = etUpdateEmail.text.toString().trim { it <= ' ' }
        val password = etUpdatePass.text.toString().trim { it <= ' ' }
        val mobile = etUpdatemobile.text.toString().trim { it <= ' ' }

        if (this?.let { Cons.isNetworkAvailable(it) }) {
            val call = RetrofitClient
                .instance.api.updateUser(empId, email, mobile, password)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val updateResponse = response.body()

                    if (updateResponse!!.status == "0") {
                        progressDialog.dismiss()
                        Toast.makeText(this@UpdateActivity, "" + updateResponse.message, Toast.LENGTH_LONG).show()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this@UpdateActivity, "" + updateResponse.message, Toast.LENGTH_LONG).show()
                        this.let {
                            SharedPrefManager.getInstance(this@UpdateActivity)
                                .saveUser(updateResponse.userData)

                            finish()
                        }

                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                    Toast.makeText(this@UpdateActivity, t.message, Toast.LENGTH_LONG).show()

                }
            })

        }
    }

    private fun validate(): Boolean {
        var valid = true

        val emailId = etUpdateEmail!!.text.toString()
        val mobile = etUpdatemobile!!.text.toString()
        val empPass = etUpdatePass.text.toString()

        if (emailId.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            etUpdateEmail!!.error = "Enter valid Email Address"
            valid = false
        } else {
            etUpdateEmail.error = null
        }


        if (mobile.isEmpty() || mobile.length < 10) {
            etUpdatemobile!!.error = "Enter valid  Mobile No"
            valid = false
        } else {
            etUpdatemobile.error = null
        }

        if (empPass.isEmpty() || empPass.length < 6) {
            etUpdatePass!!.error = "Your Password must be at least 6 characters"
            valid = false
        } else {
            etUpdatePass.error = null
        }
        return valid
    }

}
