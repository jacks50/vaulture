package ch.jacks.vaulture.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.jacks.vaulture.R
import ch.jacks.vaulture.db.JsonDbHelper
import ch.jacks.vaulture.db.entity.PasswordEntity
import java.net.URI

class PasswordAdapter(
    private var dataSet: MutableMap<String, PasswordEntity>,
    private var listener: (PasswordEntity) -> Unit
) : RecyclerView.Adapter<PasswordViewHolder>() {
    private var dataSetCopy: ArrayList<PasswordEntity> = ArrayList(dataSet.values)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.password_entity_layout, parent, false)

        return PasswordViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val password = dataSetCopy[position]

        //holder.passwordCardImage = password.passwordIcon
        holder.passwordCardName.text = password.passwordName
        holder.passwordCardUrl.text = URI(password.passwordURL).host

        holder.itemView.setOnClickListener { listener(password) }
    }

    override fun getItemCount(): Int {
        return dataSetCopy.size
    }

    fun filterDataSet(filter: String?) {
        dataSetCopy.clear()

        if (!filter.isNullOrEmpty())
            dataSet.forEach {
                if (it.value.passwordName.contains(filter, true) or it.value.passwordURL.contains(
                        filter,
                        true
                    )
                )
                    dataSetCopy.add(it.value)
            }
        else
            dataSetCopy = ArrayList(dataSet.values)

        notifyDataSetChanged()
    }

    fun updateDataSet() {
        dataSet = JsonDbHelper.getPasswords()
        dataSetCopy = ArrayList(dataSet.values)

        notifyDataSetChanged()
    }
}