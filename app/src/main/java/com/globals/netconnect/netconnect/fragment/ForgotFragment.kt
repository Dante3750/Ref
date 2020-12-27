package com.globals.netconnect.netconnect.fragment

import android.app.ProgressDialog
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.app.Cons
import com.globals.netconnect.netconnect.helper.RetrofitClient
import com.globals.netconnect.netconnect.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotFragment : Fragment(), View.OnFocusChangeListener, View.OnClickListener {



    private lateinit var mFEmpId: EditText
    private lateinit var btFsubmit: Button
    private lateinit var mFtvEmp: TextView
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_forgot, container, false)
        (activity as AppCompatActivity).supportActionBar!!.show()
        (activity as AppCompatActivity).title = "Forgot Password"

        mFEmpId = view.findViewById(R.id.et_forgot__employee_id)
        btFsubmit = view.findViewById(R.id.bt_forgot_submit)
        mFtvEmp = view.findViewById(R.id.tv_forgot_EmpId)

        val fields = Typeface.createFromAsset(context!!.assets, "fonts/lato_regular.ttf")
        val fieldsBt = Typeface.createFromAsset(context!!.assets, "fonts/lato_semibold.ttf")

        mFtvEmp.typeface = fields
        mFEmpId.typeface = fields
        btFsubmit.typeface = fieldsBt

        mFEmpId.onFocusChangeListener = this
        btFsubmit.setOnClickListener(this)

        return view
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {

        when (v?.id) {
            R.id.et_forgot__employee_id -> {
                mFEmpId.background.setColorFilter(
                    resources.getColor(R.color.red),
                    PorterDuff.Mode.SRC_ATOP
                )
                mFtvEmp.setTextColor(resources.getColor(R.color.red))
            }
        }
    }

    override fun onClick(v: View?) {
        progressDialog = ProgressDialog(context,
            R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Login...")
        progressDialog.show()
        forgotPass()
    }

    private fun forgotPass() {

        val empId = mFEmpId!!.text.toString()

        if (empId.isEmpty() || empId.length < 5) {
            mFEmpId!!.error = "Enter valid Employee Id"

        } else {
            mFEmpId.error = null
            operation()

        }

    }

    private fun operation() {
        val emp = mFEmpId.text.toString().trim { it <= ' ' }

        if (context?.let { Cons.isNetworkAvailable(it) }!!) {


            val call = RetrofitClient
                .instance.api.forgetPassword(emp)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()


                    if (loginResponse!!.status == "0") {
                        progressDialog.dismiss()
                        Toast.makeText(context, "" + loginResponse.message, Toast.LENGTH_LONG).show()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(context, "" + loginResponse.message, Toast.LENGTH_LONG).show()
                        fragmentManager!!.popBackStackImmediate()
                    }

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()

                }
            })

        }
        else{
            Toast.makeText(context, Cons.NETWORK_ERROR, Toast.LENGTH_LONG).show()
        }
    }

}
