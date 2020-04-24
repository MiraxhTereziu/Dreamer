package com.miraxh.dreamer.util

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.miraxh.dreamer.util.LIFECYCLE_TAG

class MyLifeCycleObserver: LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateEvent(){
        Log.i(LIFECYCLE_TAG,"onCreate")
    }

    //mettere in questo metodo i vari restore di dati
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumevent(){
        Log.i(LIFECYCLE_TAG,"onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStartEvent(){
        Log.i(LIFECYCLE_TAG,"onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopEvent(){
        Log.i(LIFECYCLE_TAG,"onStop")
    }

    //mettere in questo metodo tutti i salvataggi dei data
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseEvent(){
        Log.i(LIFECYCLE_TAG,"onPause")
    }
}