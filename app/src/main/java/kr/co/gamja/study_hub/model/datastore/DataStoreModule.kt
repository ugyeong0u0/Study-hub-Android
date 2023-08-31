package kr.co.gamja.study_hub.model.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// preferences DataStore 생성
class DataStoreModule(private val context: Context) {
    private val Context.datastore by preferencesDataStore(name = "TokenStore")

    private val accessTokenKey = stringPreferencesKey("ACCESS_TOKEN")
    private val refeshTokenKey = stringPreferencesKey("REFRESH_TOKEN")

    // 읽기
    val accessToken: Flow<String> = context.datastore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }}.map{
                it[accessTokenKey]?:""
        }
    val refreshToken: Flow<String> = context.datastore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }}.map{
            it[refeshTokenKey]?:""
        }
    // 저장
    suspend fun setAccessToken(accessToken:String){
        context.datastore.edit {
            it[accessTokenKey]=accessToken
        }
    }
    suspend fun setRefreshToken(refreshToken:String){
        context.datastore.edit {
            it[refeshTokenKey]=refreshToken
        }
    }
}