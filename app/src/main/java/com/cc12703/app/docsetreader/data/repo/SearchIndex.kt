package com.cc12703.app.docsetreader.data.repo

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "searchIndex")
data class SearchIndex(
        @DatabaseField(columnName="id", id=true) var index: Int? = null,
        @DatabaseField(columnName="name") var name: String? = null,
        @DatabaseField(columnName="type") var type: String? = null,
        @DatabaseField(columnName="path") var path: String? = null
)
