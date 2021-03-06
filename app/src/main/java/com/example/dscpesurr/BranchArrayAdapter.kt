package com.example.dscpesurr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.spinner_item.view.*

class BranchArrayAdapter(context: Context,branchList:List<Branch>):ArrayAdapter<Branch>(context,0,branchList)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup):View{
        val branch=getItem(position)
        val view=LayoutInflater.from(context).inflate(R.layout.spinner_item,parent,false)
        view.image_branch.setImageResource(branch!!.image)
        view.text_branch.text=branch.name
        return view
    }
}