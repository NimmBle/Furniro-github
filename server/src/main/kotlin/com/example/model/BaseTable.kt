package com.example.model

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date

abstract class BaseTable(name: String) : UUIDTable(name) {
    val creationDate = date("creation_date")
    val lastUpdatedDate = date("last_updated_date")
}