package com.globals.netconnect.netconnect.fragment


import android.app.ProgressDialog
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.app.SharedPrefManager
import com.globals.netconnect.netconnect.app.Cons
import com.globals.netconnect.netconnect.helper.RetrofitClient
import com.globals.netconnect.netconnect.model.LoginResponse
import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.OnDrawableClickListener
import kotlinx.android.synthetic.main.fragment_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



@Suppress("DEPRECATION")
class SignUpFragment : Fragment(), View.OnFocusChangeListener, View.OnClickListener {



    private lateinit var mTvSignUpEmpId: TextView
    private lateinit var mTvSignUpEmail:TextView
    private lateinit var mTvSignUpMobile: TextView
    private lateinit var mTvSignUpPassword:TextView
    private lateinit var mTvSignUpRePassword:TextView
    private lateinit var mSignUpEmpId: EditText
    private lateinit var mSignUpEmail: EditText
    private lateinit var mSignUpMobile: EditText
    private lateinit var mSignUpPassword: EditText
    private lateinit var mSignUpRePassword :EditText
    private lateinit var btSignUpSubmit : Button
    private lateinit var progressDialog:ProgressDialog

    private lateinit var pref: SharedPrefManager

    private var status: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        (activity as AppCompatActivity).supportActionBar!!.show()
        (activity as AppCompatActivity).title = "Sign Up"
        InitializationLayout(view)

