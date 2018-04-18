package com.example.saba.sampleKotlin.domain.model.dbModels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "owner")
data class OwnerDbModel(@PrimaryKey val id: Int,
                        val name: String,
                        val avatarUrl: String)