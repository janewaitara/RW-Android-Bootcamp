package com.raywenderlich.android.datadrop.viewModel

import com.raywenderlich.android.datadrop.model.Drop

interface ClearDropsListener {
    fun dropCleared(drop: Drop)
}