package com.raywenderlich.android.datadrop.model

//defines the structure of the db
object DropDbSchema {

    const val VERSION = 1
    const val DB_NAME = "drops.db"

    object DropTable{
        const val  NAME = "drops"

        object Columns{
            const val ID = "id"
            const val LATITUDE = "latitude"
            const val LONGITUDE = "longitude"
            const val DROP_MESSAGE = "dropMessage"
        }
    }
}