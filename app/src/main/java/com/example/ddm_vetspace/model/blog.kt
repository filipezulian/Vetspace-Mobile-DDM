package com.example.ddm_vetspace.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blog")
data class Blog(
    @PrimaryKey(autoGenerate = true) val blog_id: Int = 0,
    val descricao: String? = null,
    val user_id: Int? = null,
    val titulo: String? = null,
    val data: String? = null
)

