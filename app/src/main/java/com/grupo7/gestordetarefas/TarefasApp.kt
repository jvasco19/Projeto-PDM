package com.grupo7.gestordetarefas

import android.app.Application
import androidx.room.Room
import com.grupo7.gestordetarefas.data.AppDatabase

class TarefasApp : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "tarefas.db").build()
    }
}