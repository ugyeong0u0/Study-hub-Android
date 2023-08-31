package kr.co.gamja.study_hub.model.datastore

import android.app.Application
import android.content.Context

// DataStore 싱글톤
class App:Application() {
    private lateinit var datastore:DataStoreModule

    companion object{
        private lateinit var app:App
        fun getInstance():App= app
    }

    override fun onCreate() {
        super.onCreate()
        app=this
        datastore=DataStoreModule(this)
    }
    fun getDataStore():DataStoreModule=datastore
}