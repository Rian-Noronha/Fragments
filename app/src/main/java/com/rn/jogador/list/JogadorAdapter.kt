package com.rn.jogador.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import com.rn.jogador.databinding.ItemJogadorBinding
import com.rn.jogador.model.Jogador

class JogadorAdapter(context: Context, jogadores: List<Jogador>):
    ArrayAdapter<Jogador>(context, 0, jogadores){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemJogadorBinding
        val jogador = getItem(position)
        if(convertView == null){
            binding = ItemJogadorBinding.inflate(LayoutInflater.from(context), parent, false)
        }else{
            binding = ItemJogadorBinding.bind(convertView)
        }

        val viewHolder = ViewHolder(binding)
        viewHolder.txtName.text = jogador?.name
        viewHolder.rtbRating.rating = jogador?.rating ?: 0f

        return binding.root
    }


    class ViewHolder(val binding: ItemJogadorBinding){
        val txtName: TextView = binding.txtNome
        val rtbRating: RatingBar = binding.rtbRating
    }


}