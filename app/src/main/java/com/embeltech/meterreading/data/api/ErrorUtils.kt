
package com.embeltech.meterreading.data.api

import com.embeltech.meterreading.data.repository.remote.model.APIError
import com.google.gson.Gson
import retrofit2.Response

/**
 * Class used to parse the error response received in api calling
 */
object ErrorUtils {

    fun<T> parseError(response: Response<T>): APIError {

        return Gson().fromJson(response.errorBody()!!.charStream(), APIError::class.java)
    }
}