package com.pklos.payshop.utils

interface DialogInterfaceListener {
    fun firebaseSortByCategoryNameDescending()
    fun firebaseSortByCategoryNameAscending()
    fun firebaseSortPriceDescending()
    fun firebaseSortPriceAscending()
    fun firebaseSortNameDescending()
    fun firebaseSortNameAscending()
    fun firebaseApplyFilters()
}