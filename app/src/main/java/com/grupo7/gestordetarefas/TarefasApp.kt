package com.grupo7.gestordetarefas

import android.app.Application
import androidx.room.Room
import com.grupo7.gestordetarefas.data.AppDatabase
import com.grupo7.gestordetarefas.data.SettingsStore
import com.grupo7.gestordetarefas.data.BackupStore

class TarefasApp : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "tarefas.db").build()
    }
    val settings by lazy { SettingsStore(this) }
    val backup by lazy { BackupStore(filesDir) }
}