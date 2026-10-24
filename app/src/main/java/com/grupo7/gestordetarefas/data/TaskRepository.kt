package com.grupo7.gestordetarefas.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class TaskRepository(
    private val dao: TaskDao,
    private val settings: SettingsStore,
    private val backup: BackupStore
) {

    val showDone: Flow<Boolean> = settings.showDone

    val tasks: Flow<List<Task>> = combine(dao.observeAll(), settings.showDone) { list, show ->
        if (show) list else list.filterNot { it.done }
    }

    suspend fun add(title: String) {
        dao.insert(Task(title = title))
    }

    suspend fun updateTitle(task: Task, newTitle: String) {
        dao.update(task.copy(title = newTitle))
    }

    suspend fun toggleDone(task: Task) {
        dao.update(task.copy(done = !task.done))
    }

    suspend fun delete(task: Task) {
        dao.delete(task)
    }

    suspend fun setShowDone(value: Boolean) {
        settings.setShowDone(value)
    }

    suspend fun exportBackup(): Int = backup.export(dao.getAll())

    companion object {
        const val BACKUP_FILE = BackupStore.FILE_NAME
    }
}