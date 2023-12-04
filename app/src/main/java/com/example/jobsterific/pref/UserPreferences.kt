

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.jobsterific.pref.UploadResumeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSessionPathResume(user: UploadResumeModel) {
        dataStore.edit { preferences ->
            preferences[RESUME_URI] = user.uriPdf
            preferences[RESUME_NAME] = user.fileName
        }
    }


    fun getSessionPathResume(): Flow<UploadResumeModel> {
        return dataStore.data.map { preferences ->
            UploadResumeModel(
                preferences[RESUME_URI] ?: "",
                preferences[RESUME_NAME] ?: "",

            )
        }
    }





    suspend fun deleteResume() {
        dataStore.edit { preferences ->
            preferences.remove(RESUME_URI)
            preferences.remove(RESUME_NAME)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val RESUME_URI = stringPreferencesKey("resume_uri")
        private val RESUME_NAME = stringPreferencesKey("resume_name")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}