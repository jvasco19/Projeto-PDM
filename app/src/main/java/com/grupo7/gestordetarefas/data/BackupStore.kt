package com.grupo7.gestordetarefas.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File


class BackupStore(private val filesDir: File) {


    suspend fun export(tasks: List<Task>): Int = withContext(Dispatchers.IO) {
        val json = JSONArray()
        tasks.forEach { t ->
            json.put(
                JSONObject()
                    .put("id", t.id)
                    .put("title", t.title)
                    .put("done", t.done)
                    .put("createdAt", t.createdAt)
            )
        }
        File(filesDir, FILE_NAME).writeText(json.toString(2))
        tasks.size
    }

    companion object {
        const val FILE_NAME = "backup.json"
    }
}