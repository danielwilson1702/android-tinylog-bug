package com.sp.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.poynt.api.model.Business
import co.poynt.os.model.Intents
import co.poynt.os.model.PoyntError
import co.poynt.os.services.v1.IPoyntBusinessReadListener
import co.poynt.os.services.v1.IPoyntBusinessService
import org.tinylog.kotlin.Logger

class MainFragment : Fragment() {

    private var poyntBusinessService: IPoyntBusinessService? = null

    val poyntBusinessServiceListener: IPoyntBusinessReadListener =
        object : IPoyntBusinessReadListener.Stub() {
            override fun onResponse(b: Business?, error: PoyntError?) {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onResume() {
        super.onResume()

        //Logger.trace("tinylog binding to business service")

        activity?.bindService(
            Intents.getComponentIntent(Intents.COMPONENT_POYNT_BUSINESS_SERVICE),
            businessConnection, Context.BIND_AUTO_CREATE
        )
    }

    override fun onPause() {
        super.onPause()

        //Logger.trace("tinylog unbinding from business service")

        activity?.unbindService(businessConnection)
    }
}