package com.wxeapapp.ui.select

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.nickming.wxeap.utils.applyStatusBar
import com.wxeapapp.R
import com.wxeapapp.api.request.LoginResponse
import com.wxeapapp.base.BaseActivity
import com.wxeapapp.ui.web.CloseEvent
import com.wxeapapp.ui.web.WebActivity
import com.wxeapapp.utils.Constant
import com.wxeapapp.utils.java.SPUtil
import kotlinx.android.synthetic.main.activity_selection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class SwitchSystemActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        applyStatusBar(Color.parseColor("#000000"), 0.3f)
        back.setOnClickListener { finish() }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onResponseEvent(loginResponse: LoginResponse): Unit {
        val source = loginResponse.data
        val adapter = SwitchAdapter(source as ArrayList<LoginResponse.Item>)
        selectionRv.apply {
            this.layoutManager = LinearLayoutManager(this@SwitchSystemActivity, LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }
        adapter.callback = { view: View, i: Int ->
            EventBus.getDefault().post(CloseEvent(true))
            SPUtil.put(this, SPUtil.SWITCH_SYSTEM_TYPE_URL, source[i].ArgFullAddress)
            SPUtil.put(this, SPUtil.COMPANY_NAME, source[i].RegShortName)
            val intent = Intent(this@SwitchSystemActivity, WebActivity::class.java)
            intent.putExtra(Constant.WEB_MODE, WebActivity.MODE_INDEX)
            intent.putExtra(Constant.PARAM_URL, source[i].ArgFullAddress + Constant.DEFAULT_URL)
            startActivity(intent)
            finish()
        }
    }
}
