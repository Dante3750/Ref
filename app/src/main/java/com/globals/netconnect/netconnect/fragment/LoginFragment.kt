@file:Suppress("DEPRECATION")

package com.globals.netconnect.netconnect.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.globals.netconnect.netconnect.activity.HomeActivity


import com.globals.netconnect.netconnect.R

import com.globals.netconnect.netconnect.app.Cons
import com.globals.netconnect.netconnect.app.SharedPrefManager
import com.globals.netconnect.netconnect.helper.RetrofitClient
import com.globals.netconnect.netconnect.model.LoginResponse

import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.OnDrawableClickListener
import kotlinx.android.synthetic.main.fragment_login.*


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class LoginFragment : Fragment(), View.OnFocusChangeListener, View.OnClickListener {

    private lateinit var mEmpId: EditText
    private lateinit var pref: SharedPrefManager
    private lateinit var mEmpPass: EditText
    private lateinit var mTvEmp: TextView
    private lateinit var mTvPassword: TextView
    private var status: Int = 0
    private lateinit var btLogin: Button
    private lateinit var progressDialog:ProgressDialog
    private lateinit var mTvForgotPass: TextView



    //OnCreateView Part   //----

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)


        mEmpId = view.findViewById(R.id.et_employee_id)
        mEmpPass = view.findViewById(R.id.et_password)
        mTvEmp = view.findViewById(R.id.tvEmpId)
        mTvPassword = view.findViewById(R.id.tvEmpPass)
        btLogin = view.findViewById(R.id.bt_login)
        mTvForgotPass = view.findViewById(R.id.tv_forgot_pass)
        mEmpId.onFocusChangeListener = this
        mEmpPass.onFocusChangeListener = this


        pref = SharedPrefManager.getInstance(this.context!!)




        val fields = Typeface.createFromAsset(context!!.assets, "fonts/lato_regular.ttf")
        val fieldsBt = Typeface.createFromAsset(context!!.assets, "fonts/lato_semibold.ttf")
        mTvEmp.typeface = fields
        mEmpId.typeface = fields
        mEmpPass.typeface = fields
        mEmpId.typeface = fields
        mTvPassword.typeface = fields
        mTvEmp.typeface = fields
        btLogin.typeface = fieldsBt
        mTvForgotPass.typeface = fields
        mTvForgotPass.setTextColor(resources.getColor(R.color.forgotColor))

        mEmpPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0)
        (activity as AppCompatActivity).supportActionBar!!.show()
        (activity as AppCompatActivity).title = "Login"








        return view
    }


    //OnViewCreated  part   //----
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btLogin.setOnClickListener(this)
        tv_forgot_pass.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toForgot))
        et_password.setDrawableClickListener(object : OnDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                if (status == 1) {
                    DrawablePosition.RIGHT
                    mEmpPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0)
                    mEmpPass.transformationMethod = PasswordTransformationMethod.getInstance()
                    status = 0
                } else {
                    DrawablePosition.RIGHT
                    mEmpPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password_icon, 0)
                    mEmpPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    status = 1
                }
            }
        })
    }

    //EditText Focus Listeners
    @SuppressLint("ResourceAsColor")


    //OnEditTextFocusChange   //----
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.et_employee_id -> {
                mEmpPass.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )
                mEmpId.background.setColorFilter(
                    resources.getColor(R.color.red),
                    PorterDuff.Mode.SRC_ATOP
                )
                mTvEmp.setTextColor(resources.getColor(R.color.red))
                mTvPassword.setTextColor(resources.getColor(R.color.tvColor))
            }
            R.id.et_password -> {
                mEmpId.background.setColorFilter(
                    resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP
                )

                mEmpPass.background.setColorFilter(
                    resources.getColor(R.color.red),
                    PorterDuff.Mode.SRC_ATOP
                )
                mTvEmp.setTextColor(resources.getColor(R.color.tvColor))
                mTvPassword.setTextColor(resources.getColor(R.color.red))
            }
        }
    }


    //onClickOnSubmitButton  //----

    override fun onClick(v: View?) {
        progressDialog = ProgressDialog(context,
            R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Login...")
        progressDialog.show()
        login()


    }

    fun login() {


        if (!validate()) {

        } else {

            operation()
        }

    }


    private fun operation() {
        val emp = mEmpId.text.toString().trim { it <= ' ' }
        val password = mEmpPass.text.toString().trim { it <= ' ' }
        val tokenId = pref.tokenToServer.tokenID




        if (context?.let { Cons.isNetworkAvailable(it) }!!) {

            val call = RetrofitClient
                .instance.api.userData(emp, password ,tokenId)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (loginResponse!!.status == "0") {
                        progressDialog.dismiss()

                       // Toast.makeText(context, "" + loginResponse.message, Toast.LENGTH_LONG).show()
                    } else {
                        progressDialog.dismiss()
                        pref.setLoggedIn(activity!!.applicationContext, true)
                       // Toast.makeText(context, "" + loginResponse.message, Toast.LENGTH_LONG).show()
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        activity!!.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
                        context?.let {
                            SharedPrefManager.getInstance(it)
                                .saveUser(loginResponse.userData)
                            fragmentManager!!.popBackStackImmediate()
                        }

                    }

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }
            })

        } else {
            Toast.makeText(context, Cons.NETWORK_ERROR, Toast.LENGTH_LONG).show()
        }
    }


    //Validation method   //-----
    private fun validate(): Boolean {
        var valid = true

        val empId = mEmpId!!.text.toString()
        val empPass = mEmpPass.text.toString()

        if (empId.isEmpty() || empId.length < 5) {
            mEmpId!!.error = "Enter valid Employee Id"
            valid = false
        } else {
            mEmpId.error = null
        }

        if (empPass.isEmpty() || empPass.length < 6) {
            mEmpPass.error = "Your Password must be at least 6 characters"
            valid = false
        } else {
            mEmpPass.error = null
        }
        return valid
    }

    private fun onLoginSuccess() {

    }


}






