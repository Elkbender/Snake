package no.elkbender.snake.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.elkbender.snake.R

class GameCache(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)
        val HIGH_SCORES_KEY = stringPreferencesKey(DATASTORE_KEY_HIGH_SCORES)
        val PLAYER_NAME_KEY = stringPreferencesKey(DATASTORE_KEY_PLAYER_NAME)
        val DIFFICULTY_KEY = intPreferencesKey(DATASTORE_KEY_DIFFICULTY)
        val WRAP_AROUND_KEY = intPreferencesKey(DATASTORE_KEY_WRAP_AROUND_MODE)
    }

    val getHighScores: Flow<List<HighScore>> = context.dataStore.data.map { preferences ->
        val scores = preferences[HIGH_SCORES_KEY]
        val listType = object : TypeToken<List<HighScore?>?>() {}.type
        Gson().fromJson<List<HighScore>>(scores, listType) ?: listOf()
    }

    val getPlayerName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PLAYER_NAME_KEY] ?: context.getString(R.string.default_player_name)
    }

    val getDifficulty: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[DIFFICULTY_KEY] ?: 1
    }

    val getWrapAroundMode: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[WRAP_AROUND_KEY] ?: 0
    }

    val getTheme: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[WRAP_AROUND_KEY] ?: 0
    }

    suspend fun saveHighScore(highScores: List<HighScore>) {
        context.dataStore.edit { preferences ->
            preferences[HIGH_SCORES_KEY] = Gson().toJson(highScores)
        }
    }

    suspend fun savePlayerName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PLAYER_NAME_KEY] = name
        }
    }

    suspend fun saveDifficulty(level: Int) {
        context.dataStore.edit { preferences ->
            preferences[DIFFICULTY_KEY] = level
            println("Saved difficulty: $level")
        }
    }

    suspend fun saveWrapAroundMode(mode: Int) {
        context.dataStore.edit { preferences ->
            preferences[WRAP_AROUND_KEY] = mode
        }
    }

    suspend fun saveTheme(mode: Int) {
        context.dataStore.edit { preferences ->
            preferences[WRAP_AROUND_KEY] = mode
        }
    }
}