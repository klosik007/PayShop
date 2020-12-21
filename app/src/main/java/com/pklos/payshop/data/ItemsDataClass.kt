package com.pklos.payshop.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

enum class Category{
    HOME, SPORT, FOOD, NONE
}

data class Item(val id: Int, val name: String, val category: Category, val price: Float, var isFavorite: Boolean) {
    override fun toString(): String {
        return "Id: $id \n Name: $name \n Category: $category \n Price: $price \n Is favorite: $isFavorite"
    }
}

object FirebaseData{
    private var db = Firebase.firestore

    private lateinit var dataItem: Item

    fun firebaseDataDownload(clb: MyCallback){
        db.collection("items").get()
            .addOnSuccessListener { docs ->
                val dataList = mutableListOf<Item>()
                var id = 0
                for (item in docs){
                    val price = item.data.toString().removePrefix("{").removeSuffix("}").split(", ")[0].substringAfter("=").toFloat()
                    val category = item.data.toString().removePrefix("{").removeSuffix("}").split(", ")[1].substringAfter("=")
                    val isFavorite = item.data.toString().removePrefix("{").removeSuffix("}").split(", ")[2].substringAfter("=").toBoolean()
                    dataItem = Item(
                        id = id,
                        name = item.id,
                        category = when(category){
                            "HOME" -> Category.HOME
                            "SPORT" -> Category.SPORT
                            "FOOD" -> Category.FOOD
                            else -> Category.NONE
                        },
                        price = price,
                        isFavorite = isFavorite
                    )
                    id.inc()
                    dataList.add(dataItem)
                    Log.d("fireBaseDownload", "${item.id} => ${item.data}")
                }
                clb.onCallback(dataList)
            }
            .addOnFailureListener { exception ->
                Log.w("docs", "Error getting documents: ", exception)
            }
    }
}