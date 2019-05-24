package io.wax911.sample.core.model.contract

import io.wax911.sample.core.util.ThumbnailHelper

interface TraktEntity {

    val id: Int
    val title: String
    val year: Int

    // extended info
    val overview: String?
    val runtime: Int?
    val country: String?
    val trailer: String?
    val homepage: String?
    val rating: Double?
    val votes: Int?
    val updated_at: String?
    val language: String?
    val genres: List<String>?
    val certification: String?

    fun getBanner() = ThumbnailHelper.getThumbnailFromUrl(trailer)
}