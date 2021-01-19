package com.pklos.payshop.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

enum class Category{
    HOME, SPORT, FOOD, NONE
}

data class Item(val id: Int, val name: String, val category: Category, val price: Float, var isFavorite: Boolean, val imageUrl: String, val itemDescription: String) {
    override fun toString(): String {
        return "Id: $id \n Name: $name \n Category: $category \n Price: $price \n Is favorite: $isFavorite \n Image URL: $imageUrl \n Item Description: "
    }
}

object FirebaseData{
    private var db = Firebase.firestore
    private lateinit var dataItem: Item

    private fun String.getItemProperty(item: Int) = removePrefix("{").removeSuffix("}").split(", ")[item].substringAfter("=")

    fun firebaseDataDownload(clb: MyCallback){
        db.collection("items").get()
            .addOnSuccessListener { docs ->
                val dataList = mutableListOf<Item>()
                var id = 0
                for (item in docs){
                    Log.d("item", item.data.toString())
                    val imageUrl = item.data.toString().getItemProperty(0)
                    val price = item.data.toString().getItemProperty(1).toFloat()
                    val itemDescription = item.data.toString().getItemProperty(2)
                    val category = item.data.toString().getItemProperty(3)
                    val isFavorite = item.data.toString().getItemProperty(4).toBoolean()

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
                        isFavorite = isFavorite,
                        imageUrl = imageUrl,
                        itemDescription = itemDescription
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

    fun firebaseUpdateFavoriteStatus(docToUpdate: String/*, newValue: Boolean*/){
        val sfDocRef = db.collection("items").document(docToUpdate)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)

            val newFavoriteStatus = !(snapshot.getBoolean("isFavorite")!!)
            transaction.update(sfDocRef, "isFavorite", newFavoriteStatus)
            //transaction.update(sfDocRef, "isFavorite", newValue)

            null
        }.addOnSuccessListener { Log.d("updateData", "Transaction success!") }
            .addOnFailureListener { e -> Log.w("updateData", "Transaction failure.", e) }
    }
}