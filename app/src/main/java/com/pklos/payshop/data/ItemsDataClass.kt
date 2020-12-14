package com.pklos.payshop.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

enum class Category{
    HOME, SPORT, FOOD
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
    var retreivedData = hashMapOf<Int, String>()

    private var db = Firebase.firestore
    val firebaseData = db.collection("items").get()
        .addOnSuccessListener { docs ->
            for (document in docs){
                Log.d("docs", "${document.id} => ${document.data}")
                retreivedData.put(document.id.toInt(), document.data.toString())
            }
        }
        .addOnFailureListener { exception ->
            Log.w("docs", "Error getting documents: ", exception)
        }
}
