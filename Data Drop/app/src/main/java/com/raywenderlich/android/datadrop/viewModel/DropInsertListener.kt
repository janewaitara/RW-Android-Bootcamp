package com.raywenderlich.android.datadrop.viewModel

import com.raywenderlich.android.datadrop.model.Drop

interface DropInsertListener {
    fun dropInserted(drop: Drop)
}