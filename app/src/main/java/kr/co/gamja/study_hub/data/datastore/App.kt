package kr.co.gamja.study_hub.data.datastore

import android.app.Application
import java.io.File

// DataStore 싱글톤
class App:Application() {
    private lateinit var datastore:DataStoreModule

    companion object{
        private lateinit var app:App
        fun getInstance():App= app
    }

    override fun onCreate() {
        super.onCreate()
//        val dexOutputDir: File =codeCacheDir
//        dexOutputDir.setReadOnly()

        app=this
        datastore=DataStoreModule(this)
    }
    fun getDataStore():DataStoreModule=datastore
}