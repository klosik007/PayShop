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

object ExampleData{
    private val bread = Item(
        id = 0,
        name = "Bread",
        category = Category.FOOD,
        price = 2.50f,
        isFavorite = false
    )
    private val ball = Item(
        id = 1,
        name = "Ball",
        category = Category.SPORT,
        price = 50.0f,
        isFavorite = false
    )
    private val table = Item(
        id = 2,
        name = "Table",
        category = Category.HOME,
        price = 200.0f,
        isFavorite = false
    )
    private val bike = Item(
        id = 3,
        name = "Bike",
        category = Category.SPORT,
        price = 1500.0f,
        isFavorite = true
    )

    val dataList = listOf(bread, ball, table, bike)

    /*fun getItem(id: Int): Item? {
        for (item in dataList){
            if (item.id == id){
                return item
            }
        }

        return null
    }*/
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
                    Log.d("item", "${item.id}  ${item.data}")
                    val price = item.data.toString().removePrefix("{").removeSuffix("}").split(", ")[0].substringAfter("=").toFloat()
                    val category = item.data.toString().removePrefix("{").removeSuffix("}").split(", ")[1].substringAfter("\"").removeSuffix("\"")
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
                }
                clb.onCallback(dataList)
            }
            .addOnFailureListener { exception ->
                Log.w("docs", "Error getting documents: ", exception)
            }
    }
}