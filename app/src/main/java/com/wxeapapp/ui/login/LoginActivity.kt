package com.wxeapapp.ui.login

import android.app.Dialog
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.google.gson.Gson
import com.tencent.android.tpush.XGPushConfig
import com.wxeapapp.R
import com.wxeapapp.api.request.LoginResponse
import com.wxeapapp.base.BaseActivity
import com.wxeapapp.ui.select.SwitchSystemActivity
import com.wxeapapp.ui.web.WebActivity
import com.wxeapapp.utils.Constant
import com.wxeapapp.utils.LoginUtil
import com.wxeapapp.utils.java.KeyboardUtil
import com.wxeapapp.utils.java.SPUtil
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.yanzhenjie.permission.PermissionListener
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity(), LoginContract.View {


    var mPresenter: LoginPresenter = LoginPresenter(this)
    lateinit var mTipsDialog: Dialog
    lateinit var phoneDrawable: Drawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        swipeBackLayout.setEnableGesture(false)
        val sid = SPUtil.get(this, SPUtil.NET_SessionId, "") as String
        val token = SPUtil.get(this, SPUtil.AppCloudToken, "") as String
        if (sid.isNotBlank() && token.isNotBlank()) {
            transitionRoot.visibility = View.VISIBLE
            KeyboardUtil.hideSoftInput(this)
        }
        requestAllPermission()
        initViews()
    }

    override fun onResume() {
        super.onResume()
        val notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifyManager.cancelAll()
    }

    override fun autoLogin() {
        val sid = SPUtil.get(this, SPUtil.NET_SessionId, "") as String
        val token = SPUtil.get(this, SPUtil.AppCloudToken, "") as String
        if (!TextUtils.isEmpty(sid) && !TextUtils.isEmpty(token)) {
            var clientId: String = SPUtil.get(this, SPUtil.TOKEN, "") as String
            if (clientId.isBlank()) {
                clientId = XGPushConfig.getToken(this)
            }
            var map = hashMapOf(Pair<String, String>("clientid", clientId))
            mPresenter.login(map)
        }
    }

    override fun requestAllPermission() {
        AndPermission.with(this)
                .requestCode(0x11)
                .permission(
                        Permission.LOCATION,
                        Permission.CAMERA,
                        Permission.PHONE,
                        Permission.SMS,
                        Permission.STORAGE
                )
                .callback(object : PermissionListener {
                    override fun onSucceed(requestCode: Int, grantPermissions: MutableList<String>) = autoLogin()
                    override fun onFailed(requestCode: Int, deniedPermissions: MutableList<String>) = autoLogin()

                })
                .start()
    }


    override fun initViews() {
        mTipsDialog = AlertDialog.Builder(this)
                .setTitle("无法登陆")
                .setMessage("请联系您所在的系统管理员")
                .setPositiveButton("确定", { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                })
                .create()
        needHelpTv.setOnClickListener({
            showHelpDialog()
        })


        loginRootLl.setOnTouchListener { view, motionEvent ->
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
            KeyboardUtil.hideSoftInput(this@LoginActivity)
            false
        }

        initMobileEt()
        initVerifyCodeEt()
        initLoginBtn()
    }

    fun initVerifyCodeEt() {
        verifyCodeEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var mobile = mobileEt.text.toString()
                var code = p0.toString()
                if (LoginUtil.isPhoneNum(mobile) && code.length == 6) {
                    loginBtn.isEnabled = true
                    showLoginEnable(loginBtn.isEnabled)
                    KeyboardUtil.hideSoftInput(this@LoginActivity)
                }
            }
        })
    }

    /**
     * 初始化登陆控制
     */
    private fun initLoginBtn() {
        val token = SPUtil.get(this, SPUtil.AppCloudToken, "") as String
        if (token.isNotBlank()) {
            loginBtn.isEnabled = true
            showLoginEnable(loginBtn.isEnabled)
        } else {
            loginBtn.isEnabled = false
            showLoginEnable(loginBtn.isEnabled)
        }
        loginBtn.setOnClickListener({
            KeyboardUtil.hideSoftInput(this@LoginActivity)
            if (LoginUtil.isPhoneNum(mobileEt.text.toString()) && verifyCodeEt.text.toString().length == 6) {
                var mobile = mobileEt.text.toString()
                var code = verifyCodeEt.text.toString()
                var token = XGPushConfig.getToken(application)
                var map = hashMapOf(Pair<String, String>("mobile", mobile), Pair<String, String>("code", code), Pair<String, String>("clientid", token))
                mPresenter.login(map)
            } else {
                toast("信息不完整")
            }


        })
    }

    /**
     * 初始化登录名控件，包括装态的改变以及逻辑判断
     */
    private fun initMobileEt() {
        phoneDrawable = resources.getDrawable(R.mipmap.ic_phone)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            phoneDrawable.setTint(resources.getColor(R.color.gray))
        }
        phoneDrawable.setBounds(0, 0, phoneDrawable.minimumWidth, phoneDrawable.minimumHeight)
        mobileEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (LoginUtil.isPhoneNum(p0.toString())) {
                    sendVerifyTv.visibility = View.VISIBLE
                    mobileEt.setCompoundDrawables(null, null, null, null)
                    mobileEt.clearFocus()
                    KeyboardUtil.hideSoftInput(this@LoginActivity)
                } else {
                    sendVerifyTv.visibility = View.GONE
                    mobileEt.setCompoundDrawables(null, null, phoneDrawable, null)
                }
            }
        })
        mobileEt.setText("")
        sendVerifyTv.setOnClickListener({
            if (sendVerifyTv.isClickable) {
                mPresenter.requestVerifyCode(mobileEt.text.toString())
            }
        })


    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        this.mPresenter = presenter as LoginPresenter
    }

    override fun showToast(string: String) {
        toast(string)
    }


    override fun showHelpDialog() {
        if (!mTipsDialog.isShowing) {
            mTipsDialog.show()
        }
    }

    override fun showTimeTvState(enable: Boolean, text: String) {
        if (enable) {
            sendVerifyTv.isClickable = true
            sendVerifyTv.text = text
        } else {
            sendVerifyTv.isClickable = false
            sendVerifyTv.text = text
        }
    }

    override fun showStart() {
        loginBtn.startAnimation()
    }

    override fun showDone() {
        loginBtn.revertAnimation()
    }

    override fun jumpToWeb(loginResponse: LoginResponse) {
        EventBus.getDefault().postSticky(loginResponse)
        var addressUrl: String = ""
        var lastResJson: String = SPUtil.get(applicationContext, SPUtil.LAST_RESPONSE, "") as String
        if (lastResJson.isNotBlank()) {
            var lastRes = Gson().fromJson(lastResJson, LoginResponse::class.java)
            if (lastRes.data.size != loginResponse.data.size) {
                hideLoadingCompany()
                SPUtil.clear(applicationContext)
            } else {
                val lastIndex: Int = SPUtil.get(applicationContext, SPUtil.LAST_SYSTEM_INDEX, -1) as Int
                addressUrl = loginResponse.data[lastIndex].ArgFullAddress
                showLoadingAnimation(loginResponse.data[lastIndex].RegShortName, addressUrl)
            }

        } else {
            SPUtil.put(applicationContext, SPUtil.LAST_RESPONSE, Gson().toJson(loginResponse, LoginResponse::class.java))
            if (loginResponse.data.size == 1) {
                addressUrl = loginResponse.data[0].ArgFullAddress
                SPUtil.put(applicationContext, SPUtil.LAST_SYSTEM_INDEX, 0)
                showLoadingAnimation(loginResponse.data[0].RegShortName, addressUrl)
            } else {
                showSwitchSystem()
            }
        }
    }

    private fun openWebActivity(baseUrl: String) {
        var intent = Intent(this, WebActivity::class.java)
        intent.putExtra(Constant.PARAM_URL, baseUrl + Constant.DEFAULT_URL)
        intent.putExtra(Constant.WEB_MODE, WebActivity.MODE_INDEX)
        startActivity(intent)
        finish()
    }

    override fun showLoginEnable(enable: Boolean) {
        if (enable && loginBtn.isEnabled) {
            loginBtn.setBackgroundResource(R.drawable.btn_login_bg_enable)
        } else {
            loginBtn.setBackgroundResource(R.drawable.btn_login_bg)
        }
    }

    override fun closeSelf() {
        finish()
    }

    fun showLoadingAnimation(text: String, url: String) {
        companyNameTv.text = text
        companyNameTv.postDelayed({
            openWebActivity(url)
        }, 2000)
    }

    override fun hideLoadingCompany() {
        transitionRoot.visibility = View.GONE
    }

    override fun showLoadingCompany() {
        transitionRoot.visibility = View.VISIBLE
    }

    override fun showSwitchSystem() {
        startActivity(Intent(this, SwitchSystemActivity::class.java))
        finish()
    }


}
