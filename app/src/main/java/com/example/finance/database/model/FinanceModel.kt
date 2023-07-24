package com.example.finance.database.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.finance.database.converters.Converters

@Entity
@TypeConverters(Converters::class)
class FinanceModel() : Parcelable{
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @ColumnInfo(name = "nominal")
    var nominal : Int = 0
    @ColumnInfo(name = "desc")
    var desc : String = ""
    @ColumnInfo(name = "date")
    var date : String = ""
    @ColumnInfo(name = "type")
    var type : String = ""
    @ColumnInfo(name = "month")
    var month: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        nominal = parcel.readInt()
        desc = parcel.readString().toString()
        date = parcel.readString().toString()
        type = parcel.readString().toString()
        month = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(nominal)
        parcel.writeString(desc)
        parcel.writeString(date)
        parcel.writeString(type)
        parcel.writeString(month)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FinanceModel> {
        override fun createFromParcel(parcel: Parcel): FinanceModel {
            return FinanceModel(parcel)
        }

        override fun newArray(size: Int): Array<FinanceModel?> {
            return arrayOfNulls(size)
        }
    }
}