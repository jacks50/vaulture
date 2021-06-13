package ch.jacks.vaulture.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.jacks.vaulture.R
import ch.jacks.vaulture.db.dao.PasswordDao
import ch.jacks.vaulture.db.entity.PasswordEntity
import ch.jacks.vaulture.util.SessionUtil

class PasswordAdapter(
        private var dataSet: ArrayList<PasswordEntity>,
        private var listener: (PasswordEntity) -> Unit
): RecyclerView.Adapter<PasswordViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.password_entity_layout, parent, false)

        return PasswordViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val password: PasswordEntity = dataSet[position]

        //holder.passwordCardImage = password.passwordIcon
        holder.passwordCardName.text = password.passwordName
        holder.passwordCardUrl.text = password.passwordURL

        holder.itemView.setOnClickListener{ listener(password) }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateDataSet() {
        dataSet = PasswordDao.getPasswords(SessionUtil.currentLoginId)
        notifyDataSetChanged()
    }
}