        return view

    }

    private fun InitializationLayout(view: View) {

        mTvSignUpEmpId = view.findViewById(R.id.tvSignUpEmpId)
        mTvSignUpEmail = view.findViewById(R.id.tvSignUpEmail)
        mTvSignUpMobile = view.findViewById(R.id.tvSign_up_Mobile)
        mTvSignUpPassword = view.findViewById(R.id.tvSignPass)
        mTvSignUpRePassword = view.findViewById(R.id.tvSignRePass)
        mSignUpEmpId = view.findViewById(R.id.et_signup_employee_id)
        mSignUpEmail = view.findViewById(R.id.et_sign_up_email)
        mSignUpMobile = view.findViewById(R.id.et_sign_up_mobile)
        mSignUpPassword = view.findViewById(R.id.et_sign_up_password)
        mSignUpRePassword = view.findViewById(R.id.et_sign_up_re_password)

        btSignUpSubmit = view.findViewById(R.id.bt_sign_up_submit)


        mSignUpEmpId.onFocusChangeListener = this
        mSignUpEmail.onFocusChangeListener = this
        mSignUpMobile.onFocusChangeListener = this
        mSignUpPassword.onFocusChangeListener = this
        mSignUpRePassword.onFocusChangeListener = this

        btSignUpSubmit.setOnClickListener(this)

        pref = SharedPrefManager.getInstance(this.context!!)



        val fields = Typeface.createFromAsset(context!!.assets, "fonts/lato_regular.ttf")
        val fieldsBt = Typeface.createFromAsset(context!!.assets, "fonts/lato_semibold.ttf")

        mTvSignUpEmpId.typeface = fields
        mTvSignUpEmail.typeface = fields
        mTvSignUpMobile.typeface = fields
        mTvSignUpPassword.typeface = fields
        mTvSignUpRePassword.typeface = fields

        mSignUpEmpId.typeface = fields
        mSignUpEmail.typeface = fields
        mSignUpMobile.typeface = fields
        mSignUpPassword.typeface = fields
        mSignUpRePassword.typeface = fields

        btSignUpSubmit.typeface = fieldsBt
        mSignUpPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0)
        mSignUpRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0)

    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.et_signup_employee_id -> {
                mSignUpEmail.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                mSignUpMobile.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                mSignUpPassword.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                mSignUpRePassword.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                mSignUpEmpId.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)
                mTvSignUpEmpId.setTextColor(resources.getColor(R.color.red))

                mTvSignUpEmail.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpMobile.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpRePassword.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpPassword.setTextColor(resources.getColor(R.color.tvColor))

            }

            R.id.et_sign_up_email -> {
                mSignUpEmpId.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                mSignUpMobile.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                mSignUpPassword.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                mSignUpRePassword.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                mSignUpEmail.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)
                mTvSignUpEmail.setTextColor(resources.getColor(R.color.red))

                mTvSignUpEmpId.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpMobile.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpRePassword.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpPassword.setTextColor(resources.getColor(R.color.tvColor))

            }

            R.id.et_sign_up_mobile -> {
                mSignUpEmpId.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                mSignUpEmail.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                mSignUpPassword.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                mSignUpRePassword.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                mSignUpMobile.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)
                mTvSignUpMobile.setTextColor(resources.getColor(R.color.red))

                mTvSignUpEmpId.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpEmail.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpRePassword.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpPassword.setTextColor(resources.getColor(R.color.tvColor))


            }

            R.id.et_sign_up_password -> {
                mSignUpEmpId.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                mSignUpMobile.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                mSignUpEmail.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                mSignUpRePassword.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                mSignUpPassword.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)
                mTvSignUpPassword.setTextColor(resources.getColor(R.color.red))

                mTvSignUpEmpId.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpMobile.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpRePassword.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpEmail.setTextColor(resources.getColor(R.color.tvColor))



            }

            R.id.et_sign_up_re_password -> {
                mSignUpEmpId.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                mSignUpMobile.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)
                mSignUpPassword.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                mSignUpEmail.background.setColorFilter(resources.getColor(R.color.dimBlack),
                    PorterDuff.Mode.SRC_ATOP)
                mSignUpRePassword.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)
                mTvSignUpRePassword.setTextColor(resources.getColor(R.color.red))

                mTvSignUpEmpId.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpMobile.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpEmail.setTextColor(resources.getColor(R.color.tvColor))
                mTvSignUpPassword.setTextColor(resources.getColor(R.color.tvColor))

            }
        }


    }

    override fun onClick(v: View?) {

        progressDialog = ProgressDialog(context,
            R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Login...")
        progressDialog.show()
        signUp()




    }

    private fun signUp() {
        if (!validate()) {


            return
        }
        else{

            operation()
        }


//
//        android.os.Handler().postDelayed(
//            {
//                progressDialog.dismiss()
//            }, 3000)
    }



    private fun operation() {

        val empId = mSignUpEmpId.text.toString().trim { it <= ' ' }
        val empEmail = mSignUpEmail.text.toString().trim { it <= ' ' }
        val mobile = mSignUpMobile.text.toString().trim { it <= ' ' }
        val password = mSignUpPassword.text.toString().trim { it <= ' ' }
        val tokenId = pref.tokenToServer.tokenID

        if (context?.let { Cons.isNetworkAvailable(it) }!!) {
            val call = RetrofitClient
                .instance.api.registerUser(empId, empEmail, mobile, password,tokenId)

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
                        context?.let {
                            SharedPrefManager.getInstance(it)
                                .saveUser(loginResponse.userData)


                        }

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

    //Validation method   //-----
    private fun validate(): Boolean {
        var valid = true

        val empId = mSignUpEmpId!!.text.toString()
        val empPass = mSignUpPassword.text.toString()
        val empMobile = mSignUpMobile!!.text.toString()
        val empRepass = mSignUpRePassword.text.toString()
        val empEmail = mSignUpEmail!!.text.toString()


        if (empId.isEmpty() || empId.length < 5) {
            mSignUpEmpId!!.error = "Enter valid Employee Id"
            valid = false
        } else {
            mSignUpEmpId.error = null
        }


        if (empEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(empEmail).matches()) {
            mSignUpEmail!!.error = "Enter valid Email Address"
            valid = false
        } else {
            mSignUpEmail.error = null
        }


        if (empMobile.isEmpty() || empMobile.length < 10) {
            mSignUpMobile!!.error = "Enter valid  Mobile No"
            valid = false
        } else {
            mSignUpMobile.error = null
        }

        if (empPass.isEmpty() || empPass.length < 6) {
            mSignUpPassword!!.error = "Your Password must be at least 6 characters"
            valid = false
        } else {
            mSignUpPassword.error = null
        }


        if (empRepass.isEmpty() || empRepass != empPass) {
            mSignUpRePassword!!.error = "Password must be same"
            valid = false
        } else {
            mSignUpRePassword.error = null
        }

        return valid
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_sign_up_password.setDrawableClickListener(object : OnDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                if (status == 1) {
                    DrawablePosition.RIGHT
                    mSignUpPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0)
                    mSignUpPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    status = 0
                } else {
                    DrawablePosition.RIGHT
                    mSignUpPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password_icon, 0)
                    mSignUpPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    status = 1
                }
            }
        })

        et_sign_up_re_password.setDrawableClickListener(object : OnDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                if (status == 1) {
                    DrawablePosition.RIGHT
                    mSignUpRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0)
                    mSignUpRePassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    status = 0
                } else {
                    DrawablePosition.RIGHT
                    mSignUpRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password_icon, 0)
                    mSignUpRePassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    status = 1
                }
            }
        })

    }




}
