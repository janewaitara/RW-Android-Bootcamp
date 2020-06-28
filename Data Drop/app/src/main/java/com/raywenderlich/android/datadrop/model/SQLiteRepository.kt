package com.raywenderlich.android.datadrop.model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.raywenderlich.android.datadrop.app.DataDropApplication
import com.raywenderlich.android.datadrop.model.DropDbSchema.DropTable
import com.raywenderlich.android.datadrop.viewModel.ClearAllDropsListener
import com.raywenderlich.android.datadrop.viewModel.ClearDropsListener
import com.raywenderlich.android.datadrop.viewModel.DropInsertListener
import java.io.IOException

class SQLiteRepository : DropRepository {

    private val database = DropDbHelper(DataDropApplication.getAppContext()).writableDatabase

    override fun addDrop(drop: Drop,listener: DropInsertListener) {
        //get a contentValue object from the drop an use insert method in the db to insert the drop
        val contentValues = getDropContentValues(drop)
        val result = database.insert(DropTable.NAME,null,contentValues)

        //get a result from the insert and call the listener if insert was successful
        if (result != -1L){
            listener.dropInserted(drop)
        }
    }

    override fun getDrops(): LiveData<List<Drop>> {

        val liveData = MutableLiveData<List<Drop>>()

        val drops = mutableListOf<Drop>()

        val cursor = queryDrops(null,null)

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast){
                drops.add(cursor.getDrop())
                cursor.moveToNext()
            }
        }catch (e: IOException){
            Log.e("SQLiteRepository", "Error reading drop")
        }finally {
            cursor.close()
        }

        liveData.value = drops

       return liveData
    }

    override fun clearDrop(drop: Drop, listener: ClearDropsListener) {
        val result = database.delete(DropTable.NAME,
                DropTable.Columns.ID + " = ?", // the question mark is replaced by the drop id passed into the array
                arrayOf(drop.id)
        )
        if (result != 0){
            listener.dropCleared(drop)
        }
    }

    override fun clearAllDrops(listener: ClearAllDropsListener) {
        val result = database.delete(DropTable.NAME, null,null)

        if ( result != 0){
            listener.allDropsCleared()
        }

    }
    private fun getDropContentValues(drop: Drop): ContentValues{
        val contentValues = ContentValues()
        contentValues.put(DropTable.Columns.ID, drop.id)
        contentValues.put(DropTable.Columns.LATITUDE, drop.latLng.latitude)
        contentValues.put(DropTable.Columns.LONGITUDE, drop.latLng.longitude)
        contentValues.put(DropTable.Columns.DROP_MESSAGE, drop.dropMessage)
        contentValues.put(DropTable.Columns.MARKER_COLOR, drop.markerColor)
        return contentValues
    }

    private fun queryDrops(where: String?, whereArgs: Array<String>?): DropCursorWrapper{
        //querying the database
        val cursor = database.query(
                DropTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null
        )
        return DropCursorWrapper(cursor)
    }
}