package com.grupo7.gestordetarefas.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")


class SettingsStore (private val context: Context){

    private val showDoneKey = booleanPreferencesKey("show_done")

    val showDone: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[showDoneKey] ?: true
    }

    suspend fun setShowDone(value: Boolean) {
        context.dataStore.edit { prefs -> prefs[showDoneKey] = value }
    }
}
