package com.grupo7.gestordetarefas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.grupo7.gestordetarefas.data.Task
import com.grupo7.gestordetarefas.data.TaskRepository
import com.grupo7.gestordetarefas.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repo: TaskRepository
    private lateinit var adapter: ArrayAdapter<String>

    private var tarefas: List<Task> = emptyList()

    private var selecionada: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = (application as TarefasApp).repository

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        binding.listaTarefas.adapter = adapter

        binding.listaTarefas.setOnItemClickListener { _, _, posicao, _ ->
            selecionada = tarefas[posicao]
            binding.etTarefa.setText(tarefas[posicao].title)
        }

        binding.btnAdicionar.setOnClickListener { adicionar() }
        binding.btnAtualizar.setOnClickListener { atualizar() }
        binding.btnConcluir.setOnClickListener { concluir() }
        binding.btnEliminar.setOnClickListener { eliminar() }
        binding.btnExportar.setOnClickListener { exportar() }

        binding.switchMostrarConcluidas.setOnCheckedChangeListener { _, ligado ->
            lifecycleScope.launch {
                repo.setShowDone(ligado)
            }
        }

        lifecycleScope.launch {
            repo.tasks.collect { lista ->
                mostrarTarefas(lista)
            }
        }

        lifecycleScope.launch {
            repo.showDone.collect { valor ->
                binding.switchMostrarConcluidas.isChecked = valor
            }
        }
    }

    private fun adicionar() {
        val texto = binding.etTarefa.text.toString().trim()
        if (texto.isEmpty()) {
            mostrarMensagem("Escreve uma tarefa primeiro")
            return
        }
        lifecycleScope.launch {
            repo.add(texto)
        }
        limparCampo()
    }

    private fun atualizar() {
        val tarefa = selecionada
        if (tarefa == null) {
            mostrarMensagem("Seleciona uma tarefa na lista")
            return
        }
        val texto = binding.etTarefa.text.toString().trim()
        if (texto.isEmpty()) {
            mostrarMensagem("O texto não pode estar vazio")
            return
        }
        lifecycleScope.launch {
            repo.updateTitle(tarefa, texto)
        }
        limparCampo()
    }

    private fun concluir() {
        val tarefa = selecionada
        if (tarefa == null) {
            mostrarMensagem("Seleciona uma tarefa na lista")
            return
        }
        lifecycleScope.launch {
            repo.toggleDone(tarefa)
        }
        limparCampo()
    }

    private fun eliminar() {
        val tarefa = selecionada
        if (tarefa == null) {
            mostrarMensagem("Seleciona uma tarefa na lista")
            return
        }
        lifecycleScope.launch {
            repo.delete(tarefa)
        }
        limparCampo()
    }

    private fun exportar() {
        lifecycleScope.launch {
            val total = repo.exportBackup()
            mostrarMensagem("Backup de $total tarefas guardado em backup.json")
        }
    }

    private fun mostrarTarefas(lista: List<Task>) {
        tarefas = lista
        adapter.clear()
        for (tarefa in lista) {
            if (tarefa.done) {
                adapter.add("✔ " + tarefa.title)
            } else {
                adapter.add(tarefa.title)
            }
        }
    }

    private fun limparCampo() {
        binding.etTarefa.text.clear()
        selecionada = null
    }

    private fun mostrarMensagem(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }
}
