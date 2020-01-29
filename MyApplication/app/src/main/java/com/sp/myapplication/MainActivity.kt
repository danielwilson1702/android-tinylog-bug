package com.sp.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import androidx.appcompat.app.AppCompatActivity
import co.poynt.api.model.Business
import co.poynt.os.model.Intents
import co.poynt.os.model.PoyntError
import co.poynt.os.services.v1.IPoyntBusinessReadListener
import co.poynt.os.services.v1.IPoyntBusinessService
import org.tinylog.configuration.Configuration
import org.tinylog.kotlin.Logger

class MainActivity : AppCompatActivity() {

    private var poyntBusinessService: IPoyntBusinessService? = null

    val poyntBusinessServiceListener: IPoyntBusinessReadListener =
        object : IPoyntBusinessReadListener.Stub() {
            override fun onResponse(b: Business?, error: PoyntError?) {
                //This line crashes the app if Logger.trace has not been called yet
                Logger.trace("tinylog Logging Business response! " + b?.id)
            }
        }

    private val businessConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            poyntBusinessService = IPoyntBusinessService.Stub.asInterface(service)

            try {
                poyntBusinessService?.getBusiness(poyntBusinessServiceListener)
            } catch (e: RemoteException) {
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            poyntBusinessService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Configuration.set("writer.file", filesDir.path + "/logs/tiny_log_{count}.txt")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onResume() {
        super.onResume()

        //If this line is uncommented, tinylog does NOT crash
        //Logger.trace("tinylog binding to business service")

        bindService(
            Intents.getComponentIntent(Intents.COMPONENT_POYNT_BUSINESS_SERVICE),
            businessConnection, Context.BIND_AUTO_CREATE
        )
    }

    override fun onPause() {
        super.onPause()

        unbindService(businessConnection)
    }
}
