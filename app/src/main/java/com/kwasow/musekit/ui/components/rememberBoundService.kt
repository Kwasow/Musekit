package com.kwasow.musekit.ui.components

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
inline fun <reified BoundService : Service, reified BoundServiceBinder : Binder> rememberBoundLocalService(
    crossinline getService: @DisallowComposableCalls BoundServiceBinder.() -> BoundService,
): BoundService? {
    val context: Context = LocalContext.current
    var boundService: BoundService? by remember(context) { mutableStateOf(null) }
    val serviceConnection: ServiceConnection =
        remember(context) {
            object : ServiceConnection {
                override fun onServiceConnected(
                    className: ComponentName,
                    service: IBinder,
                ) {
                    boundService = (service as BoundServiceBinder).getService()
                }

                override fun onServiceDisconnected(arg0: ComponentName) {
                    boundService = null
                }
            }
        }

    DisposableEffect(context, serviceConnection) {
        context.bindService(
            Intent(context, BoundService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE,
        )
        onDispose { context.unbindService(serviceConnection) }
    }

    return boundService
}
