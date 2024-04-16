package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.Tag
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the tags table
 */
@Dao
interface TagDao: BaseDao<Tag> {
    /**
     * Returns a list of tags that the transaction has
     *
     * @param transactionId - The ID of the transaction to get the tags to
     */
    @Query("SELECT * FROM tags INNER JOIN transaction_tag_cross_refs ON tag_name = name WHERE transaction_id = :transactionId")
    fun getTags(transactionId: Int): Flow<List<Tag>>
}