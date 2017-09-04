package com.wxeapapp.ui.web

import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.LinearLayout
import com.google.gson.Gson
import com.just.library.AgentWeb
import com.tencent.android.tpush.XGPushManager
import com.wxeapapp.R
import com.wxeapapp.api.LoginApi
import com.wxeapapp.api.request.LoginResponse
import com.wxeapapp.base.BaseActivity
import com.wxeapapp.model.PayLoad
import com.wxeapapp.ui.login.LoginActivity
import com.wxeapapp.ui.select.SwitchSystemActivity
import com.wxeapapp.utils.Constant
import com.wxeapapp.utils.L
import com.wxeapapp.utils.cookies.CookieHelper
import com.wxeapapp.utils.java.AndroidBug5497Workaround
import com.wxeapapp.utils.java.GifSizeFilter
import com.wxeapapp.utils.java.ImageUtil
import com.wxeapapp.utils.java.SPUtil
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.PicassoEngine
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.CaptureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_web.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class WebActivity : BaseActivity(), IWebActionDelegate {

    companion object {
        val MODE_INDEX = 0x11
        val MODE_NORMAL = 0x12
    }


    val REQUEST_CODE_CHOOSE = 0x11
    var mUrl: String = ""
    var mMode: Int = MODE_NORMAL
    var mResponse: LoginResponse? = null


    lateinit var mAgentWeb: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        //源自Stack Overflow解决Android系统bug，全屏模式webview被软键盘遮挡bug
        AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content))

        try {
            initViews()
        } catch (e: Exception) {
            //如果报错则让应用重启
            val intent = baseContext.packageManager.getLaunchIntentForPackage(packageName)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }


    private fun initViews() {
        mMode = intent.getIntExtra(Constant.WEB_MODE, MODE_NORMAL)


        if (intent.getStringExtra(Constant.PARAM_URL) != null) {
            mUrl = intent.getStringExtra(Constant.PARAM_URL)
        }

        val sid = SPUtil.get(this, SPUtil.NET_SessionId, "") as String
        val token = SPUtil.get(this, SPUtil.AppCloudToken, "") as String
        val cookies = arrayListOf(token, sid)
        //第一套解决方案
        L(sid, "handle")
        L(token, "handle")
        try {
            CookieHelper.setCookie(mMode == MODE_INDEX, "cloud.wy800.com", cookies, this)
//            AgentWebConfig.syncCookie("cloud.wy800.com", token)
//            AgentWebConfig.syncCookie("cloud.wy800.com", sid)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (mMode == MODE_INDEX) {
            swipeBackLayout.setEnableGesture(false)
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(webContainer, LinearLayout.LayoutParams(-1, -1))
                    .closeProgressBar()
                    .setAgentWebSettings(CustomSetting())
                    .createAgentWeb()//
                    .ready()
                    .go(mUrl)
        } else {
            swipeBackLayout.setEnableGesture(true)
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(webContainer, LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()// 使用默认进度条
                    .defaultProgressBarColor() // 使用默认进度条颜色
                    .setAgentWebSettings(CustomSetting())
                    .createAgentWeb()//
                    .ready()
                    .go(mUrl)
        }
        mAgentWeb.jsInterfaceHolder.addJavaObject("android", AndroidInterface(this))

        if (mMode == MODE_INDEX) {
            webBackIv.visibility = View.GONE
        }
        webBackIv.setOnClickListener {
            if (!mAgentWeb.back())
                finish()
        }
    }


    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
        val notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifyManager.cancelAll()
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun onShouldPush(payLoad: PayLoad) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra(Constant.PARAM_URL, payLoad.payload!!.url)
        startActivity(intent)
    }

    override fun onTitleUpdate(payLoad: PayLoad) {
        if (payLoad.payload!!.title!!.isNotBlank()) {
            titleTv.text = payLoad.payload!!.title
            if (payLoad.payload!!.title.equals("我的") && mResponse?.data!!.size > 1) {
                val data = PayLoad("showSwitchSystem", null)
                postMessageToWeb(data)
            }
        }
    }

    fun postMessageToWeb(payLoad: PayLoad) {
        val message = Gson().toJson(payLoad)
        mAgentWeb.jsEntraceAccess.quickCallJs("getMessageFromAndroid", message)
    }

    override fun onGoBack(payLoad: PayLoad) {
        mAgentWeb.back()
    }

    override fun onShowImagePicker(payLoad: PayLoad) {

        Matisse.from(this@WebActivity)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .capture(true)
                .captureStrategy(
                        CaptureStrategy(true, "com.wxeapapp.fileprovider"))
                .maxSelectable(1)
                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE)
    }

    override fun onSwitchSystem(payLoad: PayLoad) {
        val intent = Intent(this, SwitchSystemActivity::class.java)
        startActivity(intent)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onResponseEvent(loginResponse: LoginResponse): Unit {
        mResponse = loginResponse
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCloseEvent(closeEvent: CloseEvent) {
        finish()
    }

    override fun onLogout(payLoad: PayLoad) {
        val exitDialog = AlertDialog.Builder(this)
                .setTitle("退出登录")
                .setMessage("真的要退出登录吗?")
                .setPositiveButton("确定", { dialogInterface: DialogInterface, i: Int ->
                    LoginApi.IMPL.signOut()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                if (it.result == 0) {
                                    SPUtil.clear(this)
                                    XGPushManager.unregisterPush(this)
                                    mAgentWeb.clearWebCache()
                                    EventBus.getDefault().removeStickyEvent(LoginResponse::class.java)
                                    startActivity(Intent(this@WebActivity, LoginActivity::class.java))
                                    finish()
                                } else {
                                    toast(it.errmsg)
                                }
                            }
                })
                .setNegativeButton("取消", { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
                .create()
        exitDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            val selected: List<String> = Matisse.obtainPathResult(data)
            if (selected.size >= 1) {
                Observable.create<Boolean> {
                    val result = ImageUtil.bitmapToString(selected[0])
                    L(result.substring(result.length - 10))
                    val item = "data:image/jpeg;base64,$result"
                    val data = PayLoad("onImagePicked", PayLoad.Item(null, null, null, item))
                    val message = Gson().toJson(data, PayLoad::class.java)
                    mAgentWeb.jsEntraceAccess.quickCallJs("getMessageFromAndroid", message)
                    it.onNext(true)
                    it.onComplete()
                }.subscribeOn(Schedulers.io())
                        .subscribe {

                        }
            }
        }
    }

    override fun onBackPressed() {
        if (!mAgentWeb.back()) {
            when (mMode) {
                MODE_INDEX -> {
                    moveTaskToBack(false)
                }
                else -> {
                    finish()
                }
            }
        }
    }

}
