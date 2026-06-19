package com.tanfullstack.courseapp.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tanfullstack.courseapp.data.models.Module
import com.tanfullstack.courseapp.databinding.ItemModuleBinding

class ModuleAdapter(
    private val modules: List<Module>,
    private val completedLessons: Set<String>,
    private val onClick: (Module) -> Unit
) : RecyclerView.Adapter<ModuleAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemModuleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(module: Module) {
            binding.tvModuleTitle.text = module.title
            binding.tvModuleDesc.text = module.description
            binding.tvModuleIcon.text = module.icon

            val completed = module.lessons.count { completedLessons.contains(it.id) }
            val total = module.lessons.size

            binding.tvModuleLessons.text = "$total lessons"
            binding.tvModuleProgress.text = "$completed/$total"

            try {
                binding.tvModuleIcon.setBackgroundColor(
                    Color.parseColor(module.color).let { col ->
                        Color.argb(30, Color.red(col), Color.green(col), Color.blue(col))
                    }
                )
            } catch (e: Exception) { /* use default */ }

            binding.root.setOnClickListener { onClick(module) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(modules[position])
    }

    override fun getItemCount() = modules.size
}