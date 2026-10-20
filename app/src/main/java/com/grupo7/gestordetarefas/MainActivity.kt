package com.grupo7.gestordetarefas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo7.gestordetarefas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val tarefas = mutableListOf<String>()

    private lateinit var adapter: ArrayAdapter<String>

    private var posicaoSelecionada = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tarefas)
        binding.listaTarefas.adapter = adapter

        binding.listaTarefas.setOnItemClickListener { _, _, posicao, _ ->
            posicaoSelecionada = posicao
            binding.etTarefa.setText(tarefas[posicao])
        }

        binding.btnAdicionar.setOnClickListener { adicionarTarefa() }
        binding.btnAtualizar.setOnClickListener { atualizarTarefa() }
        binding.btnEliminar.setOnClickListener { eliminarTarefa() }
    }

    private fun adicionarTarefa() {
        val texto = binding.etTarefa.text.toString().trim()
        if (texto.isEmpty()) {
            mostrarMensagem("Escreve uma tarefa primeiro")
            return
        }
        tarefas.add(texto)
        atualizarLista()
    }

    private fun atualizarLista() {
        adapter.notifyDataSetChanged()
        limparCampos()
    }

    private fun atualizarTarefa() {
        if (posicaoSelecionada == -1) {
            mostrarMensagem("Seleciona uma tarefa na lista")
            return
        }
        val texto = binding.etTarefa.text.toString().trim()
        if (texto.isEmpty()) {
            mostrarMensagem("O texto não pode estar vazio")
            return
        }
        tarefas[posicaoSelecionada] = texto
        atualizarLista()
    }

    private fun eliminarTarefa() {
        if (posicaoSelecionada == -1) {
            mostrarMensagem("Seleciona uma tarefa na lista")
            return
        }
        tarefas.removeAt(posicaoSelecionada)
        atualizarLista()
    }

    private fun limparCampos() {
        binding.etTarefa.text.clear()
        posicaoSelecionada = -1
    }

    private fun mostrarMensagem(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }
}
