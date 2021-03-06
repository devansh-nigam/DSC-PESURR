package com.example.dscpesurr

data class Branch(val image:Int, val name:String)

object Branches{
    private val images= intArrayOf(R.drawable.logo,
            R.drawable.website,
            R.drawable.cpu,
            R.drawable.tower,
            R.drawable.construction,
            R.drawable.crane,
            R.drawable.dna)

    private val branches = arrayOf("Branch",
            "Computer Science",
            "Electronics & Communication",
            "Electrical & Electronics",
            "Mechanical",
            "Civil",
            "Biotechnology")


    var list:ArrayList<Branch>?=null
    get(){
        if(field!=null)
            return field

        field=ArrayList()
        for(i in images.indices){
            val imageId=images[i]
            val branchName= branches[i]

            val branch=Branch(imageId,branchName)
            field!!.add(branch)
        }
        return field
    }
}
