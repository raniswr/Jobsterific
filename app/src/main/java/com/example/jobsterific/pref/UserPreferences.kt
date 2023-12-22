

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.jobsterific.pref.UploadResumeModel
import com.example.jobsterific.pref.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    //Authentication
    suspend fun saveSessionUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = user.token
            preferences[USERID_KEY] = user.userId
            preferences[EMAIL_KEY] = user.email
            preferences[PASSWORD_KEY] = user.password
            preferences[FIRST_NAME_KEY] = user.firstName
            preferences[LAST_NAME_KEY] = user.lastName
            preferences[PROFILE_KEY] = user.profile
            preferences[STATUS_KEY] = user.status
            preferences[JOB_KEY] = user.job
            preferences[SEX_KEY] = user.sex
            preferences[ADRESS_KEY] = user.adress
            preferences[WEBSITE_KEY] = user.website
            preferences[DESCRIPTION_KEY] = user.description
            preferences[IS_CUSTOMER_KEY] = user.isCostumer ?: false
            preferences[IS_ADMIN_KEY] = true
            preferences[IS_LOGIN_KEY] = true
        }
    }


    fun getSessionUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[TOKEN_KEY] ?: "",
                preferences[USERID_KEY] ?: "",
                preferences[FIRST_NAME_KEY] ?: "",
                preferences[LAST_NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[PHONE_KEY] ?: 0,
                preferences[PROFILE_KEY] ?: "",
                preferences[SEX_KEY] ?: "",
                preferences[STATUS_KEY] ?: false,
                preferences[JOB_KEY] ?: "",
                preferences[ADRESS_KEY] ?: "",
                preferences[WEBSITE_KEY] ?: "",
                preferences[DESCRIPTION_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[IS_ADMIN_KEY] ?: false,
                preferences[IS_CUSTOMER_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    //Resume
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
        //Resume
        private val RESUME_URI = stringPreferencesKey("resume_uri")
        private val RESUME_NAME = stringPreferencesKey("resume_name")
        //Auth
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USERID_KEY = stringPreferencesKey("userId")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val FIRST_NAME_KEY = stringPreferencesKey("firstName")
        private val LAST_NAME_KEY = stringPreferencesKey("lastName")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val PHONE_KEY = intPreferencesKey("phone")
        private val PROFILE_KEY = stringPreferencesKey("profile")
        private val IS_CUSTOMER_KEY = booleanPreferencesKey("isCustomer")
        private val IS_ADMIN_KEY =  booleanPreferencesKey("isAdmin")
        private val STATUS_KEY =  booleanPreferencesKey("status")
        private val JOB_KEY = stringPreferencesKey("job")
        private val SEX_KEY = stringPreferencesKey("sex")
        private val ADRESS_KEY = stringPreferencesKey("adress")
        private val WEBSITE_KEY = stringPreferencesKey("website")
        private val DESCRIPTION_KEY = stringPreferencesKey("description")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}