package com.sp.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import co.poynt.api.model.Business
import co.poynt.os.model.Intents
import co.poynt.os.model.PoyntError
import co.poynt.os.services.v1.IPoyntBusinessReadListener
import co.poynt.os.services.v1.IPoyntBusinessService
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import loylap.core.sdk.data.local.analytics.AnalyticsHelper
import org.tinylog.configuration.Configuration
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var analytics: AnalyticsHelper

    private var poyntBusinessService: IPoyntBusinessService? = null

    val poyntBusinessServiceListener: IPoyntBusinessReadListener =
        object : IPoyntBusinessReadListener.Stub() {
            override fun onResponse(b: Business?, error: PoyntError?) {
                analytics.log("Logging Business response! " + b?.id)
            }
        }

    private val businessConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            poyntBusinessService = IPoyntBusinessService.Stub.asInterface(service)

            try {
                poyntBusinessService?.getBusiness(poyntBusinessServiceListener)
            } catch (e: RemoteException) { }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            poyntBusinessService = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        Configuration.set("writer.file", filesDir.path + "/logs/tiny_log_{count}.txt")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onResume() {
        super.onResume()

        analytics.log("binding to business service")

        bindService(
            Intents.getComponentIntent(Intents.COMPONENT_POYNT_BUSINESS_SERVICE),
            businessConnection, Context.BIND_AUTO_CREATE
        )
    }

    override fun onPause() {
        super.onPause()

        analytics.log("unbinding from business service")

        unbindService(businessConnection)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
