package com.pklos.payshop.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pklos.payshop.data.FirebaseData.getItemProperty
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException

enum class Category{
    HOME, SPORT, FOOD, NONE
}

enum class OrderFilter{
    DESC, ASC
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
    private val dataList = mutableListOf<Item>()
    val checkboxFilters = mutableListOf<(Item) -> Boolean>()
    var priceFrom: Int = 0
    var priceTo: Int? = null

    private fun processDataItem(doc: QuerySnapshot): List<Item>{
        var id = 0
        for (item in doc){
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

        return dataList
    }

    private fun processDataItem(doc: DocumentSnapshot): List<Item>{
        Log.d("item", doc.data.toString())
        var id = 0
        val imageUrl = doc.data.toString().getItemProperty(0)
        val price = doc.data.toString().getItemProperty(1).toFloat()
        val itemDescription = doc.data.toString().getItemProperty(2)
        val category = doc.data.toString().getItemProperty(3)
        val isFavorite = doc.data.toString().getItemProperty(4).toBoolean()

        dataItem = Item(
            id = id,
            name = doc.id,
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
        Log.d("fireBaseDownload", "${doc.id} => ${doc.data}")

        return dataList
    }

    private fun firebaseDataDownload(clb: MyCallback){
        dataList.clear()

        db.collection("items").get()
            .addOnSuccessListener { doc ->
                processDataItem(doc)
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

    fun firebaseSearchByName(name: String, clb: MyCallback){
        try{
            val sfDocRef = db.collection("items").document(name)
            dataList.clear()

            sfDocRef.get().addOnSuccessListener { doc ->
                processDataItem(doc)
                clb.onCallback(dataList)
            }
        } catch (e: IllegalArgumentException){
            Log.w("firebaseSearchByName", "IllegalArgumentException - no text provided -> whole base download", e)
            firebaseDataDownload(clb)
        } catch (e: IndexOutOfBoundsException){
            Log.w("firebaseSearchByName", "IndexOutOfBoundsException - no such item in base", e)
        }
    }

    fun firebaseSortByCategoryName(order: OrderFilter, clb: MyCallback){
        try{
            if(dataList.isNotEmpty()) {
                when(order){
                    OrderFilter.DESC ->{
                        dataList.sortByDescending { it.category }
                    }

                    OrderFilter.ASC -> {
                        dataList.sortBy { it.category }
                    }
                }

                clb.onCallback(dataList)
            }
        }catch (e: IllegalArgumentException){
            Log.w(" firebaseSortByCategoryNameDescending", "IllegalArgumentException", e)
        } catch (e: IndexOutOfBoundsException){
            Log.w(" firebaseSortByCategoryNameDescending", "IndexOutOfBoundsException", e)
        }
    }

    fun firebaseSortByPrice(order: OrderFilter, clb: MyCallback){
        try{
            if(dataList.isNotEmpty()) {
                when(order){
                    OrderFilter.DESC ->{
                        dataList.sortByDescending { it.price }
                    }

                    OrderFilter.ASC -> {
                        dataList.sortBy { it.price }
                    }
                }

                clb.onCallback(dataList)
            }
        }catch (e: IllegalArgumentException){
            Log.w(" firebaseSortByCategoryNameDescending", "IllegalArgumentException", e)
        } catch (e: IndexOutOfBoundsException){
            Log.w(" firebaseSortByCategoryNameDescending", "IndexOutOfBoundsException", e)
        }
    }

    fun firebaseSortByItemName(order: OrderFilter, clb: MyCallback){
        try{
            if(dataList.isNotEmpty()) {
                when(order){
                    OrderFilter.DESC ->{
                        dataList.sortByDescending { it.name }
                    }

                    OrderFilter.ASC -> {
                        dataList.sortBy { it.name }
                    }
                }

                clb.onCallback(dataList)
            }
        }catch (e: IllegalArgumentException){
            Log.w(" firebaseSortByCategoryNameDescending", "IllegalArgumentException", e)
        } catch (e: IndexOutOfBoundsException){
            Log.w(" firebaseSortByCategoryNameDescending", "IndexOutOfBoundsException", e)
        }
    }

    private fun firebaseFilterByPrice(from: Int = 0, to: Int?/*, clb:MyCallback*/){
        try{
           // if(dataList.isNotEmpty()) {
                if (to == null || to == 0 || to < from){
                    dataList.filter {
                        it.price >= from
                    }
                }
                else{
                    dataList.filter {
                        it.price >= from && it.price <= to
                    }
                }

//                clb.onCallback(dataList)
           // }
        }catch (e: IllegalArgumentException){
            Log.w(" firebaseSortByCategoryNameDescending", "IllegalArgumentException", e)
        } catch (e: IndexOutOfBoundsException){
            Log.w(" firebaseSortByCategoryNameDescending", "IndexOutOfBoundsException", e)
        }
    }

//    private fun firebaseCategoryFilters() = listOf<(Item) -> Boolean>(
//        { it.category == Category.FOOD},
//        { it.category == Category.HOME},
//        { it.category == Category.SPORT}
//    )

    private fun firebaseFilterByCategoryName(filters: List<(Item) -> Boolean>/*, clb: MyCallback*/){
        try{
            //if(dataList.isNotEmpty()) {
                dataList.filter {
                    category -> filters.all { filter -> filter(category) }
                }

               // clb.onCallback(dataList)
           // }
        }catch (e: IllegalArgumentException){
            Log.w(" firebaseSortByCategoryNameDescending", "IllegalArgumentException", e)
        } catch (e: IndexOutOfBoundsException){
            Log.w(" firebaseSortByCategoryNameDescending", "IndexOutOfBoundsException", e)
        }
    }

    fun firebaseApplyFilters(clb: MyCallback){
        if(dataList.isNotEmpty()){
            firebaseFilterByPrice(priceFrom, priceTo)
            firebaseFilterByCategoryName(checkboxFilters)

            clb.onCallback(dataList)
        }
    }
}